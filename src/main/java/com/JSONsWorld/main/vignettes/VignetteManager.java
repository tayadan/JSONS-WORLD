package com.JSONsWorld.main.vignettes;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class VignetteManager {

    private ArrayList<VignetteSchema> panels = new ArrayList<>();;

    public VignetteManager(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        parseXML(xmlFile);
    }

    private void parseXML(File xmlFile) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder factory = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document xml = factory.parse(xmlFile);

        NodeList scenes = xml.getElementsByTagName("scenes").item(0).getChildNodes();
        for(int i = 0; i < scenes.getLength(); i++) {
            Node scene = scenes.item(i);
        }
    }

    public void addVignette(VignetteSchema vignette) { panels.add(vignette); }

    public ArrayList<VignetteSchema> getVignettes() { return panels; }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        new VignetteManager(new File("C:\\Users\\sebas\\Downloads\\lesson 3 specification.xml"));
    }
}
