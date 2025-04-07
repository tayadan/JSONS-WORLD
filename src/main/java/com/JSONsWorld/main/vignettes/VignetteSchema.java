package com.JSONsWorld.main.vignettes;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Contains the scene info for 1 vignette
 */
class VignetteSchema {
    private Node scene;

    private ArrayList<TextAndTranslation> extractedText = new ArrayList<>();

    protected VignetteSchema(Node scene) {
        this.scene = scene;

        NodeList list = scene.getChildNodes();

        Node childNode = null;
        for(int i = 0; i < list.getLength(); i++) {
            childNode = list.item(i);

            if(childNode.getNodeName().equalsIgnoreCase("left") ||
                    childNode.getNodeName().equalsIgnoreCase("middle") ||
                    childNode.getNodeName().equalsIgnoreCase("right"))
            {
                NodeList grandChildNodes = childNode.getChildNodes();

                for(int j = 0; j < grandChildNodes.getLength(); j++) {
                    if(grandChildNodes.item(j).getNodeName().equalsIgnoreCase("balloon")) {
                        extractedText.add(new TextAndTranslation(grandChildNodes.item(j).getFirstChild().getNextSibling().getTextContent()));
                    }
                }
            }

        }
    }

    protected boolean hasTranslation() {
        if(extractedText.isEmpty()) return false;
        return !extractedText.getFirst().getOriginal().trim().isEmpty();

    }

    protected String[] getOriginalText() {
        String[] text = new String[extractedText.size()];
        for(int i = 0; i < extractedText.size(); i++) {
            text[i] = extractedText.get(i).original;
        }
        return text;
    }

    protected String[] getTranslatedText() {
        String[] text = new String[extractedText.size()];
        for(int i = 0; i < extractedText.size(); i++) {
            text[i] = extractedText.get(i).translation;
        }
        return text;
    }

    protected int getExtractedCount() {
        return extractedText.size();
    }

    protected void setTranslations(String... translations) {
        Queue<String> translatedQueue = new LinkedList<>();
        for(int i = 0; i < extractedText.size(); i++) {
            extractedText.get(i).setTranslation(translations[i]);
            translatedQueue.add(translations[i]);
        }


        NodeList list = scene.getChildNodes();

        Node childNode = null;
        for(int i = 0; i < list.getLength(); i++) {
            childNode = list.item(i);

            if(childNode.getNodeName().equalsIgnoreCase("left") ||
                    childNode.getNodeName().equalsIgnoreCase("middle") ||
                    childNode.getNodeName().equalsIgnoreCase("right"))
            {
                NodeList grandChildNodes = childNode.getChildNodes();

                for(int j = 0; j < grandChildNodes.getLength(); j++) {
                    if(grandChildNodes.item(j).getNodeName().equalsIgnoreCase("balloon")) {
                        Node t = scene.getOwnerDocument().createElement("translation");
                        t.appendChild(scene.getOwnerDocument().createTextNode(translatedQueue.remove()));
                        grandChildNodes.item(j).appendChild(t);
                    }
                }
            }

        }
    }

    private class TextAndTranslation {
        private String original = "";
        private String translation = "";

        private TextAndTranslation(String original) {
            this.original = original;
        }

        private String getOriginal() {
            return this.original;
        }

        private void setTranslation(String translation) {
            this.translation = translation;
        }

        private String getTranslation() {
            return translation;
        }
    }
}
