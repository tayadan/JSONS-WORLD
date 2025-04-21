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

    //idk how to access this file without the absolute path
    //pls change it later
    //pose_pairings.tsv but only first 11 lines to save "credits", since the main tsv is big.
    private static final String TSV_FILE = "pose_pairings_test.tsv";
    private static final String TRANSLATION_FILE = "english-spanish.tsv"; //idk which language yet

    public static ConfigurationFile config;

    //please build this for the buildTranslationFile to work.
    //when using API call. make sure the prompt that mentions which language to use
    //is taken from config, new variable preferredlanguage. he mentioned something on that
    //so we are able to in theory make any sort of translated file with EN-preferredlanguage.tsv
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

    private static String generateTranslationText(ArrayList<String> text) {
        StringBuilder returnMe = new StringBuilder();
        for(String line : text) {
            String[] lineValues = line.split("\t");

            if(lineValues.length >= 2) {
                returnMe.append(lineValues[1]);
                if(lineValues.length >= 3) returnMe.append("\t").append(lineValues[2]);
            }
            returnMe.append("\n");
        }
        return returnMe.toString().trim();
    }

    private static void insertTranslatedText(ArrayList<String> text, String translatedText) {
        String[] splitTranslation = translatedText.split("\\\\n");

        int pos = 0;
        for(String line : text) {
            String[] lineValues = line.split("\t");
            String[] splitTranslatedLine = splitTranslation[pos].split("  ( )+");

            if(lineValues.length >= 2) {
                lineValues[1] = lineValues[1] + "   " + splitTranslatedLine[0];
                if(splitTranslatedLine.length == 2) lineValues[2] = lineValues[2] + "   " + splitTranslatedLine[1];
            }

            text.set(pos, String.join("   ", lineValues));

            pos++;
        }
    }


}
