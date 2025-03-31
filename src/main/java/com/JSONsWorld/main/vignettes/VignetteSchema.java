package com.JSONsWorld.main.vignettes;

import org.w3c.dom.Document;

/**
 * Vignette object class for storing each row.
 * Makes it easier to extract stuff etc.
 */
public class VignetteSchema extends VignetteXML {
    private String leftPose = "";
    private String rightPose = "";

    private String combinedText = "";
    private String translated_combinedText = "";

    private String leftText = "";
    private String translated_leftText = "";

    private String backgrounds = "";

    // Set of translated strings???
    // Private String translated leftPose;???
    // leftPose, combinedText, leftText, rightPose, backgrounds
    public VignetteSchema(String... info) {
        //sets all data, if combined or left is empty, the translated fields should be empty too
        String[] padded = new String[7]; //new list to adjust for empty values
        int j = 0;
        for (int i = 0; i < padded.length; i++) {
            //check translated indexes?
            if ((i == 2 && padded[1].isEmpty()) || (i == 4 && padded[3].isEmpty())) {
                padded[i] = "";  //translated set to empty
                continue;
            }
            //missing right pose?
            if (i == 5 && j >= info.length) {
                padded[5] = "";
                padded[6] = (j < info.length + 1) ? info[j] : "";
                break;
            }
            //assign value
            padded[i] = (j < info.length) ? info[j] : "";
            j++;
        }

        String leftPose = padded[0];
        String combinedText = padded[1];
        String translated_combinedText = padded[2];
        String leftText = padded[3];
        String translated_leftText = padded[4];
        String rightPose = padded[5];
        String backgrounds = padded[6];

        this.leftPose = info[0].trim();
        this.combinedText = info[1].trim();
        this.translated_combinedText = info[2].trim();
        this.leftText = info[3].trim();
        this.translated_leftText = info[4].trim();
        this.rightPose = info[5].trim();
        if(info.length == 7) this.backgrounds = info[6].trim();
    }

    public void buildXML(Document document) {
        super.build(backgrounds.split(", ")[(int) (Math.random() * backgrounds.split(", ").length)], leftPose, leftText, rightPose, combinedText, document);
    }

    public String getLeftPose() { return leftPose; }
    public String getRightPose() { return rightPose; }

    public String getCombinedText() { return combinedText; }
    public String getTranslatedCombinedText() { return translated_combinedText; }

    public String getLeftText() { return leftText; }
    public String getTranslatedLeftText() { return translated_leftText; }

    public String getBackgrounds() { return backgrounds; }
    //setters for translated stuff???, could be useful for extracting translated tsv?
    // Not gonna do it now, but we should add the library lombok which can automatically add getters and setters. -Seb


    @Override
    public String toString() {
        // The trim is just to prevent trailing tabs.
        return (leftPose + "\t" + combinedText + "\t" + leftText + "\t" + rightPose + "\t" + backgrounds).trim();
    }
}

