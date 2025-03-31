COMP30880
JSON'S WORLD (Sebastian Drayne, Sean Keane, Taisia Danilova)

SPRINT 1 OVERVIEW:
For this sprint we had to set up a connection with the API.
We have set up a JSONsWorldMain which handles the connection with the API.
The file reads the configuration values, ie, the API key and model name, from the config.properties file (made separately for confidentiality)
then sends a request with a given input prompt. The response is then printed to the console.
OutputProcessor handles getting what ChatGPT returned.
ContextManager handles building the prompt to send.

SPRINT 2 OVERVIEW:
We added in a systemn to parse TSV files and translate individual sections, then update the TSV with the translations and save to a file.

SPRINT 3 OVERVIEW:
Fixed key aspects of the Vignette layout. VignetteSchema, VignetteManager and VignetteXML. Basically just manages all the vignettes and builds appropriate XML file for the scenes. Translated aspects have been integrated aswell.

PROJECT STRUCTURE:
JSONsWorldMain.java: the main class that handles the HTTP request and processing the API's response.
ConfigurationFile.java: class that reads configuration values from a config.properties file.
OutputProcessor.java: Handles the value returned from ChatGPT.
ContextManager.java: Builds the context the context and appends the prompt.
config.properties: configuration file is stored outside and contains all the info needed to run. It's stored separately for flexibility.

DEPENDENCIES:
Apache HttpComponents Library: used in the main file to send HTTP requests to the API and process the response.
Google gson library: Used to handle JSON parsing.

STEPS TO RUN:

Set up config.properties to contain:
api.key={api_key}   #{api_key} being the API key
llm.model=gpt-4o-mini
prompt={Prompt}  #{Prompt} being the prompt you wish to run.
language=preferred_language

To run:
mvn clean install
mvn package

In console run:
java -jar (Path to jar file)

The path to the runnable jar is:
target/JSONsWorld-1.0-SNAPSHOT-jar-with-dependencies.jar

Blog URL: https://jsonsworld.blogspot.com/

