
package com.bp.devtraceserver.mapper;

import com.bp.devtraceserver.dto.ApiRequestResponse;
import com.bp.devtraceserver.model.ApiRequestLog;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ApiRequestLogMapper  {

    private static final ObjectMapper jsonMapper = new ObjectMapper();



    public static ApiRequestResponse toDTO(ApiRequestLog log) {
        ApiRequestResponse dto = new ApiRequestResponse();
        dto.setId(log.getId());

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(log.getScheme() != null ? log.getScheme() : "http")
                .append("://")
                .append(log.getHost() != null ? log.getHost() : "localhost")
                .append(":")
                .append(log.getPort() != 0 ? log.getPort() : "8080")
                .append(log.getPath() != null ? log.getPath() : "");

        if (log.getQueryParams() != null && !log.getQueryParams().isEmpty()) {
            urlBuilder.append("?").append(log.getQueryParams());
        }

        dto.setUrl(urlBuilder.toString());
        dto.setMethod(log.getMethod());
        if (log.getHeaders() != null) {

            try {
                String rawHeaders = log.getHeaders();
                if (rawHeaders.startsWith("\"")) {
                    rawHeaders = jsonMapper.readValue(rawHeaders, String.class);
                }

                Map<String, Object> headerMap = jsonMapper.readValue(
                        rawHeaders,
                        new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {}
                );

                dto.setHeaders(headerMap);

            } catch (Exception e) {
                // Fallback to simple toString if JSON conversion fails
                dto.setHeaders(new java.util.HashMap<>());
            }
        }


        if (log.getRequestBody() != null) {
            try {
                String rawReq = log.getRequestBody();

                if (rawReq.startsWith("\"")) {
                    rawReq = jsonMapper.readValue(rawReq, String.class);
                }

                dto.setRequestBody(jsonMapper.readValue(rawReq, Object.class));
            } catch (Exception e) {
                dto.setRequestBody(log.getRequestBody());
            }
        }


        if (log.getResponseBody() != null) {
            try {
                String rawRes = log.getResponseBody();
                if (rawRes.startsWith("\"")) {
                    rawRes = jsonMapper.readValue(rawRes, String.class);
                }
                // Parse into Object so Jackson renders it as a nested tree
                dto.setResponseBody(jsonMapper.readValue(rawRes, Object.class));
            } catch (Exception e) {
                dto.setResponseBody(log.getResponseBody());
            }
        }


        dto.setResponseStatus(log.getResponseStatus() != 0 ? String.valueOf(log.getResponseStatus()) : "0");

        dto.setLatency(log.getLatency());
        dto.setTimeStamp(log.getTimestamp().toString());

        return dto;
    }









   public static  List<ApiRequestResponse> toDTOList(List<ApiRequestLog> logs){
        return logs.stream()
                .map(ApiRequestLogMapper::toDTO)
                .collect(Collectors.toList());
   }




}
