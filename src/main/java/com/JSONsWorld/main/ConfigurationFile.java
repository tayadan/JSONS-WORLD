package com.JSONsWorld.main;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationFile {
    private Properties properties;

    public ConfigurationFile(String fileName) {
        this.properties = new Properties();
        loadProperties(fileName);

        if(!properties.containsKey("api.key")) {
            if(System.getenv().containsKey("OpenAI_API_Key")) properties.put("api.key", System.getenv("OpenAI_API_Key"));
            else {
                throw new RuntimeException("Specify the API key in the properties file with the key \"api.key\" " +
                        "or as an environment variable with the name \"OpenAI_API_Key.\"");
            }
        }
        if(!properties.containsKey("llm.model")) properties.put("llm.model", "gpt-4o-mini");
        if(!properties.containsKey("prompt"))
            properties.put("prompt", "Please explain why the person running this program should specify a prompt to send to you and not just use the default one (this).");
    }

    /*
    Current properties:
    1. api key
    2. prompt
    3. llm.model

    Future properties:
    1. language
    */

    private void loadProperties(String fileName){
        if(!new File(fileName).exists()) return; // There are some default values, so it's not necessary to specify a config file
        try (FileReader reader = new FileReader(fileName)) {
            properties.load(reader);
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + fileName);
            e.printStackTrace();
        }

        // To make the capitalisation consistent
        for(String property : properties.stringPropertyNames()) {
            String originalValue = properties.getProperty(property);
            properties.remove(property);
            properties.put(property.toLowerCase(), originalValue);
        }
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }




}
