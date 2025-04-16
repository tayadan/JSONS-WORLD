package com.JSONsWorld.main;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;

//class made cuz it was needed.
//i made it so it takes in an xml file (for last sprint we will need this to process whole xml file w all dialogue"
//audio is generated and placed inside the audio tags and updates the xml
public class AudioIndex {
    private final String model;
    private final String voice;

    private final Path englishDir;
    private final Path targetDir;

    public AudioIndex(String model, String voice, String englishPath, String targetPath) {
        this.model = model;
        this.voice = voice;

        this.englishDir = Paths.get(englishPath);
        this.targetDir = Paths.get(targetPath);

        try {
            Files.createDirectories(englishDir);
            Files.createDirectories(targetDir);
        } catch (IOException e) {
            throw new RuntimeException("Error creating directories for audio", e);
        }
    }

    //processes xml file, generates audio and tags the audio inside the xml file
    public void processXml(String fileName) {
        try {
            //parse file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(fileName));

            NodeList panels = document.getElementsByTagName("panel");

            // iterate over panels to get original text and translated text
            for (int i = 0; i < panels.getLength(); i++) {
                Element panel = (Element) panels.item(i);

                NodeList balloons = panel.getElementsByTagName("balloon");
                for (int j = 0; j < balloons.getLength(); j++) {
                    Element balloon = (Element) balloons.item(j);
                    String originalText = balloon.getElementsByTagName("text").item(0).getTextContent();
                    String translationText = balloon.getElementsByTagName("translation").item(0).getTextContent();

                    //tag panel w audioinfo and generate mp3 for original and translated texts
                    // generate mp3 for the original text (English)
                    String originalFileName = generateAudio(originalText, englishDir, i, "original");
                    balloon.setAttribute("audio", originalFileName);

                    // generate mp3 for the translated text
                    String translationFileName = generateAudio(translationText, targetDir, i, "translated");
                    balloon.setAttribute("audio_translation", translationFileName);
                }
            }

            // save updated xml with the audio stuff
            updateXML(document, fileName);

        } catch (Exception e) {
            System.err.println("Error processing XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //generate mp3 if it doesn't exist already
    private String generateAudio(String text, Path dir, int panelIndex, String type) {
        try {
            //checks if name of mp3 file exists or not
            String safeName = String.format("panel_%d_%s.mp3", panelIndex, type);
            Path filePath = dir.resolve(safeName);

            if (Files.exists(filePath)) {
                System.out.println("Audio already exists: " + filePath.getFileName());
                return safeName;
            }

            System.out.println("Generating audio for: " + text); //debugging line

            //request for tts
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.openai.com/v1/audio/speech").openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + TranslationProcessor.config.getProperty("api.key"));
            conn.setRequestProperty("Content-Type", "application/json");

            String payload = String.format(
                    "{\"model\":\"%s\",\"input\":\"%s\",\"voice\":\"%s\"}",
                    model,
                    text.replace("\"", "\\\""),
                    voice
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }

            if (conn.getResponseCode() != 200) {
                throw new IOException("TTS failed: " + conn.getResponseCode());
            }

            try (InputStream in = conn.getInputStream();
                 OutputStream out = Files.newOutputStream(filePath)) {
                byte[] buffer = new byte[4096];
                int n;
                while ((n = in.read(buffer)) > 0) {
                    out.write(buffer, 0, n);
                }
            }

            System.out.println("Saved: " + filePath.getFileName());
            return safeName;

        } catch (IOException e) {
            System.err.println("Error generating audio for: " + text);
            e.printStackTrace();
            return "";
        }
    }

    //copy of the write in VignetteManager
    private void updateXML(Document document, String fileName) {
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);

            System.out.println("XML file created.");
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
