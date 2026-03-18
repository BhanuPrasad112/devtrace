package com.bp.devtraceserver.service;

import com.bp.devtraceserver.dto.ReplayResponseDTO;
import com.bp.devtraceserver.model.EditedRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.bp.devtraceserver.model.ApiRequestLog;
import com.bp.devtraceserver.repository.ApiRequest;
import lombok.Data;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;
import java.util.*;

@Service
@Data
@RequiredArgsConstructor


public class CaptureService {

    private final ApiRequest apiRequestRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private boolean isCaptureEnabled = false;

    public void startCapturing(){
        this.isCaptureEnabled = true;

    }

    public void stopCapturing(){
        this.isCaptureEnabled = false;
    }


    public void SaveAllRequests(ApiRequestLog requestLog) {
        if(isCaptureEnabled) {
            apiRequestRepository.save(requestLog); }

    }


    public List<ApiRequestLog> getAllRequests(){
        return   apiRequestRepository.findAll();


    }

    public Optional<ApiRequestLog> getRequestById(Long id){

        return apiRequestRepository.findById(id);


    }

    public void deleteRequests(){
        apiRequestRepository.deleteAll();
    }

    public void deleteRequestById(Long id) {
        apiRequestRepository.deleteById(id);
    }

    public List<Map<String, Object>> exportRequests() {
        List<ApiRequestLog> logs = apiRequestRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> exportData = new ArrayList<>();

        for (ApiRequestLog log : logs) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", log.getId());
            item.put("method", log.getMethod());
            item.put("url", log.getScheme() + "://" + log.getHost() + ":" + log.getPort() + log.getPath());
            item.put("responseStatus", log.getResponseStatus());
            item.put("responseBody", log.getResponseBody());
            item.put("requestBody", log.getRequestBody());
            item.put("timeStamp", log.getTimestamp());


            try {
                String rawHeaders = log.getHeaders();

                if (rawHeaders != null && rawHeaders.startsWith("\"")) {
                    rawHeaders = mapper.readValue(rawHeaders, String.class);
                }

                Map<String, Object> headerMap = mapper.readValue(rawHeaders, new TypeReference<>() {});
                item.put("headers", headerMap);
            } catch (Exception e) {
                item.put("headers", log.getHeaders());
            }

            exportData.add(item);
        }
        return exportData;
    }


public ReplayResponseDTO replayRequest(Long id, EditedRequest edited) throws JsonProcessingException {
    ApiRequestLog log = apiRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));


    String url = String.format("%s://%s:%d%s", log.getScheme(), log.getHost(), log.getPort(), log.getPath());
    if (log.getQueryParams() != null && !log.getQueryParams().isEmpty()) {
        url += "?" + log.getQueryParams();
    }

    ObjectMapper objectMapper = new ObjectMapper();
    HttpHeaders httpHeaders = new HttpHeaders();


    String rawHeaders = edited.getHeaders();
    if (rawHeaders != null && !rawHeaders.isBlank()) {
        String cleaned = rawHeaders.trim();

        if (cleaned.startsWith("{") && cleaned.endsWith("}")) {
            cleaned = cleaned.substring(1, cleaned.length() - 1);
        }


        String[] pairs = cleaned.split("(?<=\\]),\\s*");
        for (String pair : pairs) {
            if (pair.contains("=") || pair.contains(":")) {
                String sep = pair.contains("=") ? "=" : ":";
                String[] parts = pair.split(sep, 2);
                String key = parts[0].trim().replaceAll("^\"|\"$", "");


                String val = parts[1].trim()
                        .replaceAll("^\\[|\\]$", "")
                        .trim();


                List<String> forbidden = Arrays.asList("host", "content-length", "connection", "postman-token", "accept-encoding");

                if (!forbidden.contains(key.toLowerCase())) {
                    httpHeaders.add(key, val);
                }
            }
        }
    }


    String finalBody = "";
    if (edited.getBody() != null && !edited.getBody().isBlank()) {
        String bodyStr = edited.getBody().trim();


        if (bodyStr.startsWith("{") && bodyStr.contains(":")) {
            finalBody = bodyStr;
        } else {

            Map<String, Object> map = new HashMap<>();
            String content = bodyStr.replaceAll("^\\{|\\}$", "");
            String[] pairs = content.split(",\\s*");

            for (String pair : pairs) {
                if (pair.contains("=")) {
                    String[] kv = pair.split("=", 2);
                    String k = kv[0].trim();
                    String v = kv[1].trim();


                    if (v.matches("-?\\d+")) {
                        map.put(k, Long.parseLong(v));
                    } else {
                        map.put(k, v);
                    }
                }
            }
            finalBody = objectMapper.writeValueAsString(map);
        }
    }
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);


    ReplayResponseDTO dto = new ReplayResponseDTO();
    long startTime = System.currentTimeMillis();
    long latency = 0;
    try {
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.valueOf(log.getMethod()),
                new HttpEntity<>(finalBody, httpHeaders),
                String.class
        );

        latency = System.currentTimeMillis() - startTime;

        dto.setStatusCode(response.getStatusCode().value());
        dto.setResponseBody(response.getBody());

        Map<String, List<String>> resHeaders = new HashMap<>();
        response.getHeaders().forEach(resHeaders::put);
        dto.setHeaders(resHeaders);

    } catch (org.springframework.web.client.HttpStatusCodeException e) {


        latency = System.currentTimeMillis() - startTime;
        dto.setStatusCode(e.getStatusCode().value());
        dto.setResponseBody(e.getResponseBodyAsString());
        Map<String, List<String>> errHeaders = new HashMap<>();
        if (e.getResponseHeaders() != null) e.getResponseHeaders().forEach(errHeaders::put);
        dto.setHeaders(errHeaders);
    }

    ApiRequestLog replayRecord = new ApiRequestLog();
    replayRecord.setMethod(log.getMethod());
    replayRecord.setScheme(log.getScheme());
    replayRecord.setHost(log.getHost());
    replayRecord.setPort(log.getPort());

    replayRecord.setPath(log.getPath() + " (replay)");
    replayRecord.setQueryParams(log.getQueryParams());
    replayRecord.setRequestBody(finalBody);
    replayRecord.setResponseStatus(dto.getStatusCode());
    replayRecord.setResponseBody(dto.getResponseBody());
    replayRecord.setTimestamp(LocalDateTime.now());
    replayRecord.setClientIp("127.0.0.1");
    replayRecord.setLatency(latency);


    apiRequestRepository.save(replayRecord);

    dto.setReplayedAt(LocalDateTime.now());
    return dto;
}

}
