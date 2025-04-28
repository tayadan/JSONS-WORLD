package com.JSONsWorld.main;

import com.JSONsWorld.main.story.AudioIndex;
import com.JSONsWorld.main.story.TranslationProcessor;
import com.JSONsWorld.main.vignettes.VignetteManager;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static com.JSONsWorld.main.story.StoryManager.*;

public class JSONsWorldMain {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        TranslationProcessor.config = new ConfigurationFile(args.length == 0 ? "config.properties" : args[0]);


        // Checks if the file exists then creates it if it doesn't
        if(!new File("output/Output.xml").exists()) {
            generateOutput();
        }
        else {
            AudioIndex audioIndex = new AudioIndex(
                    "tts-1",
                    "alloy",
                    "output/audio/english",
                    "output/audio/" + TranslationProcessor.config.getProperty("language")
            );
            audioIndex.processXml("output/Output.xml"); //AUDIO IS GENERATED FOR XML FILE AND IS UPDATED ACCORDINGLY.
        }


        // ---------------------BASE CODE FOR SCHEDULE----------------------
        //conjugation needs to be implemented (preferable w different panels and dialogue into formatted function
        //left and whole text not fully implemented
        //we need function that goes in tsv file and randomly selects a line that has a left text in it and use it and translate word
        //and for combined aswell
        //these will be passed in function and panel should be generated (doesn't generate well yet, no translation implementation and no balloon for dialogue yet


        //String[] schedule = {"conjugation", "left", "whole", "story", "left", "whole", "conjugation", "left", "conjugation", "whole", "story"};

    }

    private static void generateOutput() throws IOException, ParserConfigurationException {
        String[] schedule = TranslationProcessor.config.getProperty("schedule").trim().split(",\\h*");
        VignetteManager manager = new VignetteManager();

        // For each of these it loops and runs the prompt code with the specific prompt.
        for(String scheduleItem : schedule) {
            String[] backgrounds = TranslationProcessor.config.getProperty(scheduleItem + ".backgrounds").trim().split(",\\h*");
            String[] characters = TranslationProcessor.config.getProperty(scheduleItem + ".characters").trim().split(",\\h*");
            String[] poses = TranslationProcessor.config.getProperty(scheduleItem + ".poses").trim().split(",\\h*");

            // Gets the name of it the next thing in the schedule, the prompt associated with it, and then runs it before adding it as a scene.
            //panel descriptions
            String prompt = TranslationProcessor.config.getProperty(scheduleItem + ".prompt").replace("{language}", TranslationProcessor.config.getProperty("language"));
            String storyDescription = generateSimplePanelDescriptions(backgrounds, characters, poses, prompt);

            String panelDescriptions = generatePanelDescriptions(backgrounds, characters, poses, storyDescription);
            //dialogue

            String dialogue = generateDialogue(storyDescription, prompt, TranslationProcessor.config.getProperty("language"));


            manager.addNewScene(panelDescriptions, dialogue);
        }

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
}
