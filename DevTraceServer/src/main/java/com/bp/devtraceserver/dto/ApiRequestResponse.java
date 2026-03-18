package com.bp.devtraceserver.dto;



import lombok.Data;


import java.util.Map;

@Data

public class ApiRequestResponse {

    private Long id;

    private String url;

    private String method;


    private Map<String, Object> headers;

    private Object requestBody;

    private Object responseBody;

    private String responseStatus;

    private Long latency;

    private String timeStamp;

}
