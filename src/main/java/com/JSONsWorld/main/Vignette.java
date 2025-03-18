package com.JSONsWorld.main;

//vignette object class for storing each row
//makes it easier to extract stuff etc.
public class Vignette {
    private String leftPose;
    private String combinedText;
    private String leftText;
    private String rightPose;
    private String backgrounds;

    //set of translated strings???
    //private String translated leftPose;???

    public Vignette(String leftPose, String combinedText, String leftText, String rightPose, String backgrounds) {
        this.leftPose = leftPose;
        this.combinedText = combinedText;
        this.leftText = leftText;
        this.rightPose = rightPose;
        this.backgrounds = backgrounds;
    }

    public String getLeftPose() { return leftPose; }
    public String getCombinedText() { return combinedText; }
    public String getLeftText() { return leftText; }
    public String getRightPose() { return rightPose; }
    public String getBackgrounds() { return backgrounds; }
    //setters for translated stuff???

    @Override
    public String toString() {
        return leftPose + "\t" + combinedText + "\t" + leftText + "\t" + rightPose + "\t" + backgrounds;
    }
}

