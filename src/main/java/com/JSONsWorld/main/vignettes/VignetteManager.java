package com.JSONsWorld.main.vignettes;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VignetteManager {

    private ArrayList<VignetteSchema> panels = new ArrayList<>();
    protected Document managerDocument;

    public VignetteManager(List<VignetteSchema> panels) throws ParserConfigurationException, IOException, SAXException {
        this.managerDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        this.panels.addAll(panels);
        panels.forEach(vignette -> vignette.buildXML(managerDocument));
    }

    public void write(String fileName) {
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(combine());
            StreamResult result = new StreamResult(new File(fileName));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

            System.out.println("XML file created.");
        } catch (TransformerException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private Document combine() throws ParserConfigurationException {

        Element scenes = managerDocument.createElement("scenes");
        managerDocument.appendChild(scenes);
        int i = 0;
        for(VignetteSchema vignette : panels) {
            System.out.println(i);
            scenes.appendChild(vignette.getElement());
            i++;
        }

        return managerDocument;
    }

    public void addVignette(VignetteSchema vignette) { panels.add(vignette); }

    public ArrayList<VignetteSchema> getVignettes() { return panels; }
}
