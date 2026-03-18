package com.bp.devtracecli.commands;


import com.bp.devtracecli.DevTraceCLI;
import com.bp.devtracecli.client.DevTraceClient;
import com.bp.devtracecli.model.RequestSummary;
import com.bp.devtracecli.util.CLIPrinter;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name="watch", description = "watch API requests in real time")
public class WatchCommand implements  Runnable{


    @CommandLine.ParentCommand
    private DevTraceCLI parent;

    @Override
    public void run() {

        String serverUrl = parent.getServerUrl();
        DevTraceClient client = new DevTraceClient(serverUrl);

        CLIPrinter.info("🔍 Watching API requests.. Press CTRL+C to stop\n");

        long lastSeenId = 0;

        while(true) {

            try{

                List<RequestSummary> requests = client.getRequests();

                for(RequestSummary req : requests) {

                    long  currentId = req.getId();

                    if(currentId > lastSeenId) {
                        CLIPrinter.printRequestRow(req);
                        lastSeenId = currentId;
                    }
                }

                Thread.sleep(2000);
            } catch (Exception e) {

                CLIPrinter.error("watch error" + e.getMessage());

            }

        }


    }
}
