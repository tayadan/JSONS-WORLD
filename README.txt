COMP30880
JSON'S WORLD (Sebastian Drayne, Sean Keane, Taisia Danilova)

SPRINT 1 OVERVIEW:
For this sprint we had to set up a connection with the API.
We have set up a JSONsWorldMain which handles the connection with the API.
The file reads the configuration values, ie, the API key and model name, from the config.properties file (made separately for confidentiality)
then sends a request with a given input prompt. The response is then printed to the console.

(need to write more for the numbered list, denial and config changes (environment variables used if no config??))

PROJECT STRUCTURE:
JSONsWorldMain.java: the main class that handles the HTTP request and processing the API's response.
ConfigurationFile.java: class that reads configuration values from a config.properties file
config.properties: configuration file storing API keys and model details, made separately for confidentiality.

DEPENDENCIES:
Apache HttpComponents Library: used in the main file to send HTTP requests to the API and process the response.

STEPS TO RUN:

Set up config.properties to contain:
api.key={api_key}   #{api_key} being the API key
llm.model=gpt-4o-mini

Compile Java program (idk how on cmd):
Run Java program (idk how on cmd): ;__;

