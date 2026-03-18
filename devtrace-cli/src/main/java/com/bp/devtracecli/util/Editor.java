package com.bp.devtracecli.util;

import com.bp.devtracecli.model.EditedRequest;
import com.bp.devtracecli.model.RequestDetails;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class Editor {


    public static EditedRequest openEditor(RequestDetails request) throws Exception {

        File file = File.createTempFile("devtrace_replay", ".txt");

        String content = "METHOD: " + request.getMethod() + "\n" +
                "URL: " + request.getUrl() + "\n\n" +
                "HEADERS:\n" +
                request.getHeaders() + "\n\n" +
                "BODY:\n" +
                request.getRequestBody();

        Files.writeString(file.toPath(), content);

        ProcessBuilder pb = new ProcessBuilder("notepad.exe", file.getAbsolutePath());
        pb.inheritIO();

        Process process = pb.start();
        process.waitFor();

        List<String> lines = Files.readAllLines(file.toPath());

        StringBuilder headers = new StringBuilder();
        StringBuilder body = new StringBuilder();

        boolean headerSection = false;
        boolean bodySection = false;

        for(String line : lines) {

            if(line.equals("HEADERS:")) {
                headerSection =true;
                bodySection = false;
                continue;
            }

            if(line.equals("BODY:")) {
                headerSection = false;
                bodySection = true;
                continue;
            }

            if(headerSection) {
                headers.append(line).append("\n");
            }

            if(bodySection) {
                body.append(line).append("\n");
            }
        }

        return new EditedRequest(
                headers.toString().trim(),
                body.toString().trim()
        );

    }
}
