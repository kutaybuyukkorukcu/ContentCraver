package com.scalx.contentcraver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.scalx.contentcraver.utils.StandardResponse;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.ObjectAssert;
import org.hamcrest.Matchers;
import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class RDTCrawlerTest {

//    https://www.reddit.comarticlelink.json/ get the expection
//    https://www.reddit.com/r/top.json private/forbidden throws 403
//    https://www.reddit.com/r/topa.json banned/not found throws 404

    @Inject
    ObjectMapper objectMapper;

    @Test
    public void test_getArticleLinksEndpoint_throwsRedirectionException() throws Exception {

        BaseRequest request = new BaseRequest(null, "mockdata");

        JsonNode json = objectMapper.convertValue(request, JsonNode.class);

        given()
            .when()
                .contentType(ContentType.JSON)
                .body(json)
                .queryParam("content", "reddit")
                .post("/api/articles")
            .then()
                .statusCode(302)
                .and()
                .body(is("Given subreddit is not active."));
    }

    @Test
    public void test_getArticleLinksEndpoint_throwsNotFoundException() throws Exception {

        BaseRequest request = new BaseRequest(null, "%22%22");

        JsonNode json = objectMapper.convertValue(request, JsonNode.class);

        given()
            .when()
                .contentType(ContentType.JSON)
                .body(json)
                .queryParam("content", "reddit")
                .post("/api/articles")
            .then()
                .statusCode(404)
                .and()
                .body(is("Given subreddit value is not suitable for the format. Check the parameter."));
    }

    @Test
    public void test_getArticleLinksEndpoint() throws Exception {

        RestAssured.defaultParser = Parser.JSON;

        BaseRequest request = new BaseRequest(null, "java");

        JsonNode json = objectMapper.convertValue(request, JsonNode.class);

        Response response = given()
                .when()
                    .contentType(ContentType.JSON)
                    .body(json)
                    .queryParam("content", "reddit")
                    .post("/api/articles")
                .thenReturn();

        assertThat(response.jsonPath().getInt("statusCode")).isEqualTo(200);
        assertThat(response.jsonPath().getString("message")).isEqualTo("OK");

        List<LinkedHashMap> list = response.jsonPath().getList("data");
        JsonNode jsonNode = objectMapper.convertValue(list.get(0), JsonNode.class);

//        assertThat(list.size()).isEqualTo(25);
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
    public void test_getArticleCommentsEndpoint_throwsRedirectionException() throws Exception {

        BaseRequest request = new BaseRequest("javacomments/j2l1l7/7_years_of_blogging_about_java/", null);

        JsonNode json = objectMapper.convertValue(request, JsonNode.class);

        given()
            .when()
                .contentType(ContentType.JSON)
                .body(json)
                .queryParam("content", "reddit")
                .post("/api/comments")
            .then()
                .statusCode(302)
                .and()
                .body(is("Given article link value is not suitable for the format. Check backslashes."));
    }

    @Test
    public void test_getArticleCommentsEndpoint_throwsNotFoundException() throws Exception {

        BaseRequest request = new BaseRequest("java/comments/j2l/7_years_of_blogging_about_java/", null);

        JsonNode json = objectMapper.convertValue(request, JsonNode.class);

        given()
            .when()
                .contentType(ContentType.JSON)
                .body(json)
                .queryParam("content", "reddit")
                .post("/api/comments")
            .then()
                .statusCode(404)
                .and()
                .body(is("Given article link value is not suitable for the format. Check comment id."));
    }

    @Test
    public void test_getArticleCommentsEndpoint() throws Exception {

        RestAssured.defaultParser = Parser.JSON;

        BaseRequest request = new BaseRequest("java/comments/j2l1l7/7_years_of_blogging_about_java/", null);

        JsonNode json = objectMapper.convertValue(request, JsonNode.class);

        Response response = given()
                .when()
                    .contentType(ContentType.JSON)
                    .body(json)
                    .queryParam("content", "reddit")
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
