package com.JSONsWorld.main;

import com.JSONsWorld.main.vignettes.Vignette;

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

    public static void main(String[] args) throws IOException {
        List<Vignette> extracted = extractFromTSV();
        //print rows of data as test on how it looks like
        for (Vignette vignette : extracted) {
            System.out.println(vignette);
        }
        buildTranslationFile(extracted); //english-spanish.tsv should be built!! in theory
    }


        //extracts tsv file and brings back list of vignette objects
    private static List<Vignette> extractFromTSV() throws IOException {
        List<Vignette> data = new ArrayList<>();

        ArrayList<String> lines = new ArrayList<>
                (List.of(Files.readString(new File(TranslationProcessor.TSV_FILE).toPath()).trim().split("\n")));
        lines.removeFirst();

        // Filters the lines then adds them to the list
        lines.removeIf(line -> line.trim().isEmpty());
        lines.forEach(line -> data.add(new Vignette(line.split("\t"))));

        return data; //return the list of objects
    }

    //we need translation part which gets translations for each of these and then assigns them in the object??
    //and then placing this data in a separate file with both english and "spanish" translations
    private static void buildTranslationFile(List<Vignette> vignettes) throws IOException {
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
    private static String translate(String text) {
        //gets the text and sends to API LLM call and translates.
        return null; //returns the translated text
    }


}
