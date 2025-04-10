package com.JSONsWorld.main;

import com.JSONsWorld.main.api.ContextManager;
import com.JSONsWorld.main.api.OutputProcessor;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class StoryManager {



    private static String sendPrompt(String prompt) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + TranslationProcessor.config.getProperty("api.key"));

        StringEntity entity = new StringEntity("{"
                + "\"model\": \"" + TranslationProcessor.config.getProperty("llm.model") + "\","
                + prompt + "}");
        post.setEntity(entity);

        return OutputProcessor.processResponse(EntityUtils.toString(httpClient.execute(post).getEntity()));
    }

    public static List<String> generatePanelDescriptions(List<String> backgrounds, List<String> poses, List<String> characters) throws IOException {

        String descriptionPrompt = buildDescriptionPrompt(backgrounds, poses, characters);
        //send prompt to LLM
        String response = sendPrompt(descriptionPrompt);

        return getStrings(response);
    }

    private static List<String> getStrings(String response) {
        List<String> parsed = new ArrayList<>();
        String[] lines = response.split("\n");

        for (String panel : lines) {
            if (!panel.trim().isEmpty()) {
                parsed.add(panel.trim());
            }
        }

        return parsed;
    }

    public static List<String> generateDialogue(List<String> panelDescriptions, String targetLanguage) throws IOException {

        String dialoguePrompt = buildDialoguePrompt(panelDescriptions, targetLanguage);
        // send to LLM
        String response = sendPrompt(dialoguePrompt);

        return getStrings(response);
    }

    //we could change background list to be a map? for categories like
    //restaurant: restaurant interior, exterior, etc
    //desert: gobi desert, etc
    public static String buildDescriptionPrompt(List<String> backgrounds, List<String> poses, List<String> characters) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are generating a 6-panel comic story for a language learner.\n\n");

        prompt.append("* Backgrounds available to choose from: \n");
        prompt.append(backgrounds.toString()).append("\n\n");

        prompt.append("* Characters available to choose from: \n");
        prompt.append(characters.toString()).append("\n\n");

        prompt.append("* Poses available to choose from: \n");
        prompt.append(poses.toString()).append("\n\n");

        prompt.append("Please generate a short visual description for each panel. Each description should be exactly one line.\n");
        prompt.append("For each scene description, include 1 or 2 chosen character/s that reoccurs throughout the story.\n");
        prompt.append("For each scene description, use the poses available to describe what the character/s is/are doing in the scene.\n");
        prompt.append("The backgrounds for each panel should flow seamlessly, i.e, there should be no panel that has (desert) and another with (moon) as it doesn't make sense.\n");
        prompt.append("The panels should describe a simple story about this/these characters and have a simple conclusion.\n\n");

        //format example
        prompt.append("The output format should be:\n\n");
        prompt.append("1. (background) [scene description]\n");
        prompt.append("2. (background) ...\n");
        prompt.append("...\n");
        prompt.append("6. (background) ...\n");

        return prompt.toString();
    }

    public static String buildDialoguePrompt(List<String> panelDescriptions, String targetLanguage) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Based on the following panel descriptions, please write simple, short dialogue for the characters in each panel. ");
        prompt.append("The dialogue should match the actions and scenes described.\n\n");

        //descriptions from llm response from previous prompt
        prompt.append("Panel descriptions:\n");
        for (int i = 0; i < panelDescriptions.size(); i++) {
            prompt.append((i + 1)).append(". ").append(panelDescriptions.get(i)).append("\n");
        }

        prompt.append("Write 1 or 2 lines of dialogue per panel, spoken by the characters mentioned.\n");
        prompt.append("Keep the English simple and natural — suitable for a language learner.\n");
        prompt.append("Use clear character names (i.e, Anna: \"Hello!\") before each line.\n");
        prompt.append("Then, for each line of English, provide a translation into ").append(targetLanguage).append(" below it.\n\n");

        //format example
        prompt.append("The output format should be:\n\n");
        prompt.append("1.\n");
        prompt.append("Alfie: \"I'm so hungry!\"\n");
        prompt.append("Translation: \"¡Tengo mucha hambre!\"\n\n");
        prompt.append("2.\n");
        prompt.append("Betty: \"Welcome to our restaurant!\"\n");
        prompt.append("Alfie: \"Thank you!\"\n");
        prompt.append("Translation: \"¡Bienvenido a nuestro restaurante!\"\n");
        prompt.append("Translation: \"¡Gracias!\"\n\n");
        prompt.append("...\n");
        prompt.append("Continue like this for all " + panelDescriptions.size() + " panels.\n");

        return prompt.toString();
    }
}
