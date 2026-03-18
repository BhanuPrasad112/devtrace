package com.bp.devtracecli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestDetails {

    private Long id;

    private String method;

    private String url;

    private Map<String, Object> headers;

    private Object requestBody;

    private Integer responseStatus;
    private Long latency;
    private Map<String, Object> responseHeaders;
    private Object responseBody;

    private String timeStamp;
}
