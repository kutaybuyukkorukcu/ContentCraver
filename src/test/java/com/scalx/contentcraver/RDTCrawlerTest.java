package com.scalx.contentcraver;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.RedirectionException;

@QuarkusTest
public class RDTCrawlerTest {

    @BeforeAll
    public void start() {

    }

//        https://www.reddit.com/r.json - class javax.ws.rs.RedirectionException
//        https://www.reddit.com/rasdlivestreamFail/.java - javax.ws.rs.NotFoundException
//        https://www.reddit.com/rasdlivestreamFail.json - javax.ws.rs.NotFoundException

    @Test
    public void test_RDTCrawler_whenSubredditIsNotPresent() {
        // RedirectionException
    }

    @Test
    public void test_RDTCrawler_whenTargetUrlIsNotPresent() {

    }
}
