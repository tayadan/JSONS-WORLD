package com.JSONsWorld.main.vignettes;

import java.util.ArrayList;
import java.util.Arrays;

public class VignetteManager {

    private ArrayList<Vignette> panels;

    public VignetteManager(Vignette... frames) {
        panels = new ArrayList<>(Arrays.asList(frames));
    }
}
