package com.JSONsWorld.main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class TranslationProcessor {

    //idk how to access this file without the absolute path
    //pls change it later
    //pose_pairings.tsv but only first 11 lines to save "credits", since the main tsv is big.
    private static final String TSV_FILE = "pose_pairings_test.tsv";
    private static final String TRANSLATION_FILE = "english-spanish.tsv"; //idk which language yet

    public static void main(String[] args) {
        List<Vignette> extracted = extractFromTSV();
        //print rows of data as test on how it looks like
        for (Vignette vignette : extracted) {
            System.out.println(vignette);
        }
        buildTranslationFile(extracted); //english-spanish.tsv should be built!! in theory
    }


        //extracts tsv file and brings back list of vignette objects
    private static List<Vignette> extractFromTSV() {
        List<Vignette> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TranslationProcessor.TSV_FILE))) {
            String line;
            reader.readLine(); //skip first line

            while ((line = reader.readLine()) != null) { //read file until end
                String[] columns = line.split("\t"); //split by tab

                //create new vignette obj for each row
                Vignette row = new Vignette(
                        columns.length > 0 ? columns[0] : "", //left pose
                        columns.length > 1 ? columns[1] : "", //combined text
                        columns.length > 2 ? columns[2] : "", //left text
                        columns.length > 3 ? columns[3] : "", //right pose
                        columns.length > 4 ? columns[4] : ""  //backgrounds
                );
                //add vignette to list
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data; //return the list of objects
    }

    //we need translation part which gets translations for each of these and then assigns them in the object??
    //and then placing this data in a separate file with both english and "spanish" translations
    private static void buildTranslationFile(List<Vignette> vignettes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSLATION_FILE))) {
            for (Vignette vignette : vignettes) {
                StringBuilder output = new StringBuilder();
                //append text
                output.append(vignette.getLeftPose()).append("\t");
                output.append(vignette.getCombinedText()).append("\t");
                output.append(translate(vignette.getCombinedText())).append("\t"); //translate() not implemented yet
                output.append(vignette.getLeftText()).append("\t");
                output.append(translate(vignette.getLeftText())).append("\t");  //translate() not implemented yet
                output.append(vignette.getRightPose()).append("\t");
                output.append(vignette.getBackgrounds());

                writer.write(output.toString());
                writer.newLine();
            }
            System.out.println("Translation file saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //please build this for the buildTranslationFile to work.
    //when using API call. make sure the prompt that mentions which language to use
    //is taken from config, new variable preferredlanguage. he mentioned something on that
    //so we are able to in theory make any sort of translated file with EN-preferredlanguage.tsv
    private static String translate(String text) {
        //gets the text and sends to API LLM call and translates.
        return null; //returns the translated text
    }


}
