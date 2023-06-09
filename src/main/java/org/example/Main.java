package org.example;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String nickname = "torvalds";
        try {
            Map<String, String> userAchievements = Parser.parseUserAchievements(nickname);
            Map<String, String> userInfo = Parser.parseUserInfo(nickname);
            System.out.println("User " + nickname + " info:");
            for (Map.Entry<String, String> entry : userInfo.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("    " + key + " : " + value);
            }
            System.out.println("User " + nickname + " achievements:");
            for (Map.Entry<String, String> entry : userAchievements.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("    " + key + " : " + value);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}