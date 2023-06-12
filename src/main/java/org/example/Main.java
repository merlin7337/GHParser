package org.example;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String nickname = "torvalds";
        try {
            Map<String, String> userInfo = Parser.parseUserInfo(nickname);
            System.out.println("User " + nickname + " info:");
            for (Map.Entry<String, String> entry : userInfo.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("    " + key + " : " + value);
            }

            Map<String, String> userAchievements = Parser.parseUserAchievements(nickname);
            System.out.println("User " + nickname + " achievements:");
            for (Map.Entry<String, String> entry : userAchievements.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("    " + key + " : " + value);
            }

            Map<String, String> userActivity = Parser.parseUserActivity(nickname);
            System.out.println("User " + nickname + " activity:");
            for (Map.Entry<String, String> entry : userActivity.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("    " + key + " : " + value);
            }

            List<Map<String, String>> userPublicRepositories = Parser.parseUserPublicRepositories(nickname);
            System.out.println("User " + nickname + " repositories:");
            for (Map<String, String> e : userPublicRepositories) {
                System.out.println("    " + e.get("title") + ":");
                for (Map.Entry<String, String> entry : e.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    System.out.println("        " + key + " : " + value);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}