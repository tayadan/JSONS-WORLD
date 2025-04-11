package com.JSONsWorld.main;

import com.JSONsWorld.main.StoryManager;
import com.JSONsWorld.main.api.ContextManager;
import com.JSONsWorld.main.api.OutputProcessor;
import com.JSONsWorld.main.vignettes.VignetteManager;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.JSONsWorld.main.StoryManager.generateDialogue;
import static com.JSONsWorld.main.StoryManager.generatePanelDescriptions;

public class JSONsWorldMain {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        TranslationProcessor.config = new ConfigurationFile(args.length == 0 ? "config.properties" : args[0]);

        //VignetteManager manager = new VignetteManager(new File("specification.xml"));
        //manager.translateVignettes(TranslationProcessor.config.getProperty("language"));
        //manager.write("translatedXML.xml");

        //sample data
        ArrayList<String> backgrounds = new ArrayList<>(List.of("restaurant exterior", "restaurant interior", "kitchen", "dining table, school, classroom, hallway, desert, beach"));
        ArrayList<String> poses = new ArrayList<>(List.of("walking", "crawling", "sitting", "eating", "standing", "serving", "laughing", "mirage waitress, running, studying, reading, writing"));
        ArrayList<String> characters = new ArrayList<>(List.of("Anna", "Bob"));

        //panel descriptions
        List<String> panelDescriptions = generatePanelDescriptions(backgrounds, poses, characters);
        //dialogue
        List<String> dialogue = generateDialogue(panelDescriptions, TranslationProcessor.config.getProperty("language"));

        //debug descriptions
        System.out.println("Panel Descriptions:");
        for (String description : panelDescriptions) {
            System.out.println(description);
        }

        //debug dialogue
        System.out.println("\nGenerated Dialogue:");
        for (String line : dialogue) {
            System.out.println(line);
        }
    }
}
