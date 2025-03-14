package com.JSONsWorld.main;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
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
        //String apiKey = "";

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost post = new HttpPost("https://api.openai.com/v1/responses");
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + apiKey);
        //post.addHeader("OpenAI-Organization", "");

        String input = "say hello to John";

        StringEntity entity = new StringEntity("{"
                + "\"model\": \"gpt-4o\","
                + "\"input\": \"" + input + "\"}");
        post.setEntity(entity);


        //System.out.println(httpClient.execute(post));
        System.out.println(EntityUtils.toString(httpClient.execute(post).getEntity()));
    }
}
