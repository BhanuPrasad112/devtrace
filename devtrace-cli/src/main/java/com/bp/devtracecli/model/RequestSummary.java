package com.bp.devtracecli.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestSummary {

    private Long id;
    private String method;
    private String url;
    private Integer responseStatus;
    private Long latency;
}
