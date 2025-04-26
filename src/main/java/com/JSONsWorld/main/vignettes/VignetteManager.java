package com.JSONsWorld.main.vignettes;

import com.JSONsWorld.main.story.TranslationProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

    private ArrayList<VignetteSchema> panels = new ArrayList<>();
    protected Document document;

    public VignetteManager(File source) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Parse the XML file
        this.document = builder.parse(source);

        NodeList xmlPanels = document.getElementsByTagName("panel");

        for(int i = 0; i < xmlPanels.getLength(); i++) {
            this.panels.add(new VignetteSchema(xmlPanels.item(i)));
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
        this.panels = new ArrayList<>();
    }
    public void addFormattedPanels(String format, String dialogue) {
        NodeList scenesList = document.getElementsByTagName("scene");
        if (scenesList.getLength() == 0) throw new RuntimeException("No <scene> element found.");
        Element scene = (Element) scenesList.item(0);

        int panelIndex = 0;
        for (String line : format.split("\n")) {
            Element panelElement = document.createElement("panel");
            scene.appendChild(panelElement);
            panels.add(new VignetteSchema(line.trim(), dialogue.split("-")[panelIndex], this.document, panelElement));
            panelIndex++;
        }
    }

    public VignetteManager(String format, String dialogue) throws ParserConfigurationException {
        int panel = 0;
        this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = document.createElement("comic");

        document.appendChild(root);
        root.appendChild(document.createElement("figures"));
        Element scenes = document.createElement("scenes");
        scenes.appendChild(document.createElement("scene"));

        for(String line : format.split("\n")) {
            Element panelElement = document.createElement("panel");
            scenes.getChildNodes().item(0).appendChild(panelElement);
            panels.add(new VignetteSchema(line.trim(), dialogue.split("-")[panel], this.document, panelElement));
            panel++;
        }

        root.appendChild(scenes);
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
        Element scene = (Element) scenesList.item(0);

        Element panelElement = document.createElement("panel");
        scene.appendChild(panelElement);

        String[] characters = TranslationProcessor.config.getProperty("characters").trim().split("\\s*,\\s*");

        String format = "background-" + chosenBackground + ", left-" + characters[0] + ":-, right-" + characters[1] + ":-, pose-" + leftPose + ":" + rightPose;


        panels.add(new VignetteSchema(format, dialogue, this.document, panelElement));
    }



    public void translateVignettes(String language) throws IOException {
        ArrayList<String[]> textToTranslate = new ArrayList<>();

        panels.forEach(panel -> textToTranslate.add(panel.getOriginalText()));
        textToTranslate.removeIf(text -> text.length == 0);

        String[] formattedPanels = new String[textToTranslate.size()];
        for (int i = 0; i < textToTranslate.size(); i++) {
            formattedPanels[i] = String.join("\n", textToTranslate.get(i));
        }

        // Sends the text to translate
        String toTranslate = String.join("\n-\n", formattedPanels);
        String result = TranslationProcessor.translate(language, toTranslate);

        // Gets the result and formats it properly
        result = result.replaceAll("\\\\n", "\n").replaceAll("-", "");
        ArrayList<String> results = new ArrayList<>(Arrays.asList(result.split("\n")));
        results.removeIf(r -> r.trim().isEmpty());
        Queue<String> resultsQueue = new LinkedList<>();
        results.forEach(r -> resultsQueue.add(r.trim()));

        //
        panels.forEach(panel -> {
            String[] setTranslations = new String[panel.getExtractedCount()];
            for(int i = 0; i < panel.getExtractedCount(); i++) {
                setTranslations[i] = resultsQueue.remove();
            }
            panel.setTranslations(setTranslations);
        });
    }

    public void write(String fileName) {
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