package com.bp.devtracecli.commands;


import com.bp.devtracecli.DevTraceCLI;
import com.bp.devtracecli.client.DevTraceClient;
import com.bp.devtracecli.util.CLIPrinter;
import picocli.CommandLine;

@CommandLine.Command(
        name = "stop",
        description = "stop capturing API requests"
)
public class StopCommand implements Runnable {

    @CommandLine.ParentCommand
    private DevTraceCLI parent;

    @Override
    public void run(){

        String serverUrl = parent.getServerUrl();

        DevTraceClient client = new DevTraceClient(serverUrl);

        try {
            String response = client.stopCapture();

            CLIPrinter.success("Capture Stopped");

        } catch (Exception e) {
            CLIPrinter.error("failed to stop capture");
            CLIPrinter.error(e.getMessage());
        }
    }
}
