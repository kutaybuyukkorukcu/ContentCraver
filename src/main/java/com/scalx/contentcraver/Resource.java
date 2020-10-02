package com.scalx.contentcraver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalx.contentcraver.hackernews.service.HNCrawler;
import com.scalx.contentcraver.reddit.service.RDTCrawler;
import com.scalx.contentcraver.utils.ContentType;
import com.scalx.contentcraver.helper.Context;
import com.scalx.contentcraver.utils.StandardResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.TEXT_PLAIN)
public class Resource {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    HNCrawler hnCrawler;

    @Inject
    RDTCrawler rdtCrawler;

    @Inject
    Context context;

    // Convert To Post
    @POST
    @Path("/articles")
    public Response getArticles(@QueryParam("content") String content, String topic)
            throws IOException {

        if (ContentType.REDDIT.getContentType().equals(content.toUpperCase())) {
            context.setCrawlerStrategy(new RDTCrawler());
        }

        if (ContentType.HACKERNEWS.getContentType().equals(content.toUpperCase())) {
            context.setCrawlerStrategy(new HNCrawler());
        }

        List<BaseCard> articles =  context.getArticleLinks(topic);

        return Response.ok(
                new StandardResponse(
                    Response.Status.OK.getStatusCode(),
                    Response.Status.OK.getReasonPhrase(),
                    (int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                    objectMapper.convertValue(articles, JsonNode.class)
                )
        ).build();
    }


    // Convert To Post
    @POST
    @Path("/comments")
    public Response getComments(@QueryParam("content") String content, String link)
            throws IOException {

        if (ContentType.REDDIT.getContentType().equals(content.toUpperCase())) {
            context.setCrawlerStrategy(new RDTCrawler());
        }

        if (ContentType.HACKERNEWS.getContentType().equals(content.toUpperCase())) {
            context.setCrawlerStrategy(new HNCrawler());
        }

        List<BaseComment> comments =  context.getArticleComments(link);

        return Response.ok(
                new StandardResponse(
                        Response.Status.OK.getStatusCode(),
                        Response.Status.OK.getReasonPhrase(),
                        (int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                        objectMapper.convertValue(comments, JsonNode.class)
                )
        ).build();
    }

    // get articles with comments
}
