package com.JSONsWorld.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TranslationProcessor {

    //idk how to access this file without the absolute path
    //pls change it later
    //pose_pairings.tsv but only first 11 lines to save "credits", since the main tsv is big.
    private static final String TSV_FILE = "C:\\Users\\tayad\\IdeaProjects\\JSONS-WORLD\\src\\main\\java\\resources\\pose_pairings_test.tsv";
    private static final String TRANSLATION_FILE = "english-spanish.tsv"; //idk which language yet

    public static void main(String[] args) {
        List<Vignette> extracted = extractFromTSV();

        //print rows of data as test
        for (Vignette vignette : extracted) {
            System.out.println(vignette);
        }
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
                Vignette vignette = new Vignette(
                        columns.length > 0 ? columns[0] : "", //left pose
                        columns.length > 1 ? columns[1] : "", //combined text
                        columns.length > 2 ? columns[2] : "", //left text
                        columns.length > 3 ? columns[3] : "", //right pose
                        columns.length > 4 ? columns[4] : ""  //backgrounds
                );
                //add vignette to list
                data.add(vignette);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data; //return the list of objects
    }

    //we need translation part which gets translations for each of these and then assigns them in the object??
    //and then placing this data in a separate file with both english and "spanish" translations



}
