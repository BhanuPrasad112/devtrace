package com.bp.devtraceserver.filter;


import com.bp.devtraceserver.model.ApiRequestLog;
import com.bp.devtraceserver.service.CaptureService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class RequestCaptureFilter extends OncePerRequestFilter {

    private final CaptureService captureService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if(path.contains("/devtrace")) {
            filterChain.doFilter(request,response);
            return;
        }

        if(!captureService.isCaptureEnabled()){
            filterChain.doFilter(request,response);
            return;
        }

        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, 1000);

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(requestWrapper,responseWrapper);

        long latency = System.currentTimeMillis() - startTime;

        String requestBody = new String(
                requestWrapper.getContentAsByteArray(),
                requestWrapper.getCharacterEncoding()

        );


        String responseBody = new String(
                responseWrapper.getContentAsByteArray(),
                responseWrapper.getCharacterEncoding()
        );

        int status = responseWrapper.getStatus();


        Map<String, List<String>> requestHeaderMap = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {

            String headerName = headerNames.nextElement();

            List<String> values = Collections.list(request.getHeaders(headerName));

            requestHeaderMap.put(headerName, values);
        }

        //extracting response headers
        Map<String, List<String>> responseHeaderMap = new HashMap<>();

        for(String headerName : responseWrapper.getHeaderNames()) {

            List<String> values = new ArrayList<>(responseWrapper.getHeaders(headerName));

            responseHeaderMap.put(headerName, values);
        }


        ObjectMapper objectMapper = new ObjectMapper();

        String requestHeaders = objectMapper.writeValueAsString(requestHeaderMap);

        String responseHeaders = objectMapper.writeValueAsString(responseHeaderMap);

        ApiRequestLog log = ApiRequestLog.builder()
                .scheme(requestWrapper.getScheme())
                .host(requestWrapper.getServerName())
                .port(requestWrapper.getServerPort())
                .method(requestWrapper.getMethod())
                .path(requestWrapper.getRequestURI())
                .queryParams(requestWrapper.getQueryString())
                .headers(requestHeaders)
                .requestBody(requestBody)
                .responseStatus(status)
                .clientIp(requestWrapper.getRemoteAddr())
                .responseHeaders(responseHeaders)
                .responseBody(responseBody)
                .latency(latency)
                .timestamp(LocalDateTime.now())
                .build();

        captureService.SaveAllRequests(log);

        responseWrapper.copyBodyToResponse();


    }
}
