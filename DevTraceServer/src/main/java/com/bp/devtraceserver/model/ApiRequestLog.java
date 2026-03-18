package com.bp.devtraceserver.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
@Entity
@Table(name="api_request_logs")
public class ApiRequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String scheme;

    private String host;

    private Integer port;

    private String method;

    private String path;

    @Column(length = 1000)
    private String queryParams;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String headers;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String requestBody;

    private Integer responseStatus;

    private String clientIp;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String responseHeaders;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String responseBody;

    private Long latency;


    private LocalDateTime timestamp;


}
