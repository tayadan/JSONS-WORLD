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
        //String apiKey = System.getenv("OpenAI_API_Key");
        //String apiKey = "";

        ConfigurationFile config = new ConfigurationFile("config.properties");

        String apiKey = config.getProperty("api.key");
        String model = config.getProperty("llm.model");

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost post = new HttpPost("https://api.openai.com/v1/responses");
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + apiKey);
        //post.addHeader("OpenAI-Organization", "");

        String input = "say hello to John";

        StringEntity entity = new StringEntity("{"
                + "\"model\": \"" +  model  + "\","
                + "\"input\": \"" + input + "\"}");
        post.setEntity(entity);


        //System.out.println(httpClient.execute(post));
        System.out.println(EntityUtils.toString(httpClient.execute(post).getEntity()));
    }
}
