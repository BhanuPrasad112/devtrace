package com.bp.devtracecli.client;

import com.bp.devtracecli.model.EditedRequest;
import com.bp.devtracecli.model.RequestDetails;
import com.bp.devtracecli.model.RequestSummary;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class DevTraceClient {

    private final String baseUrl;

    public DevTraceClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    private final OkHttpClient client = new OkHttpClient();

    private ObjectMapper mapper = new ObjectMapper();



    public String startCapture() throws IOException {

        Request request = new Request.Builder()
                .url(baseUrl+ "/devtrace/capture/start")
                .post(okhttp3.RequestBody.create(new byte[0]))
                .build();

        try(Response response = client.newCall(request).execute()) {
            return  response.body().string();
        }
    }


    public String stopCapture() throws IOException {

        Request request = new Request.Builder()
                .url(baseUrl + "/devtrace/capture/stop")
                .post(okhttp3.RequestBody.create(new byte[0]))
                .build();

        try(Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    public String getStatus() throws IOException,NullPointerException {

        Request request = new Request.Builder()
                .url(baseUrl + "/devtrace/capture/status")
                .get()
                .build();

        try(Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    public List<RequestSummary> getRequests() throws IOException {

        Request request = new Request.Builder()
                .url(baseUrl + "/devtrace/requests")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String json = response.body().string();

            return mapper.readValue(
                    json,
                    mapper.getTypeFactory()
                            .constructCollectionType(List.class, RequestSummary.class));
        }
    }

        public String  exportRequests() throws IOException{

        Request request = new Request.Builder()
                .url(baseUrl + "/devtrace/requests/export")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()){
            return response.body().string();

        }
    }



    public String replayRequest(Long id, EditedRequest edited) throws IOException {


        String jsonPayload = mapper.writeValueAsString(edited);

        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(
                jsonPayload,
                okhttp3.MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(baseUrl  + "/devtrace/requests/replay/" + id)
                .post(requestBody)
                .build();

        try(Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public RequestDetails getRequestById(long id) throws IOException {

        Request request = new Request.Builder()
                .url(baseUrl + "/devtrace/requests/" + id)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();


            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, RequestDetails.class);
        }
    }

    public String deleteRequestById(Long id) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/devtrace/requests/" + id)
                .delete()
                .build();

        try(Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String deleteAllRequests() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/devtrace/requests" )
                .delete()
                .build();

        try(Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
