package com.JSONsWorld.main.vignettes;

/**
 * Vignette object class for storing each row.
 * Makes it easier to extract stuff etc.
 */
public class Vignette {
    private String leftPose = "";
    private String combinedText = "";
    private String translated_combinedText = "";
    private String leftText = "";
    private String translated_leftText = "";
    private String rightPose = "";
    private String backgrounds = "";

    // Set of translated strings???
    // Private String translated leftPose;???
    // leftPose, combinedText, leftText, rightPose, backgrounds
    public Vignette(String... info) {
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

        leftPose = padded[0];
        combinedText = padded[1];
        translated_combinedText = padded[2];
        leftText = padded[3];
        translated_leftText = padded[4];
        rightPose = padded[5];
        backgrounds = padded[6];
    }

    public String getLeftPose() { return leftPose; }
    public String getCombinedText() { return combinedText; }
    public String getLeftText() { return leftText; }
    public String getRightPose() { return rightPose; }
    public String getBackgrounds() { return backgrounds; }
    //setters for translated stuff???, could be useful for extracting translated tsv?
    // Not gonna do it now, but we should add the library lombok which can automatically add getters and setters. -Seb


    @Override
    public String toString() {
        // The trim is just to prevent trailing tabs.
        return (leftPose + "\t" + combinedText + "\t" + leftText + "\t" + rightPose + "\t" + backgrounds).trim();
    }
}

