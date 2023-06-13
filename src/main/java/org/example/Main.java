package org.example;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter GitHub username: ");
        Scanner scanner = new Scanner(System.in);
        String nickname = scanner.nextLine();
        try {
            Map<String, String> userInfo = GHParser.parseUserInfo(nickname);
            System.out.println("User " + nickname + " info:");
            for (Map.Entry<String, String> entry : userInfo.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("    " + key + " : " + value);
            }

            Map<String, String> userAchievements = GHParser.parseUserAchievements(nickname);
            System.out.println("User " + nickname + " achievements:");
            for (Map.Entry<String, String> entry : userAchievements.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("    " + key + " : " + value);
            }

            List<Map<String, String>> userPublicRepositories = GHParser.parseUserPublicRepositories(nickname);
            System.out.println("User " + nickname + " repositories:");
            for (Map<String, String> e : userPublicRepositories) {
                System.out.println("    " + e.get("title") + ":");
                for (Map.Entry<String, String> entry : e.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    System.out.println("        " + key + " : " + value);
                }
            }
        } catch (IOException | StringIndexOutOfBoundsException exception) {
            System.out.println("Invalid username");
        }
    }
}