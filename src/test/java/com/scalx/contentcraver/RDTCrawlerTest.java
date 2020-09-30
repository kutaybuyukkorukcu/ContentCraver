package com.scalx.contentcraver;

import com.scalx.contentcraver.exception.ThrowableRedirectionException;
import com.scalx.contentcraver.reddit.service.RDTCrawler;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.core.MediaType;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
public class RDTCrawlerTest {

    @Mock
    ResteasyClientBuilder mockClientBuilder;

    @Mock
    ResteasyClient mockClient;

    @InjectMocks
    RDTCrawler rdtCrawler;

//    https://www.reddit.com/r/java/comments/j2l/7_years_of_blogging_about_/.json

//    https://www.reddit.com/r.json - class javax.ws.rs.RedirectionException
//    https://www.reddit.com/rasdlivestreamFail/.java - javax.ws.rs.NotFoundException
//    https://www.reddit.com/rasdlivestreamFail.json - javax.ws.rs.NotFoundException
//    https://www.reddit.com/r/java/comments/j2l/7_years_of_blogging_about_/.json NotFoundException
//    https://www.reddit.com/r/javacomments/j2l1l7/7_years_of_blogging_about_java/ RedirectionException
//    https://www.reddit.comarticlelink.json/ get the expection

//    https://www.reddit.com/r/top.json private/forbidden throws 403
//    https://www.reddit.com/r/topa.json banned/not found throws 404

    @Test
    public void test_getArticleLinks_whenArticleTopicIsNotPresent() throws Exception {
        // RedirectionException

        String articleTopic = "mockdata";

        doThrow(new ThrowableRedirectionException())
                .when(mockClient).target("https://www.reddit.com/r/" + articleTopic + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);

        assertThrows(RedirectionException.class, () -> {
           rdtCrawler.getArticleLinks(articleTopic);
        });

        verify(mockClient).target("https://www.reddit.com/r/" + articleTopic + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        verifyNoMoreInteractions(rdtCrawler);
    }

    @Test
    public void test_getArticleLinks_whenTargetUrlIsNotPresent() throws Exception {

        String articleTopic = "%22%22";

        doThrow(new NotFoundException())
                .when(mockClient).target("https://www.reddit.com/r/" + articleTopic + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);

        assertThrows(NotFoundException.class, () -> {
            rdtCrawler.getArticleLinks(articleTopic);
        });

        verify(mockClient).target("https://www.reddit.com/r/" + articleTopic + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        verifyNoMoreInteractions(rdtCrawler);
    }

    @Test
    public void test_getArticleComments_whenArticleLinkIsNotPresent() throws Exception {

        String articleLink = "javacomments/j2l1l7/7_years_of_blogging_about_java/";

        doThrow(new ThrowableRedirectionException())
                .when(mockClient).target("https://www.reddit.com/r/" + articleLink + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);

        assertThrows(RedirectionException.class, () -> {
            rdtCrawler.getArticleLinks(articleLink);
        });

        verify(mockClient).target("https://www.reddit.com/r/" + articleLink + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        verifyNoMoreInteractions(rdtCrawler);
    }

    @Test
    public void test_getArticleComments_whenArticleTopicIsNotExpected() {

        String articleLink = "javacomments/j2l1l7/7_years_of_blogging_about_java/";

        doThrow(new NotFoundException())
                .when(mockClient).target("https://www.reddit.com/r/" + articleLink + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);

        assertThrows(NotFoundException.class, () -> {
            rdtCrawler.getArticleLinks(articleLink);
        });

        verify(mockClient).target("https://www.reddit.com/r/" + articleLink + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        verifyNoMoreInteractions(rdtCrawler);
    }


}
