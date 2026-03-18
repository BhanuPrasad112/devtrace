package com.bp.devtracecli.util;

import com.bp.devtracecli.model.RequestDetails;
import com.bp.devtracecli.model.RequestSummary;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class CLIPrinter {

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";

    // Basic Loggers
    public static void success(String message) {
        System.out.println(GREEN + message + RESET);
    }

    public static void error(String message) {
        System.out.println(RED + message + RESET);
    }

    public static void warning(String message) {
        System.out.println(YELLOW + message + RESET);
    }

    public static void info(String message) {
        System.out.println(CYAN  + message + RESET);
    }


    public static void printTableHeader() {
        System.out.printf("%-5s %-8s %-50s %-8s %-8s%n", "ID", "METHOD", "URL", "STATUS", "LATENCY");
        printDivider();
    }

    public static void printDivider() {
        System.out.println("---------------------------------------------------------------------------------------");
    }

    public static void printRequestRow(RequestSummary req) {
        String methodColor = getMethodColor(req.getMethod());

        String statusColor = getStatusColor(String.valueOf(req.getResponseStatus()));




        System.out.printf("%-5d %s%-8s%s %-50s %s%-8s%s %-8dms%n",
                req.getId(),
                methodColor, req.getMethod(), RESET,
                req.getUrl(),
                statusColor, req.getResponseStatus(), RESET,
                req.getLatency());
    }

    private static String getMethodColor(String method) {
        if (method == null) return RESET;
        return switch (method.toUpperCase()) {
            case "GET" -> GREEN;
            case "POST" -> BLUE;
            case "PUT" -> YELLOW;
            case "DELETE" -> RED;
            case "PATCH" -> PURPLE;
            default -> CYAN;
        };
    }

    private static String getStatusColor(String status) {
        if (status == null) return RESET;
        if (status.startsWith("2")) return GREEN;  // Success
        if (status.startsWith("3")) return CYAN;   // Redirection
        if (status.startsWith("4")) return YELLOW; // Client Error
        if (status.startsWith("5")) return RED;    // Server Error
        return RESET;
    }


    public static void printRequestDetails(RequestDetails request) {
        success("Request Details");
        printDivider();

        System.out.println("ID: " + request.getId());
        System.out.println("Method: " + request.getMethod());
        System.out.println("URL: " + request.getUrl());
        System.out.println("Status: " + request.getResponseStatus());
        System.out.println("Latency: " + request.getLatency() + " ms");

        System.out.println("\n" + CYAN + "Headers:" + RESET);
        if (request.getHeaders() != null) {
            request.getHeaders().forEach((key, value) -> {
                System.out.println("  " + key + ": " + value);
            });
        }


        ObjectMapper prettyMapper = new ObjectMapper();

        System.out.println("\n" + CYAN + "RequestBody:" + RESET);
        printFormattedJson(prettyMapper, request.getRequestBody());

        System.out.println("\n" + CYAN + "ResponseBody:" + RESET);
        printFormattedJson(prettyMapper, request.getResponseBody());

        System.out.println(request.getTimeStamp());
    }

    private static void printFormattedJson(ObjectMapper mapper, Object data) {
        if (data == null || data.toString().isEmpty()) {
            System.out.println("  (empty)");
            return;
        }
        try {

            String pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            System.out.println(pretty);
        } catch (Exception e) {
            System.out.println(data);
        }
    }


    public static void printReplayResponse(String responseBody) {
        success("Replay Response");
        printDivider();
        System.out.println(formatJson(responseBody));
    }

    private static String formatJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return "  (empty)";
        }
        try {
            ObjectMapper mapper = new ObjectMapper();

            Object obj = mapper.readValue(json, Object.class);

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {

            return "  " + json;
        }

    } }

