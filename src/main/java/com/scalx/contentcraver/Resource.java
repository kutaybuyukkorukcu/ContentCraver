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
@Consumes(MediaType.APPLICATION_JSON)
public class Resource {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    HNCrawler hnCrawler;

    @Inject
    RDTCrawler rdtCrawler;

    @Inject
    Context context;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @POST
    @Path("/stories")
    public Response getArticles(@QueryParam("content") String content, BaseRequest request)
            throws IOException {

        if (ContentType.REDDIT.getContentType().equals(content.toUpperCase())) {
            context.setCrawlerStrategy(new RDTCrawler());
        }

        if (ContentType.HACKERNEWS.getContentType().equals(content.toUpperCase())) {
            context.setCrawlerStrategy(new HNCrawler());
        }

        List<BaseStory> articles =  context.getArticleLinks(request.getTopic());

        return Response.ok(
                new StandardResponse(
                    Response.Status.OK.getStatusCode(),
                    Response.Status.OK.getReasonPhrase(),
                    (int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                    objectMapper.convertValue(articles, JsonNode.class)
                )
        ).build();
    }

    @POST
    @Path("/comments")
    public Response getComments(@QueryParam("content") String content, BaseRequest request)
            throws IOException {

        if (ContentType.REDDIT.getContentType().equals(content.toUpperCase())) {
            context.setCrawlerStrategy(new RDTCrawler());
        }

        if (ContentType.HACKERNEWS.getContentType().equals(content.toUpperCase())) {
            context.setCrawlerStrategy(new HNCrawler());
        }

        List<BaseComment> comments =  context.getArticleComments(request.getLink());

        return Response.ok(
                new StandardResponse(
                        Response.Status.OK.getStatusCode(),
                        Response.Status.OK.getReasonPhrase(),
                        (int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                        objectMapper.convertValue(comments, JsonNode.class)
                )
        ).build();
    }

//    @POST
//    @Path("/articles")
//    public Response getArticleWithComments(@QueryParam("content") String content,
//                                @QueryParam("withComments") boolean isWithComment,
//                                BaseRequest request) throws IOException {
//
//        if (ContentType.REDDIT.getContentType().equals(content.toUpperCase())) {
//            context.setCrawlerStrategy(new RDTCrawler());
//        }
//
//        if (ContentType.HACKERNEWS.getContentType().equals(content.toUpperCase())) {
//            context.setCrawlerStrategy(new HNCrawler());
//        }
//
//        if (isWithComment) {
//
//        }
//
//        List<BaseComment> comments =  context.getArticleComments(request.getLink());
//
//        return Response.ok(
//                new StandardResponse(
//                        Response.Status.OK.getStatusCode(),
//                        Response.Status.OK.getReasonPhrase(),
//                        (int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
//                        objectMapper.convertValue(comments, JsonNode.class)
//                )
//        ).build();
//    }
}
