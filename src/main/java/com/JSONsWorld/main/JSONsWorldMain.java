package com.JSONsWorld.main;

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

public class JSONsWorldMain {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        TranslationProcessor.config = new ConfigurationFile(args[0]);
        VignetteManager manager = new VignetteManager(new File("specification.xml"));

        manager.translateVignettes();

        manager.write("C:\\Users\\sebas\\IdeaProjects\\JSONS-WORLD\\translatedXML.xml");
    }
}
