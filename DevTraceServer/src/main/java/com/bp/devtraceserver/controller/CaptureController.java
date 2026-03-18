package com.bp.devtraceserver.controller;


import com.bp.devtraceserver.service.CaptureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/devtrace/capture")
@RequiredArgsConstructor
public class CaptureController {

    private final CaptureService service;

    @PostMapping("/start")
    public Map<String,String> startCapture(){
        service.startCapturing();
        return Map.of("message", "Request capturing enabled");
    }

    @PostMapping("/stop")
    public Map<String,String> stopCapture(){
        service.stopCapturing();
        return Map.of("message", "request capturing stopped");
    }

    @GetMapping("/status")
    public Map<String,Boolean> captureStatus(){
        boolean status = service.isCaptureEnabled();
        return Map.of("captureEnabled",status);
    }

}
