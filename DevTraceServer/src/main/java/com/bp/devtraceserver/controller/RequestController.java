package com.bp.devtraceserver.controller;


import com.bp.devtraceserver.dto.ApiRequestResponse;
import com.bp.devtraceserver.dto.ReplayResponseDTO;
import com.bp.devtraceserver.mapper.ApiRequestLogMapper;
import com.bp.devtraceserver.model.ApiRequestLog;
import com.bp.devtraceserver.model.EditedRequest;
import com.bp.devtraceserver.service.CaptureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/devtrace/requests")
public class RequestController {

    @Autowired
    private CaptureService captureService;

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    @GetMapping
    public List<ApiRequestResponse> getAllRequests(){
        List<ApiRequestLog> logs = captureService.getAllRequests();

        return ApiRequestLogMapper.toDTOList(logs);

    }

    @GetMapping("/{id}")
    public ApiRequestResponse getRequestById(
            @PathVariable Long id
    ) {
        Optional<ApiRequestLog> log = captureService.getRequestById(id);

        if (log.isPresent()) {

            return ApiRequestLogMapper.toDTO(log.get());
        } else {
            throw new RuntimeException("request not found");
        }

    }

    @DeleteMapping
    public String deleteAllRequests() {

        captureService.deleteRequests();

        return "All requests logs deleted";

    }

    @DeleteMapping("/{id}")
    public String deleteRequestById(@PathVariable Long id) {
        captureService.deleteRequestById(id);

        return "request deleted successfully";
    }

    @PostMapping("/replay/{id}")
    public ResponseEntity<ReplayResponseDTO> replayRequest(
            @PathVariable Long id,
            @RequestBody EditedRequest edited
            ) throws Exception {

        ReplayResponseDTO response =  captureService.replayRequest(id, edited);
        return  ResponseEntity.ok(response);

    }

    @GetMapping("/export")
    public String exportRequest() throws JsonProcessingException {

        List<ApiRequestLog> logs = captureService.getAllRequests();
        List<ApiRequestResponse> dtos = ApiRequestLogMapper.toDTOList(logs);

        ObjectMapper mapper = new ObjectMapper();


        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtos);


    }
}
