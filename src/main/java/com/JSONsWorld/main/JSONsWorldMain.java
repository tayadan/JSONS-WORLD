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


        // ---------------------BASE CODE FOR SCHEDULE----------------------
        //conjugation needs to be implemented (preferable w different panels and dialogue into formatted function
        //left and whole text not fully implemented
        //we need function that goes in tsv file and randomly selects a line that has a left text in it and use it and translate word
        //and for combined aswell
        //these will be passed in function and panel should be generated (doesnt generate well yet, no translation implementation and no balloon for dialogue yet


        //String[] schedule = {"conjugation", "left", "whole", "story", "left", "whole", "conjugation", "left", "conjugation", "whole", "story"};
        String[] schedule = TranslationProcessor.config.getProperty("schedule").trim().split(",\\h*");
        VignetteManager manager = new VignetteManager();

        for (String type : schedule) {
            switch (type.trim().toLowerCase()) {
                case "conjugation":
                    manager.addFormattedPanels(panelsForConjugation, dialoguefromconjugationprompt);
                    break;
                case "left":
                    manager.appendPanelFromTsv(TSVLEFTSELECTEDLINE, false); //tsv line will be taken from file and one w left text in it
                    break;
                case "whole":
                    manager.appendPanelFromTsv(TSVWHOLESELECTEDLINE, true); //tsv line taken random from file with combined text in it
                    break;
                case "story":
                    String panelDescriptions = generatePanelDescriptions(backgrounds, characters, poses);
                    String dialogue = generateDialogue(panelDescriptions, TranslationProcessor.config.getProperty("language"));
                    manager.addFormattedPanels(panelDescriptions, dialogue);
                    break;
                default:
                    System.out.println("Unknown lesson type: " + type);
                    break;
            }

            manager.write("outputfile");
            //audio index to be generated


    }
}
