package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Elements achievements = parseAchievements("torvalds");
            for (Element e : achievements) {
                System.out.println(e.text());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Elements parseAchievements(String nickname) throws IOException {
        Document doc = Jsoup.connect("https://github.com/" + nickname + "?tab=achievements").get();
        return doc.getElementsByClass("d-flex flex-justify-center mb-1");
    }
}