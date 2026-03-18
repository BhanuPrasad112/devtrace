package com.bp.devtracecli.commands;


import com.bp.devtracecli.DevTraceCLI;
import com.bp.devtracecli.client.DevTraceClient;
import com.bp.devtracecli.model.RequestSummary;
import com.bp.devtracecli.util.CLIPrinter;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(
        name = "list",
        description = "List captured API requests"
)
public class ListCommand implements Runnable {

    @CommandLine.ParentCommand
    private DevTraceCLI parent;


    @Override
    public void run(){

        String serverUrl = parent.getServerUrl();

        DevTraceClient client = new DevTraceClient(serverUrl);

        try {

            CLIPrinter.info("Fetching captured requests");
            List<RequestSummary> requests = client.getRequests();

            CLIPrinter.printTableHeader();



            for(RequestSummary req : requests) {

                CLIPrinter.printRequestRow(req);
            }

        } catch (Exception e) {

            CLIPrinter.error("Failed to fetch requests");
            CLIPrinter.error(e.getMessage());
        }

    }
}
