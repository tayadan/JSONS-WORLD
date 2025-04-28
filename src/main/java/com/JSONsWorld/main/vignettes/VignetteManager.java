package com.JSONsWorld.main.vignettes;

import com.JSONsWorld.main.story.TranslationProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class VignetteManager {
    protected Document document;
    private ArrayList<Scene> scenes = new ArrayList<>();

    public VignetteManager(File source) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Parse the XML file
        this.document = builder.parse(source);

        NodeList xmlPanels = document.getElementsByTagName("scene");


        for(int i = 0; i < xmlPanels.getLength(); i++) {
            this.scenes.add(new Scene((Element) xmlPanels.item(i)));
        }
    }

    public VignetteManager() throws ParserConfigurationException {
        this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = document.createElement("comic");
        document.appendChild(root);
        root.appendChild(document.createElement("figures"));

        Element scenes = document.createElement("scenes");
        scenes.appendChild(document.createElement("scene"));
        root.appendChild(scenes);

        this.scenes = new ArrayList<>();
    }

    public VignetteManager(String format, String dialogue) throws ParserConfigurationException {
        int panel = 0;
        this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = document.createElement("comic");

        document.appendChild(root);
        root.appendChild(document.createElement("figures"));
        Element scenes = document.createElement("scenes");
        root.appendChild(scenes);

        Element scene = document.createElement("scene");

        ArrayList<VignetteSchema> panels = new ArrayList<>();
        for(String line : format.split("\n")) {
            Element panelElement = document.createElement("panel");
            scene.appendChild(panelElement);
            panels.add(new VignetteSchema(line.trim(), dialogue.split("-")[panel], this.document, panelElement));
            panel++;
        }

        this.scenes.add(new Scene(scene, panels));
    }

    public void addNewScene(String format, String dialogue) {
        Element scene = document.createElement("scene");

        ArrayList<VignetteSchema> panels = new ArrayList<>();
        int panelIndex = 0;
        for (String line : format.split("\n")) {
            Element panelElement = document.createElement("panel");
            scene.appendChild(panelElement);
            panels.add(new VignetteSchema(line.trim(), dialogue.split("-")[panelIndex], this.document, panelElement));
            panelIndex++;
        }

        this.scenes.add(new Scene(scene, panels));
    }

    public void appendPanelFromTsv(String tsvLine, boolean useCombined) {
        String[] fields = tsvLine.split("\t");

        if (fields.length < 6) {
            System.out.println("TSV line skipped: not enough fields.");
            return;
        }

        String leftPose = fields[0].trim();
        String combinedText = fields[1].trim();
        String leftText = fields[2].trim();
        String rightPose = fields[3].trim();
        String backgrounds = fields[4].trim();

        //decide text to use
        String dialogue;
        if (useCombined) {
            if (combinedText.isEmpty()) {
                System.out.println("TSV line skipped: no combined text.");
                return;
            }
            dialogue = combinedText;
        } else {
            if (leftText.isEmpty()) {
                System.out.println("TSV line skipped: no left text.");
                return;
            }
            dialogue = leftText;
        }

        //random background chosen
        String[] backgroundOptions = backgrounds.split(",");
        String chosenBackground = backgroundOptions[(int)(Math.random() * backgroundOptions.length)].trim();

        NodeList scenesList = document.getElementsByTagName("scene");
        if (scenesList.getLength() == 0) throw new RuntimeException("No <scene> element found.");
        Element scene = document.createElement("scene");

        Element panelElement = document.createElement("panel");
        scene.appendChild(panelElement);

        String[] characters = TranslationProcessor.config.getProperty("characters").trim().split("\\s*,\\s*");

        String format = "background-" + chosenBackground + ", left-" + characters[0] + ":-, right-" + characters[1] + ":-, pose-" + leftPose + ":" + rightPose;

        ArrayList<VignetteSchema> panels = new ArrayList<>();
        panels.add(new VignetteSchema(format, dialogue, this.document, panelElement));

        scenes.add(new Scene(scene, panels));
    }

    public void write(String fileName) {
        Node scenesElement = document.getElementsByTagName("scenes").item(0);
        for(Scene scene : scenes) {
            scenesElement.appendChild(scene.getScene());
        }

        try{
            // Creates the file in the clunkiest way imaginable
            FileWriter writer = new FileWriter(fileName);
            writer.write("");
            writer.close();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(this.document);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);

            System.out.println("XML file created.");
        } catch (TransformerException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}