package com.bp.devtracecli.commands;


import com.bp.devtracecli.DevTraceCLI;
import com.bp.devtracecli.client.DevTraceClient;
import com.bp.devtracecli.util.CLIPrinter;
import picocli.CommandLine;

@CommandLine.Command(
        name="delete",
        description = "deletes the requests"
)
public class DeleteCommand implements Runnable {


    @CommandLine.Parameters(index = "0", description = "Request ID")
    private String target;

    @CommandLine.ParentCommand
    private DevTraceCLI parent;

    @Override
    public void run() {

        String serverUrl = parent.getServerUrl();

        DevTraceClient client = new DevTraceClient(serverUrl);

        try {
            if (target.equalsIgnoreCase("all")) {
                CLIPrinter.info("Deleting all request logs...");
                String response = client.deleteAllRequests();
                CLIPrinter.success(response);
            } else {

                Long id = Long.parseLong(target);
                CLIPrinter.info("Deleting request details of request id :" + id);

                String response = client.deleteRequestById(id);

                CLIPrinter.success(response);
            }


        } catch (NumberFormatException e) {
            CLIPrinter.error("INVALID INPUT: '" + target + "' is not a valid number. Use an ID or 'all'.");
        } catch (Exception e) {
            if (target.equalsIgnoreCase("all")) {
                CLIPrinter.error("FAILED TO DELETE ALL REQUESTS: " + e.getMessage());
            } else {
                CLIPrinter.error("FAILED TO DELETE REQUEST ID " + target + ": " + e.getMessage());
            }
        }
    }
}
