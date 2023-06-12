package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        String followers = followersAndFollowing.substring(0, followersAndFollowing.indexOf(' '))
                .replace("k", "000");
        userInfo.put("followers", followers);

        String following = followersAndFollowing.substring(followersAndFollowing.indexOf(' ')).trim()
                .replace("k", "000");
        userInfo.put("following", following);

        return userInfo;
    }

    public static Map<String, String> parseUserActivity(String nickname) throws IOException {
        Document doc = Jsoup.connect("https://github.com/" + nickname).get();
        Map<String, String> userActivity = new HashMap<>();

        String lastYearContributions = doc.select("h2[class=\"f4 text-normal mb-2\"]").text();
        Pattern pattern = Pattern.compile("\\d+.\\d+");
        Matcher matcher = pattern.matcher(lastYearContributions);
        while (matcher.find()) {
            lastYearContributions = lastYearContributions.substring(matcher.start(), matcher.end());
        }
        userActivity.put("last year contributions", lastYearContributions.replace(",", "").trim());

        return userActivity;
    }

    public static List<Map<String, String>> parseUserPublicRepositories(String nickname) throws IOException {
        Document doc = Jsoup.connect("https://github.com/" + nickname + "?tab=repositories").get();
        Elements repositories = doc.select("div[class=\"col-10 col-lg-9 d-inline-block\"]");
        List<Map<String, String>> repositoriesList = new ArrayList<>();

        for (Element e : repositories) {
            repositoriesList.add(parseRepository(nickname, e.select("a[itemprop=\"name codeRepository\"]").text()));
        }
        return repositoriesList;
    }

    public static Map<String, String> parseRepository(String nickname, String repoName) throws IOException {
        String url = "https://github.com/" + nickname + "/" + repoName;
        Document doc = Jsoup.connect(url).get();
        Map<String, String> repo = new HashMap<>();
        repo.put("title", repoName);
        repo.put("URL", url);
        repo.put("description", doc.select("p[class=\"f4 my-3\"]").text());
        repo.put("commits count", doc.select("li[class=\"ml-0 ml-md-3\"]").text()
                .replace(" commits", "")
                .replace(",", ""));
        repo.put("last edited", doc.select("relative-time").text());
        repo.put("last commit code", doc.select("a[class=\"f6 Link--secondary text-mono ml-2 d-none d-lg-inline\"]").text());
        return repo;
    }
}
