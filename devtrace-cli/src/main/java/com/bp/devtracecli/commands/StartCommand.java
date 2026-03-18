package com.bp.devtracecli.commands;


import com.bp.devtracecli.DevTraceCLI;
import com.bp.devtracecli.client.DevTraceClient;
import com.bp.devtracecli.util.CLIPrinter;
import picocli.CommandLine;

@CommandLine.Command(
        name="start",
        description = "start capturing API requests"
)
public class StartCommand implements  Runnable{

    @CommandLine.ParentCommand
    private DevTraceCLI parent;

    @Override
    public void run() {

        String serverUrl = parent.getServerUrl();
        DevTraceClient client = new DevTraceClient(serverUrl);

        try{
            String response = client.startCapture();
            CLIPrinter.success("Capture Started on :" + parent.getServerUrl());

        } catch (Exception e) {
            CLIPrinter.error("failed to capture");
            CLIPrinter.error(e.getMessage());
        }

    }
}
