package com.scalx.contentcraver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class HNCrawlerTest {

    @Inject
    ObjectMapper objectMapper;

    @Test
    public void test_getArticleLinksEndpoint_throwsNotAuthorizedException() throws Exception {

        BaseRequest request = new BaseRequest(null, "mockdata");

        JsonNode json = objectMapper.convertValue(request, JsonNode.class);

        given()
            .when()
                .contentType(ContentType.JSON)
                .body(json)
                .queryParam("content", "hackernews")
                .post("/api/articles")
            .then()
                .statusCode(401)
                .and()
                .body(is("Expected top or new as parameter"));
    }

    @Test
    public void test_getArticleLinksEndpoint() throws Exception {

        RestAssured.defaultParser = Parser.JSON;

        BaseRequest request = new BaseRequest(null, "top");

        JsonNode json = objectMapper.convertValue(request, JsonNode.class);

        Response response = given()
                .when()
                    .contentType(ContentType.JSON)
                    .body(json)
                    .queryParam("content", "hackernews")
                    .post("/api/articles")
                .thenReturn();

        assertThat(response.jsonPath().getInt("statusCode")).isEqualTo(200);
        assertThat(response.jsonPath().getString("message")).isEqualTo("OK");

        List<LinkedHashMap> list = response.jsonPath().getList("data");
        JsonNode jsonNode = objectMapper.convertValue(list.get(0), JsonNode.class);

//        assertThat(list.size()).isEqualTo(24);
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
    public void test_getArticleCommentsEndpoint_throwsUnexpectedValueException_1() throws Exception {

        BaseRequest request = new BaseRequest("top",null);

        JsonNode json = objectMapper.convertValue(request, JsonNode.class);

        given()
            .when()
                .contentType(ContentType.JSON)
                .body(json)
                .queryParam("content", "hackernews")
                .post("/api/comments")
            .then()
                .statusCode(400)
                .and()
                .body(is("Given parameter is a string instead of a number"));
    }

    @Test
    public void test_getArticleCommentsEndpoint_throwsUnexpectedValueException_2() throws Exception {

        BaseRequest request = new BaseRequest("24576346", null);

        JsonNode json = objectMapper.convertValue(request, JsonNode.class);

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(json)
                .queryParam("content", "hackernews")
                .post("/api/comments")
                .then()
                .statusCode(400)
                .and()
                .body(is("Given parameter is an id of a comment"));
    }

    @Test
    public void test_getArticleCommentsEndpoint() throws Exception {

        RestAssured.defaultParser = Parser.JSON;

        BaseRequest request = new BaseRequest("24576266", null);

        JsonNode json = objectMapper.convertValue(request, JsonNode.class);

        Response response = given()
                .when()
                    .contentType(ContentType.JSON)
                    .body(json)
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
