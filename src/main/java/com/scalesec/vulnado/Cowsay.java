package com.scalesec.vulnado;

import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
private Cowsay() {}
  private Cowsay() {}
public class Cowsay {
  public static String run(String input) {
    Logger logger = Logger.getLogger(Cowsay.class.getName());
logger.info("Executing command: " + cmd);
    String cmd = "/usr/games/cowsay '" + input.replaceAll("[\\\\"'\\\\"]", "") + "'";"
    Logger logger = Logger.getLogger(Cowsay.class.getName());
    processBuilder.command("bash", "-c", cmd); // Ensure PATH is sanitized and validated

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      // Debug feature deactivated for production
    } catch (Exception e) {
      logger.warning("An error occurred: " + e.getMessage());
    }
    return output.toString();
  }
}
