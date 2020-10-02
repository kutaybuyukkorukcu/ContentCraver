package com.scalx.contentcraver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

public class HNCrawlerTest {

    @Inject
    ObjectMapper objectMapper;

    @Test
    public void test_getArticleLinksEndpoint_throwsNotAuthorizedException() throws Exception {

        given()
            .when()
                .contentType(ContentType.TEXT)
                .body("mockdata")
                .queryParam("content", "hackernews")
                .post("/api/articles")
            .then()
                .statusCode(401)
                .and()
                .body(is("HTTP 401 Unauthorized"));
    }

    @Test
    public void test_getArticleLinksEndpoint() throws Exception {

        RestAssured.defaultParser = Parser.JSON;

        Response response = given()
                .when()
                    .contentType(ContentType.TEXT)
                    .body("top")
                    .queryParam("content", "reddit")
                    .post("/api/articles")
                .thenReturn();

        assertThat(response.jsonPath().getInt("statusCode")).isEqualTo(200);
        assertThat(response.jsonPath().getString("message")).isEqualTo("OK");

        List<LinkedHashMap> list = response.jsonPath().getList("data");
        JsonNode jsonNode = objectMapper.convertValue(list.get(0), JsonNode.class);

        assertThat(list.size()).isEqualTo(25);
        assertThat(jsonNode.get("article_id")).isInstanceOf(TextNode.class);
        assertThat(jsonNode.get("title")).isInstanceOf(TextNode.class);
        assertThat(jsonNode.get("main_topic")).isInstanceOf(TextNode.class);
        assertThat(jsonNode.get("url")).isInstanceOf(TextNode.class);
        assertThat(jsonNode.get("author")).isInstanceOf(TextNode.class);
        assertThat(jsonNode.get("text")).isInstanceOf(TextNode.class);
        assertThat(jsonNode.get("upvote_count")).isInstanceOf(IntNode.class);
        assertThat(jsonNode.get("comment_count")).isInstanceOf(IntNode.class);
        assertThat(jsonNode.get("created")).isInstanceOf(IntNode.class);
    }

    @Test
    public void test_getArticleCommentsEndpoint_throwsUnexpectedValueException() throws Exception {

        given()
            .when()
                .contentType(ContentType.TEXT)
                .body("mockdata")
                .queryParam("content", "hackernews")
                .post("/api/comments")
            .then()
                .statusCode(400)
                .and()
                .body(is("HTTP 400 Bad Request"));
    }

    @Test
    public void test_getArticleCommentsEndpoint() throws Exception {

        RestAssured.defaultParser = Parser.JSON;

        Response response = given()
                .when()
                    .contentType(ContentType.TEXT)
                    .body("mockdata")
                    .queryParam("content", "hackernews")
                    .post("/api/comments")
                .thenReturn();

        assertThat(response.jsonPath().getInt("statusCode")).isEqualTo(200);
        assertThat(response.jsonPath().getString("message")).isEqualTo("OK");

        List<LinkedHashMap> list = response.jsonPath().getList("data");
        JsonNode jsonNode = objectMapper.convertValue(list.get(0), JsonNode.class);

        assertThat(jsonNode.get("article_id")).isInstanceOf(TextNode.class);
        assertThat(jsonNode.get("comment_id")).isInstanceOf(TextNode.class);
        assertThat(jsonNode.get("text")).isInstanceOf(TextNode.class);
        assertThat(jsonNode.get("user")).isInstanceOf(TextNode.class);
        assertThat(jsonNode.get("parent_comment_id")).isInstanceOf(TextNode.class);
        assertThat(jsonNode.get("created")).isInstanceOf(IntNode.class);
    }

}
