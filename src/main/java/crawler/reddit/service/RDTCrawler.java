package crawler.reddit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import crawler.reddit.model.Data;
import crawler.reddit.model.RDTCard;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

public class RDTCrawler {

    private ObjectMapper objectMapper;

    private Document document;

    public Document getSubredditAsDocument(String subreddit) throws IOException {
        return Jsoup.connect("https://www.reddit.com/r/" + subreddit + ".json").get();
    }

    public Document getArticleAsDocument(String articleLink) throws IOException {
        return Jsoup.connect("https://www.reddit.com" + articleLink).get();
    }

    // Main page of an subreddit
    public List<RDTCard> getArticleLinks(String subreddit) throws IOException {

        HttpResponse<JsonNode> jsonResponse = Unirest.get("https://www.reddit.com/r/java.json")
              .asJson();

        JsonNode jsonNode = jsonResponse.getBody();
        String jsonString = jsonNode.getObject().toString();

        objectMapper = new ObjectMapper();

        ObjectNode node = objectMapper.readValue(jsonString, ObjectNode.class);

        com.fasterxml.jackson.databind.JsonNode node1 = node.get("data").get("children");

        List<LinkedHashMap<String, LinkedHashMap>> childrenList = objectMapper.convertValue(node1, List.class);

        List<RDTCard> rdtCardList = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            LinkedHashMap<String, LinkedHashMap> mapim = childrenList.get(i).get("data");

            Data data = objectMapper.convertValue(mapim, Data.class);

            RDTCard rdtCard = new RDTCard(data.getTitle(), data.getSubreddit(), data.getUrl(), data.getAuthor(),
                    "https://www.reddit.com" + data.getPermalink(),
                    data.getSelftext(), data.getUps(), data.getNum_comments());

            System.out.println(rdtCard.toString());

            rdtCardList.add(rdtCard);
        }

//        LinkedHashMap<String, LinkedHashMap> mapim = childrenList.get(0).get("data");
//
//        Data data = objectMapper.convertValue(mapim, Data.class);
//
//        RDTCard rdtCard = new RDTCard(data.getTitle(), data.getSubreddit(), data.getAuthor(),
//                            "https://www.reddit.com" + data.getPermalink(),
//                            data.getSelftext(), data.getUps(), data.getNum_comments());
//
//        System.out.println(rdtCard.toString());

//        LinkedHashMap<String, LinkedHashMap> mapim1 = childrenList.get(3).get("data");

        return rdtCardList;
    }


    public static void main(String[] args) throws IOException {

        RDTCrawler RDTCrawler = new RDTCrawler();

        RDTCrawler.getArticleLinks("java");
    }
}
