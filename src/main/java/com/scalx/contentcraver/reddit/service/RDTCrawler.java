package com.scalx.contentcraver.reddit.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.scalx.contentcraver.hackernews.entity.HNComment;
import com.scalx.contentcraver.reddit.entity.RDTCard;
import com.scalx.contentcraver.utils.Strategy;
import com.scalx.contentcraver.BaseCard;
import com.scalx.contentcraver.BaseComment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.*;

@Named
@ApplicationScoped
public class RDTCrawler implements Strategy {

    @Inject
    private ObjectMapper objectMapper;

    private static int sizeOfComments = 0;

    private static String articleId = "";

    // Main page of an subreddit
    @Override
    public List<BaseCard> getArticleLinks(String articleTopic) throws IOException {

        Client client = ClientBuilder.newClient();

        String jsonString = client.target("https://www.reddit.com/r/" + articleTopic + ".json")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

//        HttpResponse<kong.unirest.JsonNode> jsonResponse = Unirest
//                .get("https://www.reddit.com/r/" + articleTopic + ".json")
//                .asJson();
//
//        String jsonString = jsonResponse.getBody().getObject().toString();

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        List<LinkedHashMap<String, LinkedHashMap>> childrenList = objectMapper.convertValue(
                jsonNode.get("data").get("children"),
                TypeFactory.defaultInstance().constructCollectionType(List.class, LinkedHashMap.class)
        );

        List<BaseCard> rdtCardList = new ArrayList<>();

        for (LinkedHashMap<String, LinkedHashMap> linkedHashMap : childrenList) {

            LinkedHashMap<String, Object> mapim = linkedHashMap.get("data");

            rdtCardList.add(new RDTCard(
                    mapim.get("name").toString(),
                    mapim.get("title").toString(),
                    mapim.get("subreddit").toString(),
                    mapim.get("url").toString(),
                    mapim.get("author").toString(),
                    mapim.get("selftext").toString(),
                    (int) mapim.get("ups"),
                    (int) mapim.get("num_comments"),
                    ((Double) mapim.get("created")).intValue())
            );
        }

        rdtCardList.forEach(System.out::println);

        return rdtCardList;
    }

    @Override
    public List<BaseComment> getArticleComments(String articleLink) throws IOException {

//        HttpResponse<kong.unirest.JsonNode> jsonResponse = Unirest
//                .get(articleLink.substring(0, articleLink.length() - 1) + ".json")
//                .asJson();

//        LinkedHashMap articleNode = getArticleNode(jsonResponse);

//        sizeOfComments = getCommentCount(articleNode);
//
//        articleId = getArticleId(articleNode);

        String jsonString = null;
//                = jsonResponse.getBody().getArray().get(1).toString();

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        List<LinkedHashMap<String, LinkedHashMap>> childrenList = objectMapper.convertValue(
                jsonNode.get("data").get("children"),
                TypeFactory.defaultInstance().constructCollectionType(List.class, LinkedHashMap.class)
        );

        List<BaseComment> comments = new ArrayList<>();

        recursive(childrenList, comments);

        comments.forEach(System.out::println);

        sizeOfComments = 0;

        articleId = "";

        return comments;
    }

    public void recursive(List<LinkedHashMap<String, LinkedHashMap>> childrenList, List<BaseComment> comments) {

        for (LinkedHashMap<String, LinkedHashMap> linkedHashMap : childrenList) {

            LinkedHashMap<String, Object> mapim  =  linkedHashMap.get("data");

            if (mapim.get("replies").equals("")) {

                comments.add(new HNComment(
                        articleId,
                        mapim.get("name").toString(),
                        mapim.get("body").toString(),
                        mapim.get("author").toString(),
                        mapim.get("parent_id").toString(),
                        ((Double) mapim.get("created")).intValue())
                );

                continue;
            }

            comments.add(new HNComment(
                    articleId,
                    mapim.get("name").toString(),
                    mapim.get("body").toString(),
                    mapim.get("author").toString(),
                    mapim.get("parent_id").toString(),
                    ((Double) mapim.get("created")).intValue())
            );

            LinkedHashMap<String, LinkedHashMap> linkedHashMap2 = objectMapper.convertValue(
                    linkedHashMap.get("data").get("replies"),
                    TypeFactory.defaultInstance().constructType(LinkedHashMap.class)
            );

            List<LinkedHashMap<String, LinkedHashMap>>  childrenList1 = objectMapper.convertValue(
                    linkedHashMap2.get("data").get("children"),
                    TypeFactory.defaultInstance().constructCollectionType(List.class, LinkedHashMap.class)
            );

            recursive(childrenList1, comments);

            if (comments.size() >= sizeOfComments) {
                break;
            }
        }
    }

//    private LinkedHashMap getArticleNode(HttpResponse<kong.unirest.JsonNode> jsonResponse)
//            throws JsonProcessingException {
//
//        String jsonString = jsonResponse.getBody().getArray().get(0).toString();
//
//        JsonNode jsonNode = objectMapper.readTree(jsonString);
//
//        List<LinkedHashMap<String, LinkedHashMap>> childrenList = objectMapper.convertValue(
//                jsonNode.get("data").get("children"),
//                TypeFactory.defaultInstance().constructCollectionType(List.class, LinkedHashMap.class)
//        );
//
//        return childrenList.get(0).get("data");
//    }

    private int getCommentCount(LinkedHashMap articleNode) {
        return Integer.parseInt(articleNode.get("num_comments").toString());
    }

    private String getArticleId(LinkedHashMap articleNode) {
        return articleNode.get("name").toString();
    }
}
