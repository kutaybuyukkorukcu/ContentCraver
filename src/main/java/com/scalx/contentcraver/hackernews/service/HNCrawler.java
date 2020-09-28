package com.scalx.contentcraver.hackernews.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.scalx.contentcraver.BaseCard;
import com.scalx.contentcraver.BaseComment;

import com.scalx.contentcraver.hackernews.entity.HNCard;
import com.scalx.contentcraver.utils.Strategy;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
public class HNCrawler implements Strategy {

    @Inject
    private ObjectMapper objectMapper;

    private static int sizeOfComments = 0;

    private static String articleId = "";

    @Override
    public List<BaseCard> getArticleLinks(String articleTopic) throws IOException {

        // the parameter is useless for now, top - new etc. sounds better


        Client client = ClientBuilder.newClient();

        String jsonString = client
                .target("https://hacker-news.firebaseio.com/v0/topstories.json")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

//        HttpResponse<kong.unirest.JsonNode> jsonResponse = Unirest
//                .get("https://hacker-news.firebaseio.com/v0/topstories.json")
//                .asJson();

//        String jsonString = jsonResponse.getBody().toString();

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        List<Integer> articleIdList = objectMapper.convertValue(
                jsonNode,
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class)
        );

        List<BaseCard> articleList = new ArrayList<>();

        for (Integer itemNumber : articleIdList.subList(0, 5)) {

            String articleString = client
                    .target("https://hacker-news.firebaseio.com/v0/item/" + itemNumber.toString() + ".json")
                    .request(MediaType.APPLICATION_JSON)
                    .get(String.class);

//            HttpResponse<kong.unirest.JsonNode> itemResponse = Unirest
//                    .get("https://hacker-news.firebaseio.com/v0/item/" + itemNumber.toString() + ".json")
//                    .asJson();
//
//            String articleString = itemResponse.getBody().getObject().toString();

            JsonNode articleNode = objectMapper.readTree(articleString);

            var isNodeTypeStory = articleNode.get("type").asText().equals("story");

            if (!isNodeTypeStory) {

                continue;
            }

            String text = "";
            String url = "";

            var isTextAvailable = articleNode.get("text") != null;

            if (isTextAvailable) {
                text = articleNode.get("text").asText();
            }

            var isUrlAvailable = articleNode.get("url") != null;

            if (isUrlAvailable) {
                url = articleNode.get("url").asText();
            }

            articleList.add(new HNCard(
                    articleNode.get("id").asText(),
                    articleNode.get("title").asText(),
                    "Development",
                    url,
                    articleNode.get("by").asText(),
                    text,
                    articleNode.get("score").asInt(),
                    articleNode.get("descendants").asInt(),
                    articleNode.get("time").asInt())
            );
        }

        articleList.forEach(System.out::println);

        return articleList;
    }

    @Override
    public List<BaseComment> getArticleComments(String articleId)
            throws JsonProcessingException {

//        HttpResponse<kong.unirest.JsonNode> jsonResponse = Unirest
//                .get("https://hacker-news.firebaseio.com/v0/item/" + articleId + ".json")
//                .asJson();

        List<BaseComment> comments = new ArrayList<>();

        String jsonString = null;
//                = jsonResponse.getBody().getObject().toString();

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        sizeOfComments = getCommentCount(jsonNode);

        articleId = getArticleId(jsonNode);

        List<Integer> kidCommentList = objectMapper.convertValue(
                jsonNode.get("kids"),
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class)
        );

        recursive(kidCommentList, comments);

        comments.forEach(System.out::println);

        sizeOfComments = 0;

        articleId = "";

        return comments;
    }

    public void recursive(List<Integer> kidCommentList, List<BaseComment> comments)
            throws JsonProcessingException {

        for (Integer kidComment : kidCommentList) {

//            HttpResponse<kong.unirest.JsonNode> jsonResponse = Unirest
//                    .get("https://hacker-news.firebaseio.com/v0/item/" + kidComment.toString() + ".json")
//                    .asJson();

            String commentString = null;
//                    jsonResponse.getBody().getObject().toString();

            JsonNode commentNode = objectMapper.readTree(commentString);

            var isNodeTypeComment = commentNode.get("type").asText().equals("comment");

            if (!isNodeTypeComment) {

                continue;
            }

            var isCommentDeleted = commentNode.get("deleted") != null;

            if (isCommentDeleted) {

                continue;
            }

            var isKidCommentAvailable = commentNode.get("kids") == null;

            if (isKidCommentAvailable) {

                comments.add(new BaseComment(
                        articleId,
                        commentNode.get("id").asText(),
                        commentNode.get("text").asText(),
                        commentNode.get("by").asText(),
                        commentNode.get("parent").asText(),
                        commentNode.get("time").asInt())
                );

                continue;
            }

            comments.add(new BaseComment(
                    articleId,
                    commentNode.get("id").asText(),
                    commentNode.get("text").asText(),
                    commentNode.get("by").asText(),
                    commentNode.get("parent").asText(),
                    commentNode.get("time").asInt())
            );

            List<Integer> kidCommentList1 = objectMapper.convertValue(
                    commentNode.get("kids"),
                    TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class)
            );

            recursive(kidCommentList1, comments);

            if (comments.size() >= sizeOfComments) {
                break;
            }
        }
    }

    private int getCommentCount(JsonNode jsonNode) {
        return Integer.parseInt(jsonNode.get("descendants").asText());
    }

    private String getArticleId(JsonNode jsonNode) {
        return jsonNode.get("id").asText();
    }
}