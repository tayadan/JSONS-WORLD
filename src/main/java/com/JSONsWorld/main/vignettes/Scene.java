package com.JSONsWorld.main.vignettes;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;

public class Scene {

    private Element sceneElement;

    private ArrayList<VignetteSchema> panels;
    public Scene(Element sceneElement, ArrayList<VignetteSchema> panels) {
        this.sceneElement = sceneElement;
        this.panels = panels;
    }

    protected Scene(Element sceneElement) {
        this.sceneElement = sceneElement;
    }

    public Element getScene() {
        return sceneElement;
    }

    public ArrayList<VignetteSchema> getPanels() {
        return panels;
    }
}
