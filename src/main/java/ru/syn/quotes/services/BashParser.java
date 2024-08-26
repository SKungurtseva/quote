package ru.syn.quotes.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Component
public class BashParser {

    public Map<Integer, String> getPage(int page) {
        Map<Integer, String> quotes = new HashMap<>();
        try {
            Document doc = Jsoup.connect("https://bashorg.org/page/" + page).get();
            Elements sourceQuotes = doc.select(".q");

            for (Element quoteElement : sourceQuotes) {
                int id = Integer.parseInt(quoteElement.select("a").first().text().substring(8));
                String text = quoteElement.select(".quote").first().text();
                quotes.put(id, text);
            }
        } catch (IOException ignored) {
        }
        return quotes;
    }

    public Map.Entry<Integer, String> getById(int id) {
        try {
            Document doc = Jsoup.connect("https://bashorg.org/quote/" + id).get();
            Element quoteElement = doc.select(".q").first();

            String realId = quoteElement.select("a").first().text();
            if (realId.equals("#???")) return null;
            String text = quoteElement.select(".quote").first().text();
            return new AbstractMap.SimpleEntry<>(id, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map.Entry<Integer, String> getRandom() {
        try {
            Document doc = Jsoup.connect("https://bashorg.org/random").get();
            Element quoteElement = doc.select(".q").first();
            String realId = quoteElement.select("a").first().text();
            if (realId.equals("#???")) return null;

            String text = quoteElement.select(".quote").first().text();

            return new AbstractMap.SimpleEntry<>(Integer.parseInt(realId.substring(8)), text);
        } catch (IOException ignored) {
        }
        return null;
    }
}
