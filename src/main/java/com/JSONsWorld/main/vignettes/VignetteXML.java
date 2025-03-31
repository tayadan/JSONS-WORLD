package com.JSONsWorld.main.vignettes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class VignetteXML {

    private final DocumentBuilder builder;

    public VignetteXML(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }


    }
    public void createXML(String fileName, VignetteManager manager){
        Document document = builder.newDocument();
        Element comic = document.createElement("comic");
        document.appendChild(comic);

        Element figures = document.createElement("figures");
        comic.appendChild(figures);
        figures.appendChild(createFirstCharacter(document));
        figures.appendChild(createSecondChild(document));

        Element scenes = document.createElement("scenes");
        comic.appendChild(scenes);

        for (Vignette vignette : manager.getVignettes()) {
            scenes.appendChild(createScene(document, vignette));
        }

        writeFile(document, fileName);

    }

    private void writeFile(Document document, String fileName){
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

            System.out.println("XML file created.");
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private Element createScene(Document document, Vignette vignette){

        Element scene = document.createElement("scene");

        Element leftpose = document.createElement("leftpose");
        leftpose.appendChild(document.createTextNode(vignette.getLeftPose()));
        scene.appendChild(leftpose);

        Element rightpose = document.createElement("rightpose");
        rightpose.appendChild(document.createTextNode(chooseRandom(vignette.getRightPose())));
        scene.appendChild(rightpose);

        if (!vignette.getLeftText().isEmpty()) {
            Element leftText = document.createElement("left_text");
            leftText.appendChild(document.createTextNode(chooseRandom(vignette.getLeftText())));
            scene.appendChild(leftText);
        }

        if (!vignette.getCombinedText().isEmpty()) {
            Element combinedText = document.createElement("combined_text");
            combinedText.appendChild(document.createTextNode(chooseRandom(vignette.getCombinedText())));
            scene.appendChild(combinedText);
        }

        Element background = document.createElement("background");
        background.appendChild(document.createTextNode(chooseRandom(vignette.getBackgrounds())));
        scene.appendChild(background);

        return scene;
    }

    private String chooseRandom(String data) {
        if (data == null || data.trim().isEmpty()) {
            return "";
        }
        String[] options = data.split(",");
        int randomIndex = (int) (Math.random() * options.length);
        return options[randomIndex].trim();
    }

    private Element createFirstCharacter(Document document){
        Element figure = document.createElement("figure");

        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(""));
        figure.appendChild(name);

        Element appearance = document.createElement("appearance");
        appearance.appendChild(document.createTextNode(""));
        figure.appendChild(appearance);

        Element skin = document.createElement("skin");
        skin.appendChild(document.createTextNode(""));
        figure.appendChild(skin);

        Element hair = document.createElement("hair");
        hair.appendChild(document.createTextNode(""));
        figure.appendChild(hair);

        Element beard = document.createElement("beard");
        beard.appendChild(document.createTextNode(""));
        figure.appendChild(beard);

        Element hairlength = document.createElement("hairlength");
        hairlength.appendChild(document.createTextNode(""));
        figure.appendChild(hairlength);

        Element hairstyle = document.createElement("hairstyle");
        hairstyle.appendChild(document.createTextNode(""));
        figure.appendChild(hairstyle);

        Element lips = document.createElement("lips");
        lips.appendChild(document.createTextNode(""));
        figure.appendChild(lips);

        Element pose = document.createElement("pose");
        pose.appendChild(document.createTextNode(""));
        figure.appendChild(pose);

        Element facing = document.createElement("facing");
        facing.appendChild(document.createTextNode(""));
        figure.appendChild(facing);

        return figure;
    }

    private Element createSecondChild(Document document){
        Element figure = document.createElement("figure");

        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(""));
        figure.appendChild(name);

        Element appearance = document.createElement("appearance");
        appearance.appendChild(document.createTextNode(""));
        figure.appendChild(appearance);

        Element skin = document.createElement("skin");
        skin.appendChild(document.createTextNode(""));
        figure.appendChild(skin);

        Element hair = document.createElement("hair");
        hair.appendChild(document.createTextNode(""));
        figure.appendChild(hair);

        Element beard = document.createElement("beard");
        beard.appendChild(document.createTextNode(""));
        figure.appendChild(beard);

        Element hairlength = document.createElement("hairlength");
        hairlength.appendChild(document.createTextNode(""));
        figure.appendChild(hairlength);

        Element hairstyle = document.createElement("hairstyle");
        hairstyle.appendChild(document.createTextNode(""));
        figure.appendChild(hairstyle);

        Element lips = document.createElement("lips");
        lips.appendChild(document.createTextNode(""));
        figure.appendChild(lips);

        Element pose = document.createElement("pose");
        pose.appendChild(document.createTextNode(""));
        figure.appendChild(pose);

        Element facing = document.createElement("facing");
        facing.appendChild(document.createTextNode(""));
        figure.appendChild(facing);

        return figure;
    }
}
