package com.bp.devtraceserver.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ReplayResponseDTO {

    private int statusCode;

    private String responseBody;

    private Map<String, List<String>> headers;

    private LocalDateTime replayedAt;

}
