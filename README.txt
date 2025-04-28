COMP30880
JSON'S WORLD (Sebastian Drayne, Sean Keane, Taisia Danilova)

DEPENDENCIES:
Apache HttpComponents Library: used in the main file to send HTTP requests to the API and process the response.
Google gson library: Used to handle JSON parsing.
Maven assembly plugin

STEPS TO RUN:

Set up config.properties to contain:
api.key={api_key}
llm.model=gpt-4o-mini
language=preferred_language
schedule={comma separated list of lessons}
{lesson name}.prompt={prompt to run for a lesson}
{lesson name}.characters={Characters to use for a lesson}
{lesson name}.backgrounds={Backgrounds to use for a lesson}
{lesson name}.poses={Poses to use for a lesson}

To run:
mvn clean install
mvn package

In console run:
java -jar (Path to jar file) (path to config file)

The path to the runnable jar is:
target/JSONsWorld-1.0-SNAPSHOT-jar-with-dependencies.jar

Blog URL: https://jsonsworld.blogspot.com/

