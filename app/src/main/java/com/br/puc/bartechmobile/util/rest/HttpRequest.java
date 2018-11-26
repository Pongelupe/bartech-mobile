package com.br.puc.bartechmobile.util.rest;

import android.os.AsyncTask;

import com.br.puc.bartechmobile.util.JsonPathReader;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequest extends AsyncTask<String, Void, String> {

    private static final String URL = "https://api.graph.cool/simple/v1/cjlzqfhlg1f1v01420mlus4ma";
    private static final OkHttpClient client = new OkHttpClient();
    private final String method;
    private final String body;
    private final OnTaskFinished target;
    private final RequestEnum request;

    public HttpRequest(String method, String body, OnTaskFinished target, RequestEnum request) {
        this.method = method;
        this.body = body;
        this.target = target;
        this.request = request;
    }

    public HttpRequest(String body, OnTaskFinished target, RequestEnum request) {
        this("post", body, target, request);
    }

    @Override
    protected String doInBackground(String... request) {
        try {
            if (this.method.equalsIgnoreCase("post")) {
                return doPost();
            }
        } catch (IOException e) {

        }
        return null;
    }

    protected void onPostExecute(String response) {
        this.target.taskfinished(JsonPathReader.readFromJson(response, this.request.identifier, this.request.clazz), this.request);
    }

    private String doPost() throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), this.body);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();
        return client.newCall(request).execute().body().string();
    }

}
