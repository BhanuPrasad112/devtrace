package com.bp.devtracecli.commands;


import com.bp.devtracecli.DevTraceCLI;
import com.bp.devtracecli.client.DevTraceClient;
import com.bp.devtracecli.model.RequestDetails;
import com.bp.devtracecli.util.CLIPrinter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;

import java.util.Map;

@CommandLine.Command(
        name="show",
        description = "show full details of required request"
)
public class ShowCommand implements  Runnable{

    @CommandLine.Parameters(index ="0", description = "Request ID")
    private Long id;

    @CommandLine.ParentCommand
    private DevTraceCLI parent;


    @Override
    public void run() {
        String serverUrl = parent.getServerUrl();
        DevTraceClient client = new DevTraceClient(serverUrl);

        try {
            CLIPrinter.info("Fetching request details of request id :" + id);


            RequestDetails details = client.getRequestById(id);


            CLIPrinter.success("Request Details:");
            CLIPrinter.printRequestDetails(details);

        } catch (Exception e) {
            CLIPrinter.error("FAILED TO FETCH REQUESTS");
            CLIPrinter.error(e.getMessage());
        }
    }

}
