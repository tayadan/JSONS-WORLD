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
        // Allows you to specify a custom path for the properties file.
        ConfigurationFile config = new ConfigurationFile(args.length == 0 ? "config.properties" : args[0]);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost post = new HttpPost("https://api.openai.com/v1/responses");
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + config.getProperty("api.key"));

        String input = config.getProperty("prompt");

        input = input.replaceAll("\"", "\\\\\"");
        StringEntity entity = new StringEntity("{"
                + "\"model\": \"" +  config.getProperty("llm.model")  + "\","
                + "\"input\": \"" + input + "\"}");
        post.setEntity(entity);

        String message = InputProcessor.processResponse(EntityUtils.toString(httpClient.execute(post).getEntity()));
        System.out.println(message);
    }
}
