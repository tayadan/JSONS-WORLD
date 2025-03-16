package com.JSONsWorld.main;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationFile {

    private String filename;
    private Properties properties;

    public ConfigurationFile( String filename){
        this.filename = filename;
        this.properties = new Properties();
        loadProperties();

    }

    private void loadProperties(){
        try (FileReader reader = new FileReader(filename)) {
            properties.load(reader);
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + filename);
            e.printStackTrace();
        }
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }




}
