package com.JSONsWorld.main;

import com.JSONsWorld.main.story.AudioIndex;
import com.JSONsWorld.main.story.TranslationProcessor;
import com.JSONsWorld.main.vignettes.VignetteManager;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static com.JSONsWorld.main.story.StoryManager.generateDialogue;
import static com.JSONsWorld.main.story.StoryManager.generatePanelDescriptions;

public class JSONsWorldMain {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        TranslationProcessor.config = new ConfigurationFile(args.length == 0 ? "config.properties" : args[0]);

        //sample data
        String[] backgrounds = TranslationProcessor.config.getProperty("backgrounds").trim().split(",\\h*");
        String[] characters = TranslationProcessor.config.getProperty("characters").trim().split(",\\h*");
         // "walking", "crawling", "sitting", "eating", "standing", "serving", "laughing", "talking", "running", "studying", "reading", "writing", "happy", "angry"
        String[] poses = TranslationProcessor.config.getProperty("poses").trim().split(",\\h*");

        // Checks if the file exists then creates it if it doesn't
        if(!new File("output/Output.xml").exists()) {
            //panel descriptions
            String panelDescriptions = generatePanelDescriptions(backgrounds, characters, poses);
            //dialogue
            String dialogue = generateDialogue(panelDescriptions, TranslationProcessor.config.getProperty("language"));

            VignetteManager manager = new VignetteManager();
            manager.addFormattedPanels(panelDescriptions, dialogue);

            String tsvLine = "attracted\tto fall in love, love\t\t nude, posing\tbedroom, red carpet event, locker room\t1039";
            manager.appendPanelFromTsv(tsvLine, true);  // uses "combined" text

            String anotherTsvLine = "catering\tto serve, a tray, a cocktail\t a tray\teating, drinking, sipping, slurping\trestaurant, food truck\t";
            manager.appendPanelFromTsv(anotherTsvLine, false);  // uses "left" text

            new File("output").mkdir();
            manager.write("output/Output.xml");

            AudioIndex audioIndex = new AudioIndex(
                    "tts-1",
                    "alloy",
                    "output/audio/english",
                    "output/audio/" + TranslationProcessor.config.getProperty("language")
            );
            audioIndex.processXml("output/Output.xml"); //AUDIO IS GENERATED FOR XML FILE AND IS UPDATED ACCORDINGLY.
        }
        else {
            AudioIndex audioIndex = new AudioIndex(
                    "tts-1",
                    "alloy",
                    "output/audio/english",
                    "output/audio/" + TranslationProcessor.config.getProperty("language")
            );
            audioIndex.processXml("output/New.xml"); //AUDIO IS GENERATED FOR XML FILE AND IS UPDATED ACCORDINGLY.
        }
    }
}
