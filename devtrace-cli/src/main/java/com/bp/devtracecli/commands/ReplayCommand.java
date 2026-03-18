package com.bp.devtracecli.commands;


import com.bp.devtracecli.DevTraceCLI;
import com.bp.devtracecli.client.DevTraceClient;
import com.bp.devtracecli.model.EditedRequest;
import com.bp.devtracecli.model.RequestDetails;
import com.bp.devtracecli.util.CLIPrinter;
import com.bp.devtracecli.util.Editor;
import picocli.CommandLine;

@CommandLine.Command(
        name="replay",
        description = "Replay a Captured API request"
)
public class ReplayCommand implements Runnable{

    @CommandLine.Parameters(index = "0", description = "Request ID to replay")
    private Long id;

    @CommandLine.ParentCommand
    private DevTraceCLI parent;

    @Override
    public void run(){

        String serverUrl = parent.getServerUrl();

        DevTraceClient client = new DevTraceClient(serverUrl);

        try {
            RequestDetails original = client.getRequestById(id);

            if (original.getUrl() != null && original.getUrl().contains("(replay)")) {
                CLIPrinter.error("Action Denied: Request ID " + id + " is already a replayed record.");
                CLIPrinter.info("To maintain data integrity, you can only replay original captured requests.");
                return;
            }

            CLIPrinter.info("Opening editor for Request ID: " + id);
            EditedRequest edited = Editor.openEditor(original);
            CLIPrinter.info("Replay Request ID :" + id);

            String response = client.replayRequest(id, edited);

            CLIPrinter.success("Replay Response");
            CLIPrinter.printReplayResponse(response);
        } catch (Exception e) {

            CLIPrinter.error("Failed to replay request");
            CLIPrinter.error(e.getMessage());
        }

    }
}
