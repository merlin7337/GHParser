package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static Map<String, String> parseUserAchievements(String nickname) throws IOException {
        Document doc = Jsoup.connect("https://github.com/" + nickname + "?tab=achievements").get();
        Map<String, String> userAchievements = new HashMap<>();
        Elements achievements = doc.getElementsByClass("d-flex flex-justify-center mb-1");

        for (Element e : achievements) {
            if (e.text().contains("x")) {
                userAchievements.put(e.text().substring(0, e.text().lastIndexOf('x')).trim(),
                        e.text().substring(e.text().lastIndexOf('x')).substring(1));
            } else {
                userAchievements.put(e.text(), "1");
            }
        }

        return userAchievements;
    }

    public static Map<String, String> parseUserInfo(String nickname) throws IOException {
        Document doc = Jsoup.connect("https://github.com/" + nickname).get();
        Map<String, String> userInfo = new HashMap<>();

        String name = doc.select("span[itemprop=\"name\"]").text();
        userInfo.put("name", name);

        String organization = doc.select("li[itemprop=\"worksFor\"]").text();
        userInfo.put("organization", organization);

        String homeLocation = doc.select("li[itemprop=\"homeLocation\"]").text();
        userInfo.put("home location", homeLocation);

        String followersAndFollowing = doc.select("span[class=\"text-bold color-fg-default\"]").text();
        String followers = followersAndFollowing.substring(0, followersAndFollowing.indexOf(' '));
        userInfo.put("followers", followers);
        String following = followersAndFollowing.substring(followersAndFollowing.indexOf(' ')).trim();
        userInfo.put("following", following);

        return userInfo;
    }

}
