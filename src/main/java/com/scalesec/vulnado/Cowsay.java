package com.scalesec.vulnado;

import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
private Cowsay() {}
  private Cowsay() {}
public class Cowsay {
  public static String run(String input) {
    Logger logger = Logger.getLogger(Cowsay.class.getName());
    String cmd = "/usr/games/cowsay '" + input.replaceAll("[\\\\"'\\\\"]", "") + "'";
    Logger logger = Logger.getLogger(Cowsay.class.getName());
    processBuilder.command("bash", "-c", cmd); // Ensure PATH is sanitized and validated

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      logger.warning("Debug feature activated: " + e.getMessage());
    } catch (Exception e) {
      logger.warning("Debug feature activated: " + e.getMessage());
    }
    return output.toString();
  }
}
