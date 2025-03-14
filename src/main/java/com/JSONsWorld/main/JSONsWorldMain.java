package com.JSONsWorld.main;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class JSONsWorldMain {

    public static void main(String[] args) throws IOException {
        String apiKey = System.getenv("OpenAI_API_Key");

        CloseableHttpClient client = HttpClients.createDefault();


        HttpPost post = new HttpPost("https://api.openai.com/v1/responses");
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer sk-gol4pk1m5BwUEtSTJeS568ktKVDNeTXmyJ66T3BlbkF");
        post.addHeader("OpenAI-Organization".trim(), "org-gZ7peQP5XmIRVhs78U8WH");
        post.setEntity(new StringEntity("{\"model\": \"GPT4o-mini\"," +
                "\"input\":\"You are now a pirate. Explain http get and post requests to me\"}"));



        /*client.execute(post,
                response -> {
                    System.out.println(EntityUtils.toString(response.getEntity()));
                    return "";
                }
        );*/
        InputProcessor.runTestDataset();
    }
}
