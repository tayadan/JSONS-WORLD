package com.JSONsWorld.main;

import com.JSONsWorld.main.StoryManager;
import com.JSONsWorld.main.api.ContextManager;
import com.JSONsWorld.main.api.OutputProcessor;
import com.JSONsWorld.main.vignettes.VignetteManager;
import com.JSONsWorld.main.vignettes.VignetteSchema;
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
import java.util.Arrays;
import java.util.List;

import static com.JSONsWorld.main.StoryManager.generateDialogue;
import static com.JSONsWorld.main.StoryManager.generatePanelDescriptions;

public class JSONsWorldMain {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        TranslationProcessor.config = new ConfigurationFile(args.length == 0 ? "config.properties" : args[0]);

        //sample data
        String[] backgrounds = new String[]{"restaurant exterior", "restaurant interior", "kitchen", "dining table", "school", "classroom", "hallway", "desert", "beach", "ocean", "mountain", "park", "street"};
        String[] characters = new String[]{"Anna", "Bob"};
        String[] poses = new String[]{"walking", "crawling", "sitting", "eating", "standing", "serving", "laughing", "talking", "running", "studying", "reading", "writing", "happy", "angry"};
        /* ALREADY GENERATED XML. NO NEED TO REGENERATE. WE WILL ADD THIS BACK IN MAIN FOR THE FINAL SPRINT TO SHOW IT CAN BE DONE
        //panel descriptions
        String panelDescriptions = generatePanelDescriptions(backgrounds, characters, poses);
        //dialogue
        String dialogue = generateDialogue(panelDescriptions, TranslationProcessor.config.getProperty("language"));
        VignetteManager manager = new VignetteManager(panelDescriptions, dialogue);
        //manager.write("Output.xml");
         */

        AudioIndex audioIndex = new AudioIndex(
                "tts-1",
                "alloy",
                "audio/english",
                "audio/" + TranslationProcessor.config.getProperty("language")
        );

        audioIndex.processXml("Output.xml"); //AUDIO IS GENERATED FOR XML FILE AND IS UPDATED ACCORDINGLY.
    }
}
