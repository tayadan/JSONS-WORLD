package com.JSONsWorld.main;

import com.JSONsWorld.main.api.ContextManager;
import com.JSONsWorld.main.api.OutputProcessor;
import com.JSONsWorld.main.vignettes.VignetteSchema;
import com.JSONsWorld.main.vignettes.VignetteManager;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class TranslationProcessor {

    //idk how to access this file without the absolute path
    //pls change it later
    //pose_pairings.tsv but only first 11 lines to save "credits", since the main tsv is big.
    private static final String TSV_FILE = "pose_pairings_test.tsv";
    private static final String TRANSLATION_FILE = "english-spanish.tsv"; //idk which language yet

    private static ConfigurationFile config;

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        TranslationProcessor.config = new ConfigurationFile(args.length == 0 ? "config.properties" : args[0]);
        List<VignetteSchema> extracted = extractFromTSV();
        //print rows of data as test on how it looks like
        for (VignetteSchema vignette : extracted) {
            //System.out.println(vignette.getCombinedText()); debug
        }
        buildTranslationFile(extracted); //english-spanish.tsv should be built!! in theory

        //VignetteManager manager = new VignetteManager();
        for(int i = 0; i<extracted.size(); i++){
            //manager.addVignette(extracted.get(i));
        }
    }


        //extracts tsv file and brings back list of vignette objects
    private static List<VignetteSchema> extractFromTSV() throws IOException {
        List<VignetteSchema> data = new ArrayList<>();

        ArrayList<String> lines = new ArrayList<>
                (List.of(Files.readString(new File(TranslationProcessor.TSV_FILE).toPath()).trim().split("\n")));
        lines.removeFirst();

        String translateMe = generateTranslationText(lines);
        String translated = translate(config.getProperty("language"), translateMe);
        translated = translated.replaceAll("\\\\t", "   ");

        insertTranslatedText(lines, translated);

        // Filters the lines then adds them to the list
        lines.removeIf(line -> line.trim().isEmpty());
        lines.forEach(line -> data.add(new VignetteSchema(line.split("   "))));

        return data; //return the list of objects
    }

    //we need translation part which gets translations for each of these and then assigns them in the object??
    //and then placing this data in a separate file with both english and "spanish" translations
    private static void buildTranslationFile(List<VignetteSchema> vignettes) throws IOException {
        StringBuilder writeMe = new StringBuilder();
        vignettes.forEach(v -> writeMe.append(v.toString().trim()).append("\n"));

        FileWriter writer = new FileWriter(TRANSLATION_FILE);
        writer.write(writeMe.toString().trim());
        writer.close();

        System.out.println("Translation file saved!");
    }

    //please build this for the buildTranslationFile to work.
    //when using API call. make sure the prompt that mentions which language to use
    //is taken from config, new variable preferredlanguage. he mentioned something on that
    //so we are able to in theory make any sort of translated file with EN-preferredlanguage.tsv
    private static String translate(String language, String text) throws IOException {
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
