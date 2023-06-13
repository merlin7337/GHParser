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

public class GHParser {
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

        String lastYearContributions = doc.select("h2[class=\"f4 text-normal mb-2\"]").text();
        Pattern pattern = Pattern.compile("\\d+.\\d+");
        Matcher matcher = pattern.matcher(lastYearContributions);
        while (matcher.find()) {
            lastYearContributions = lastYearContributions.substring(matcher.start(), matcher.end());
        }
        userInfo.put("last year contributions", lastYearContributions.replace(",", ""));

        return userInfo;
    }

    public static List<Map<String, String>> parseUserPublicRepositories(String nickname) throws IOException {
        Document doc = Jsoup.connect("https://github.com/" + nickname + "?tab=repositories").get();
        Elements repositories = doc.select("div[class=\"col-10 col-lg-9 d-inline-block\"]");
        List<Map<String, String>> repositoriesList = new ArrayList<>();

        for (Element e : repositories) {
            repositoriesList.add(parseRepository(e));
        }
        return repositoriesList;
    }

    private static Map<String, String> parseRepository(Element e) throws IOException {
        Map<String, String> repo = new HashMap<>();
        String href = e.select("a[itemprop=\"name codeRepository\"]").attr("href");
        String url = "https://github.com" + href;
        Document doc = Jsoup.connect(url).get();

        repo.put("title", e.select("a[itemprop=\"name codeRepository\"]").text());

        repo.put("description", e.select("p[itemprop=\"description\"]").text());

        repo.put("URL", url);

        repo.put("main language", e.select("span[itemprop=\"programmingLanguage\"]").text());

        try {
            repo.put("stars", e.select("a[href=\"" + href + "/stargazers\"]").text()
                    .replace(",", ""));
        } catch (NullPointerException exception) {
            repo.put("stars", "0");
        }

        try {
            repo.put("forks", e.select("a[href=\"" + href + "/forks\"]").text()
                    .replace(",", ""));
        } catch (NullPointerException exception) {
            repo.put("forks", "0");
        }

        try {
            repo.put("license", e.select("svg[class=\"octicon octicon-law mr-1\"]")
                    .parents()
                    .first()
                    .select("span[class=\"mr-3\"]")
                    .text());
        } catch (NullPointerException exception) {
            repo.put("license", "");
        }

        repo.put("last edited", e.select("relative-time").text());

        repo.put("commits count", doc.select("li[class=\"ml-0 ml-md-3\"]").text()
                .replace(" commits", "")
                .replace(",", ""));

        String watchersCount = doc.select("a[href=\"" + href + "/watchers\"]").text()
                .replace(" watching", "");
        watchersCount = watchersCount.contains(".")
                ? watchersCount.replace(".", "").replace("k", "00")
                : watchersCount.replace("k", "000");
        repo.put("watchers count", watchersCount);

        repo.put("branches count", doc.select("a[href=\"" + href + "/branches\"]").text()
                .replace(" branches", "")
                .replace(" branch", "")
                .replace("View all ", ""));

        return repo;
    }
}
