package com.bp.devtracecli;


import com.bp.devtracecli.commands.*;
import picocli.CommandLine;

@CommandLine.Command(
        name = "devTrace",
        description = "DevTrace CLI - Capture and replay HTTP requests",
        mixinStandardHelpOptions = true,
        version = "DevTrace CLI 0.1.0",
        subcommands = {
                StartCommand.class,
                StopCommand.class,
                StatusCommand.class,
                ExportCommand.class,
                ListCommand.class,
                ReplayCommand.class,
                WatchCommand.class,
                ShowCommand.class,
                DeleteCommand.class
        }

)
public class DevTraceCLI implements Runnable {

    @CommandLine.Option(
            names="--server",
            description = "DevTrace server URL",
            required = true
    )
    private String serverUrl;

    public String getServerUrl(){
        return serverUrl;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new DevTraceCLI()).execute(args);
        System.exit(exitCode);


    }

    @Override
    public void run(){
        System.out.println("DevTrace CLI");
        System.out.println("Use --help to see available commands");
    }
}
