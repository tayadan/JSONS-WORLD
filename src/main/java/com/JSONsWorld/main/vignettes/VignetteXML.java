package com.JSONsWorld.main.vignettes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Contains the scene info for 1 vignette
 */
class VignetteXML {

    private final DocumentBuilder builder;
    private Document document;

    // Combined isn't mentioned in the example xml. I'll just assume it's right
    public VignetteXML(String image, String leftPose, String leftText, String rightPose, String rightText) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        this.document = builder.newDocument();
        Element sceneRoot = document.createElement("scene");
        Element left = document.createElement("left");
        left.setAttribute("pose", leftPose);
        left.setAttribute("text", leftText);

        Element right = document.createElement("right");
        right.setAttribute("pose", rightPose);
        right.setAttribute("text", rightText);

        sceneRoot.appendChild(left);
        sceneRoot.appendChild(right);
    }

    /**
     * Creates the initial xml
     */
    private void initXML(){
        Document document = builder.newDocument();
        Element comic = document.createElement("comic");
        document.appendChild(comic);


        Element scenes = document.createElement("scenes");
        comic.appendChild(scenes);

    }

    private void writeFile(Document document, String fileName) {
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

    private Element createPanel(Document document, VignetteSchema vignette){
        Element panel = document.createElement("panel");

        //setting
        Element setting = document.createElement("setting");
        setting.appendChild(document.createTextNode(chooseRandom(vignette.getBackgrounds())));
        panel.appendChild(setting);

        //left character
        Element left = document.createElement("left");
        Element leftFigure = createFirstCharacter(document, vignette.getLeftPose());
        left.appendChild(leftFigure);


        //left text speech if we dont have right character and pose
        if (vignette.getRightPose().isEmpty()) {
            Element balloon = document.createElement("balloon");
            balloon.setAttribute("status", "speech");
            Element content = document.createElement("content");
            content.appendChild(document.createTextNode(chooseRandom(vignette.getLeftText())));
            balloon.appendChild(content);
            left.appendChild(balloon);
        }

        //combined text if we have both left and right poses
        if (!vignette.getCombinedText().isEmpty()) {
            Element balloon = document.createElement("balloon");
            balloon.setAttribute("status", "speech");
            Element content = document.createElement("content");
            content.appendChild(document.createTextNode(chooseRandom(vignette.getCombinedText())));
            balloon.appendChild(content);
            left.appendChild(balloon);
        }
        panel.appendChild(left);

        //right character if exists
        if (!vignette.getRightPose().isEmpty()) {
            Element right = document.createElement("right");
            Element rightFigure = createSecondChild(document, (chooseRandom(vignette.getRightPose())));
            right.appendChild(rightFigure);
            panel.appendChild(right);
        }

        return panel;
    }

    private String chooseRandom(String data) {
        if (data == null || data.trim().isEmpty()) {
            return "";
        }
        String[] options = data.split(",");
        int randomIndex = (int) (Math.random() * options.length);
        return options[randomIndex].trim();
    }

    // Not sure this is necessary for this sprint...?
    private Element createFirstCharacter(Document document, String leftPose){
        Element figure = document.createElement("figure");

        Element id = document.createElement("id");
        id.appendChild(document.createTextNode("Jim"));
        figure.appendChild(id);

        Element name = document.createElement("name");
        name.appendChild(document.createTextNode("Jim"));
        figure.appendChild(name);

        Element appearance = document.createElement("appearance");
        appearance.appendChild(document.createTextNode("male"));
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
        pose.appendChild(document.createTextNode(leftPose));
        figure.appendChild(pose);

        Element facing = document.createElement("facing");
        facing.appendChild(document.createTextNode("right"));
        figure.appendChild(facing);

        return figure;
    }

    private Element createSecondChild(Document document, String rightPose){
        Element figure = document.createElement("figure");


        Element id = document.createElement("id");
        id.appendChild(document.createTextNode("Ana"));
        figure.appendChild(id);

        Element name = document.createElement("name");
        name.appendChild(document.createTextNode("Ana"));
        figure.appendChild(name);

        Element appearance = document.createElement("appearance");
        appearance.appendChild(document.createTextNode("female"));
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
        pose.appendChild(document.createTextNode(rightPose));
        figure.appendChild(pose);

        Element facing = document.createElement("facing");
        facing.appendChild(document.createTextNode("left"));
        figure.appendChild(facing);

        return figure;
    }
}
