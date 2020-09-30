package com.scalx.contentcraver.hackernews.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.scalx.contentcraver.BaseCard;
import com.scalx.contentcraver.BaseComment;

import com.scalx.contentcraver.Crawler;
import com.scalx.contentcraver.hackernews.entity.HNCard;
import com.scalx.contentcraver.utils.CrawlerStrategy;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
public class HNCrawler extends Crawler implements CrawlerStrategy {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static int sizeOfComments = 0;

    private static String articleId = "";

    private static final Logger LOG = Logger.getLogger(HNCrawler.class);

    @Override
    public List<BaseCard> getArticleLinks(String articleTopic) throws IOException {

        String jsonString = "";

        try {
            jsonString = CLIENT
                    .target("https://hacker-news.firebaseio.com/v0/" + articleTopic + "stories.json")
                    .request(MediaType.APPLICATION_JSON)
                    .get(String.class);
        } catch (RuntimeException e) {

            LOG.info(jsonString);
            LOG.info(e.getClass().toString());
            throw new NotAuthorizedException(e);
        }

        // ObjectMapper will throw NullPointer anyway
        if (jsonString.equals("null")) {
            // Create a new NullPointerException.
            throw new NullPointerException();
        }

        LOG.info((jsonString));

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        List<Integer> articleIdList = objectMapper.convertValue(
                jsonNode,
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class)
        );

        List<BaseCard> articleList = new ArrayList<>();

        for (Integer itemNumber : articleIdList.subList(0, 5)) {

            String articleString = "";

            try {

                articleString = CLIENT
                        .target("https://hacker-news.firebaseio.com/v0/item/" + itemNumber.toString() + ".json")
                        .request(MediaType.APPLICATION_JSON)
                        .get(String.class);

            } catch (RuntimeException e) {

                LOG.info(jsonString);
                LOG.info(e.getClass().toString());
                throw new NotAuthorizedException(e);
            }

            // https://hacker-news.firebaseio.com/v0/item/top.json
            // Instead of returning 404, it returns null as plain text
            if (articleString.equals("null")) {
                // Create a new NullPointerException.
                throw new NullPointerException();
            }

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

        return articleList;
    }

    @Override
    public List<BaseComment> getArticleComments(String articleLink) throws JsonProcessingException {

        String jsonString = "";

        try {
            jsonString = CLIENT
                   .target("https://hacker-news.firebaseio.com/v0/item/" + articleLink + ".json")
                   .request(MediaType.APPLICATION_JSON)
                   .get(String.class);
        } catch (RuntimeException e) {

           LOG.info(jsonString);
           LOG.info(e.getClass().toString());
           throw new NotAuthorizedException(e);
        }

        // https://hacker-news.firebaseio.com/v0/item/top.json
        // Instead of returning 404, it returns null as plain text
        if (jsonString.equals("null")) {
            throw new NullPointerException();
        }

        List<BaseComment> comments = new ArrayList<>();

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        sizeOfComments = getCommentCount(jsonNode);

        articleId = getArticleId(jsonNode);

        List<Integer> kidCommentList = objectMapper.convertValue(
                jsonNode.get("kids"),
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class)
        );

        recursive(kidCommentList, comments);

        sizeOfComments = 0;

        articleId = "";

        return comments;
    }

    public void recursive(List<Integer> kidCommentList, List<BaseComment> comments)
            throws JsonProcessingException {

        for (Integer kidComment : kidCommentList) {

            String commentString = "";

            try {

                commentString = CLIENT
                        .target("https://hacker-news.firebaseio.com/v0/item/" + kidComment.toString() + ".json")
                        .request(MediaType.APPLICATION_JSON)
                        .get(String.class);

            } catch (RuntimeException e) {

                LOG.info(commentString);
                LOG.info(e.getClass().toString());
                throw new NotAuthorizedException(e);
            }

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