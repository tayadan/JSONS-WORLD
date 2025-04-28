package com.JSONsWorld.main.story;

import com.JSONsWorld.main.ConfigurationFile;
import com.JSONsWorld.main.api.ContextManager;
import com.JSONsWorld.main.api.OutputProcessor;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;


public class TranslationProcessor {

    public static ConfigurationFile config;

    public static String translate(String language, String text) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + config.getProperty("api.key"));

        StringEntity entity = new StringEntity("{"
                + "\"model\": \"" +  config.getProperty("llm.model")  + "\","
                + ContextManager.translateRequest(language, text) + "}");
        post.setEntity(entity);

        return OutputProcessor.processResponse(EntityUtils.toString(httpClient.execute(post).getEntity()));
    }


}
