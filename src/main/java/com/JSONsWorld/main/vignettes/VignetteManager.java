package com.JSONsWorld.main.vignettes;

import com.JSONsWorld.main.TranslationProcessor;
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
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(this.document);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);

            System.out.println("XML file created.");
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
