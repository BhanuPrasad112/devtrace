package com.bp.devtracecli.commands;


import com.bp.devtracecli.DevTraceCLI;
import com.bp.devtracecli.client.DevTraceClient;
import com.bp.devtracecli.util.CLIPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;

@CommandLine.Command(
        name="export",
        description = "Export captured requests to JSON"
)
public class ExportCommand implements Runnable{

    @CommandLine.Parameters(index = "0", description = "Export file location", arity = "0..1")
    private String filePath;

    @CommandLine.ParentCommand
    private DevTraceCLI parent;

    @Override
    public void run(){

        String serverUrl = parent.getServerUrl();

        DevTraceClient client = new DevTraceClient(serverUrl);

        try {

            CLIPrinter.info("Fetching requests from server...");
            String json = client.exportRequests();

            ObjectMapper mapper = new ObjectMapper();
            Object obj = mapper.readValue(json,Object.class);

            String prettyJson = mapper
                    .enable(SerializationFeature.INDENT_OUTPUT)
                            .writeValueAsString(obj);
            Path path;

            if(filePath == null) {

                path = Path.of("D:\\devtrace-export.json");
            } else {
                path = Path.of(filePath);
            }

            Files.writeString(path,prettyJson);


            CLIPrinter.success("Exported to :" + path.toAbsolutePath());
        

        } catch (Exception e) {
            CLIPrinter.error("Failed to export Requests");
            CLIPrinter.error(e.getMessage());
        }
    }
}
