package com.bp.devtracecli.commands;


import com.bp.devtracecli.DevTraceCLI;
import com.bp.devtracecli.client.DevTraceClient;
import com.bp.devtracecli.util.CLIPrinter;
import picocli.CommandLine;

@CommandLine.Command(
        name="status",
        description = "check capture Status"
)
public class StatusCommand implements  Runnable{

    @CommandLine.ParentCommand
    private DevTraceCLI parent;

    @Override public void run(){

        String serverUrl = parent.getServerUrl();

        DevTraceClient client = new DevTraceClient(serverUrl);

        try {
            String response = client.getStatus();

            if(response!= null && response.toLowerCase().contains("true")) {
                CLIPrinter.success("Capture Status: ENABLED");

            } else {
                CLIPrinter.info("Capture Status : DISABLED");
            }

        } catch (Exception e) {
            CLIPrinter.error("failed to get status");
            CLIPrinter.error(e.getMessage());
        }
    }
}
