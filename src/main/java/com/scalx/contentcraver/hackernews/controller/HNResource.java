package com.scalx.contentcraver.hackernews.controller;

import com.scalx.contentcraver.BaseCard;
import com.scalx.contentcraver.hackernews.service.HNCrawler;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HNResource {

    @Inject
    HNCrawler hnCrawler;

    @GET
    public Response list() throws IOException {

        List<BaseCard> articles;

        try {
            articles =  hnCrawler.getArticleLinks("lool");

            return Response.ok(articles).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.ok(new ArrayList()).build();
    }
}
