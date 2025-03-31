package com.JSONsWorld.main.vignettes;

import java.util.ArrayList;
import java.util.Arrays;

public class VignetteManager {

    private ArrayList<Vignette> panels;

    public VignetteManager(Vignette... frames) {
        panels = new ArrayList<>(Arrays.asList(frames));
    }

    public void addVignette(Vignette vignette) { panels.add(vignette); }

    public ArrayList<Vignette> getVignettes() { return panels; }
}
