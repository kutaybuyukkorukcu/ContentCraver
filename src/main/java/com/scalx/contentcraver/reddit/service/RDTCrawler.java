package com.scalx.contentcraver.reddit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.scalx.contentcraver.Crawler;
import com.scalx.contentcraver.exception.ThrowableRedirectionException;
import com.scalx.contentcraver.hackernews.entity.HNComment;
import com.scalx.contentcraver.reddit.entity.RDTCard;
import com.scalx.contentcraver.helper.CrawlerStrategy;
import com.scalx.contentcraver.BaseCard;
import com.scalx.contentcraver.BaseComment;
import com.scalx.contentcraver.reddit.entity.RDTComment;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.*;

@Named
@ApplicationScoped
public class RDTCrawler extends Crawler implements CrawlerStrategy {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static int sizeOfComments = 0;

    private static String articleId = "";

    private static final Logger LOG = Logger.getLogger(RDTCrawler.class);

    // Main page of an subreddit
    @Override
    public List<BaseCard> getArticleLinks(String articleTopic) throws IOException {

        String jsonString = "";

        try {

            jsonString = CLIENT.target("https://www.reddit.com/r/" + articleTopic + ".json")
                    .request(MediaType.APPLICATION_JSON)
                    .header("User-agent", "Devnews")
                    .get(String.class);

        } catch (RedirectionException e) {
            LOG.info(RedirectionException.class.toString());
            LOG.info(e.getClass().toString());
            LOG.info(e.getMessage());
            throw new ThrowableRedirectionException(e);
        } catch (NotFoundException e) {
            LOG.info(e.getClass().toString());
            LOG.info(e.getMessage());
            throw new NotFoundException(e);
        }

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        List<LinkedHashMap<String, LinkedHashMap>> childrenList = objectMapper.convertValue(
                jsonNode.get("data").get("children"),
                TypeFactory.defaultInstance().constructCollectionType(List.class, LinkedHashMap.class)
        );

        List<BaseCard> rdtCardList = new ArrayList<>();

        for (LinkedHashMap<String, LinkedHashMap> linkedHashMap : childrenList) {

            LinkedHashMap<String, Object> articleMap = linkedHashMap.get("data");

            rdtCardList.add(new RDTCard(
                    articleMap.get("name").toString(),
                    articleMap.get("title").toString(),
                    articleMap.get("subreddit").toString(),
                    articleMap.get("url").toString(),
                    articleMap.get("author").toString(),
                    articleMap.get("selftext").toString(),
                    (int) articleMap.get("ups"),
                    (int) articleMap.get("num_comments"),
                    ((Double) articleMap.get("created")).intValue())
            );
        }

        return rdtCardList;
    }

    @Override
    public List<BaseComment> getArticleComments(String articleLink) throws IOException {

        // Processing exception might be throwwn cause of the articleLink.
        // It's probably best to create pattern for articleLink (regx)

        String jsonString = "";

        try {

            jsonString = CLIENT.target("https://www.reddit.com/r/" + articleLink + ".json")
                    .request(MediaType.APPLICATION_JSON)
                    .header("User-agent", "Devnews")
                    .get(String.class);

        }  catch (RedirectionException e) {
            LOG.info(e.getClass().toString());
            LOG.info(e.getMessage());
            throw new ThrowableRedirectionException(e);
        } catch (NotFoundException e) {
            LOG.info(e.getClass().toString());
            LOG.info(e.getMessage());
            throw new NotFoundException(e);
        }

        LinkedHashMap articleNode = getArticleNode(jsonString);

        sizeOfComments = getCommentCount(articleNode);

        articleId = getArticleId(articleNode);

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        List<LinkedHashMap<String, LinkedHashMap>> childrenList = objectMapper.convertValue(
                jsonNode.get(1).get("data").get("children"),
                TypeFactory.defaultInstance().constructCollectionType(List.class, LinkedHashMap.class)
        );

        List<BaseComment> comments = new ArrayList<>();

        recursive(childrenList, comments);

        sizeOfComments = 0;

        articleId = "";

        return comments;
    }

    public void recursive(List<LinkedHashMap<String, LinkedHashMap>> childrenList, List<BaseComment> comments) {

        for (LinkedHashMap<String, LinkedHashMap> linkedHashMap : childrenList) {

//             Checking for "Continue this thread"
            if (String.valueOf(linkedHashMap.get("kind")).equals("more")) {
                continue;
            }

            LinkedHashMap<String, Object> commentMap  =  linkedHashMap.get("data");


            if (commentMap.get("replies").equals("")) {

                comments.add(new RDTComment(
                        articleId,
                        commentMap.get("name").toString(),
                        commentMap.get("body").toString(),
                        commentMap.get("author").toString(),
                        commentMap.get("parent_id").toString(),
                        ((Double) commentMap.get("created")).intValue())
                );

                continue;
            }

            comments.add(new RDTComment(
                    articleId,
                    commentMap.get("name").toString(),
                    commentMap.get("body").toString(),
                    commentMap.get("author").toString(),
                    commentMap.get("parent_id").toString(),
                    ((Double) commentMap.get("created")).intValue())
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

    private LinkedHashMap getArticleNode(String jsonString) throws JsonProcessingException {

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        LOG.info(jsonNode.get(0).toPrettyString());

        List<LinkedHashMap<String, LinkedHashMap>> childrenList = objectMapper.convertValue(
                jsonNode.get(0).get("data").get("children"),
                TypeFactory.defaultInstance().constructCollectionType(List.class, LinkedHashMap.class)
        );

        return childrenList.get(0).get("data");
    }

    private int getCommentCount(LinkedHashMap articleNode) {
        return Integer.parseInt(articleNode.get("num_comments").toString());
    }

    private String getArticleId(LinkedHashMap articleNode) {
        return articleNode.get("name").toString();
    }
}
