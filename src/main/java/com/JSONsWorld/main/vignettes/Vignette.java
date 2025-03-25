package com.JSONsWorld.main.vignettes;

/**
 * Vignette object class for storing each row.
 * Makes it easier to extract stuff etc.
 */
public class Vignette {
    private String leftPose;
    private String combinedText;
    private String leftText;
    private String rightPose;
    private String backgrounds;

    // Set of translated strings???
    // Private String translated leftPose;???
    // leftPose, combinedText, leftText, rightPose, backgrounds
    public Vignette(String... info) {
        for(int i = 0; i < info.length; i++) {
            switch (i) {
                case 0:
                    leftPose = info[i]; break;
                case 1:
                    combinedText = info[i]; break;
                case 2:
                    leftText = info[i]; break;
                case 3:
                    rightPose = info[i]; break;
                case 4:
                    backgrounds = info[i]; break;
            }
        }
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

