package com.JSONsWorld.main.story;

import com.JSONsWorld.main.api.OutputProcessor;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

public class StoryManager {

    public static String generateSimplePanelDescriptions(String[] backgrounds, String[] characters, String[] poses, String prompt) throws IOException {
        String text = String.format("""
                You are generating a 6-panel comic story for a language learner. Your first task is to generate the structure. To do this you will pick the backgrounds, characters and poses that will be used.
                Backgrounds that you can choose are: %s
                Characters that you can choose are: %s
                Poses for the characters are: %s
                
                The prompt you are following is: %s
                
                Please generate a simple description of the story, with a short description for each of the panels, and what each character does in the panels.
                Make sure each panel naturally flows into the next, so for example a character can't go from a desert to a kitchen as that's not realistic.
                In a later step the dialogue will be generated, however, for this step you only need to generate the outline.
                Some rules that will be applied when generating the dialogue are that only 1 character may speak per panel but each panel may have 1-2 characters.
                This means IF the prompt says that one character will say something while the other translates then make sure to have the backgrounds change every other panel.
                Your response must be brief and with minimal formatting (like no bold text or anything). Get the necessary info across. All of the characters in this comic are simple and don't have a significant amount of room for expression.
                Don't send anything except your response.""",
                String.join(", ", backgrounds), String.join(", ", characters), String.join(", ", poses), prompt);

        return sendPrompt(text);
    }

    public static String generatePanelDescriptions(String[] backgrounds, String[] characters, String[] poses, String storyDescription) throws IOException {

        String descriptionPrompt = buildDescriptionPrompt(backgrounds, characters, poses, storyDescription);
        //send prompt to LLM
        return sendPrompt(descriptionPrompt);
    }


    public static String buildDescriptionPrompt(String[] backgrounds, String[] characters, String[] poses, String storyDescription) {
        return String.format("""
                You are generating a 6-panel comic story for a language learner. Your first task is to generate the structure. To do this you will pick the backgrounds, characters and poses that will be used.
                Backgrounds that you can choose are: %s
                Characters that you can choose are: %s
                Poses for the characters are: %s
                
                The description of the story you are creating a structure for is %s
                
                When the dialogue is written later only one character may speak at a time, so keep that in mind when choosing poses.
                Don't use all the backgrounds. At most use 1-3 different types that don't change the flow of story, since we assume that the characters stay in one setting.
                Format your response like so (do not put the answers in parenthesis):
                Background - (Panel 1 background), Characters - (Panel 1 character 1):(Panel 1 character 2), Poses - (Panel 1 character 1 pose):(Panel 1 character 2 pose)
                Background - (Panel 2 background), Characters - (Panel 2 character 1):(Panel 2 character 2), Poses - (Panel 2 character 1 pose):(Panel 2 character 2 pose)
                ...
                
                Don't send anything except your formatted response. Please note the : denoting that the 2 characters/poses are in the same panel, remember to include it even if only 1 character is in the panel.
                Remember, this is a 6 panel comic and should flow as a normal comic would.""", String.join(", ", backgrounds),
                String.join(", ", characters), String.join(", ", poses), storyDescription);
    }

    public static String generateDialogue(String panelDescriptions, String storyPrompt, String targetLanguage) throws IOException {

        String dialoguePrompt = buildDialoguePrompt(panelDescriptions, storyPrompt, targetLanguage);
        // send to LLM
        String response = sendPrompt(dialoguePrompt);

        return response;
    }





    private static String buildDialoguePrompt(String panelDescriptions, String storyPrompt, String targetLanguage) {
        StringBuilder prompt = new StringBuilder();

        String promptBeginning = String.format("""
                Your job is to write the dialogue for a story based on several predetermined panels. This story is intended to teach people %s. Here is the prompt for the story you must follow "%s", and here is the panel info:
                
                """, targetLanguage, storyPrompt);

        prompt.append(promptBeginning);

        //descriptions from llm response from previous prompt
        prompt.append(panelDescriptions);



        String promptEnd = String.format("""
                Write 1 or 2 lines of dialogue per panel, spoken by the characters mentioned.
                - Keep the English simple and natural — suitable for a language learner.
                - Use clear character names (i.e, Anna: Hello!) before each line.
                - If the prompt mentions providing a translation, for each line of English, provide a translation into %s below it. Otherwise, do not provide a translation.
                DO NOT provide a Translation if the prompt says another character will translate the text. This means a character says something then in the next panel another character translates it.
                - ONLY ONE CHARACTER MAY SPEAK PER-PANEL
                For example, if the language is Spanish and the backgrounds from the panel descriptions imply it should be in a restaurant, then the output format should be:
                Alfie: I'm so hungry!|¡Tengo mucha hambre!
                -
                Betty: Welcome to our restaurant!|¡Bienvenido a nuestro restaurante!
                -
                Alfie: Thank you! Where can I sit?|¡Gracias! ¿Dónde puedo sentarme?
                -
                ...
                
                Please note the - separating panels and the | separating text from their translations. Also note that 2 characters speaking in the same panel aren't separated by a -. Continue like this for all %s panels.
                DO NOT DIVERGE FROM THIS FORMAT.""", targetLanguage, panelDescriptions.split("\n").length);

        prompt.append(promptEnd);

        return prompt.toString();
    }



    // ------------------------------------------------------------



    // ------------------------------------------------------------
    private static String sendPrompt(String prompt) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");

        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + TranslationProcessor.config.getProperty("api.key"));

        // Build JSON body
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);

        JsonArray messages = new JsonArray();
        messages.add(message);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", TranslationProcessor.config.getProperty("llm.model"));
        requestBody.add("messages", messages);

        StringEntity entity = new StringEntity(new Gson().toJson(requestBody), "UTF-8");
        post.setEntity(entity);

        return OutputProcessor.processResponse(EntityUtils.toString(httpClient.execute(post).getEntity()));
    }
}
