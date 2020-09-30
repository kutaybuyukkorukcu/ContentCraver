package com.scalx.contentcraver;

import com.scalx.contentcraver.hackernews.service.HNCrawler;
import com.sun.source.tree.ModuleTree;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.*;

@QuarkusTest
public class HNCrawlerTest {

    // https://hacker-news.firebaseio.com/v0/item/articlejson test if this throws 302 redirect

    @Mock
    ResteasyClientBuilder mockClientBuilder;

    @Mock
    ResteasyClient mockClient;

    @InjectMocks
    HNCrawler hnCrawler;

    // getArticleComments() if the given articleLink parameter is not expected "null" is returned
    @BeforeAll
    public void start() {

//        when(clientBuilder
//                    .connectionPoolSize(isA(Integer.class))
//                    .connectTimeout(anyLong(), TimeUnit.SECONDS)
//                    .build()
//        ).thenReturn(client);

    }

    @Test
    public void test_getArticleLinks_whenTargetUrlIsNotPresent() throws Exception {
        //javax.ws.rs.ProcessingException -> target URI = ""

//        ResteasyClient mockClient = clientBuilder
//                .connectionPoolSize(30)
//                .connectTimeout(20, TimeUnit.SECONDS)
//                .build();

        // Check if any form of combination
        // https://hacker-news.firebaseio.com/v0/item/" + itemNumber.toString() + ".json
        // throws ProcessingException

        doThrow(new ProcessingException("Given target uri parameter is not present"))
                .when(mockClient).target("").request(MediaType.APPLICATION_JSON).get(String.class);

        assertThrows(ProcessingException.class, () -> {
                hnCrawler.getArticleLinks("");
        });

        verify(mockClient).target("").request(MediaType.APPLICATION_JSON).get(String.class);
        verifyNoMoreInteractions(hnCrawler);
    }

    @Test()
    public void test_getArticleLinks_whenArticleTopicIsNotPresent() throws Exception {
        //javax.ws.rs.NotAuthorizedException

        String articleTopic = "top";

//        ResteasyClient mockClient = clientBuilder
//                .connectionPoolSize(30)
//                .connectTimeout(20, TimeUnit.SECONDS)
//                .build();

        // https://hacker-news.firebaseio.com/v0" + articleTopic + ".json -> Given uri missing "/" after /v0.
        doThrow(new NotAuthorizedException("Given article topic parameter is not present"))
                .when(mockClient).target("https://hacker-news.firebaseio.com/v0" + articleTopic + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);

        assertThrows(NotAuthorizedException.class, () -> {
            hnCrawler.getArticleLinks(articleTopic);
        });

        verify(mockClient).target("https://hacker-news.firebaseio.com/v0" + articleTopic + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        verifyNoMoreInteractions(hnCrawler);
    }

    @Test
    public void test_getArticleLinks_whenMethodsArePresent() throws Exception {

        String articleTopic = "top";

        String mockJsonResponse = "[ 24607819, 24607452, 24619348 ]";

        when(mockClient
                .target("https://hacker-news.firebaseio.com/v0/" + articleTopic + "stories.json")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class)
        ).thenReturn(mockJsonResponse);

        List<BaseCard> articleList = Arrays.asList(
                new BaseCard("24607819",
                        "Poshmark Filed to Go Public",
                        "Development",
                        "https://www.bloomberg.com/news/articles/2020-09-25/used-clothing-platform-poshmark-says-it-filed-to-go-public",
                        "prostoalex",
                        "",
                        43,
                        2,
                        1601223957),
                new BaseCard("24607452",
                        "Show HN: Instant SVG icon search with over 50K+ icons indexed",
                        "Development",
                        "https://iconsear.ch/search.html",
                        "Fileformat",
                        "",
                        52,
                        5,
                        1601221309),
                new BaseCard("24619348",
                        "Show HN: Bring your desktop-based engineering simulation results, Models in VR",
                        "Development",
                        "http://visulity.com",
                        "bhargav1195",
                        "",
                        8,
                        1,
                        1601315431)
        );

        List<BaseCard> expectedArticleList = hnCrawler.getArticleLinks(articleTopic);

        assertThat(articleList).isEqualTo(expectedArticleList);

        verify(mockClient).target("https://hacker-news.firebaseio.com/v0/" + articleTopic + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        verifyNoMoreInteractions(hnCrawler);
    }

    @Test
    public void test_getArticleComments_whenArticleLinkIsNotPresent() throws Exception {

        String articleLink = "24619348";

        // https://hacker-news.firebaseio.com/v0/item" + articleTopic + ".json -> Given uri missing "/" after /item.
        doThrow(new NotAuthorizedException("Given article topic parameter is not present"))
                .when(mockClient).target("https://hacker-news.firebaseio.com/v0/item" + articleLink + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);

        assertThrows(NotAuthorizedException.class, () -> {
            hnCrawler.getArticleComments(articleLink);
        });

        verify(mockClient).target("https://hacker-news.firebaseio.com/v0/item" + articleLink + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        verifyNoMoreInteractions(hnCrawler);

    }

    @Test
    public void test_getArticleComments_whenArticleLinkIsNotExpected() throws Exception {

        // If the given articleLink is a word, the endpoint returns text/plain "null"
        String articleLink = "article";

        String mockJsonResponse = "null";

        when(mockClient
                .target("https://hacker-news.firebaseio.com/v0/item" + articleLink + ".json")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class)
        ).thenReturn(mockJsonResponse);

        assertThrows(NullPointerException.class, () -> {
            hnCrawler.getArticleComments(articleLink);
        });

        verify(mockClient).target("https://hacker-news.firebaseio.com/v0/item" + articleLink + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        verifyNoMoreInteractions(hnCrawler);
    }

    @Test
    public void test_getArticleComments_whenMethodsArePresent() throws Exception {

        String articleLink = "24607452";

        String mockJsonResponse = "{\n" +
                "  \"by\": \"sshahone\",\n" +
                "  \"descendants\": 5,\n" +
                "  \"id\": 24607452,\n" +
                "  \"kids\": [\n" +
                "    24607453\n" +
                "  ],\n" +
                "  \"score\": 52,\n" +
                "  \"time\": 1601221309,\n" +
                "  \"title\": \"Show HN: Instant SVG icon search with over 50K+ icons indexed\",\n" +
                "  \"type\": \"story\"\n" +
                "  \"url\": \"https://iconsear.ch/search.html\"\n" +
                "}";

        when(mockClient
                .target("https://hacker-news.firebaseio.com/v0/item/" + articleLink + ".json")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class)
        ).thenReturn(mockJsonResponse);

        List<BaseComment> commentList = Arrays.asList(
                new BaseComment(
                        "24607452",
                        "24607453",
                        "My original side project was a logo search website [1], but I realized that it would work for other " +
                        "types of images besides logos.  And I had a lot of bookmarks to SVG icons repositories.  And I found a " +
                        "great domain name (seriously: are the domainers asleep?).  So allow me to introduce IconSear.ch!<p>I " +
                        "wrote it in NodeJS&#x2F;Koa with server-side full-text search using Lunr.js, running on Google CloudRun." +
                        " Source and full list of credits: [2]<p>The index of icons is in a separate repo [3].  Contributions " +
                        "welcome!  Right now, all the sources are on github or gitlab, but it could support any website that can " +
                        "publish in the index format and allows hotlinking.<p>I know there are a ton of almost identical icon " +
                        "search websites (my list: [4]), and while mine is infinitely superior and perfect, I&#x27;m still " +
                        "interested in what you think!<p>[1] <a href=\"https:&#x2F;&#x2F;logosear.ch&#x2F;search.html\" " +
                        "rel=\"nofollow\">https:&#x2F;&#x2F;logosear.ch&#x2F;search.html</a><p>[2] <a href=\"https:&#x2F;" +
                        "&#x2F;github.com&#x2F;VectorLogoZone&#x2F;logosearch#credits\" rel=\"nofollow\">https:&#x2F;&#x2F;" +
                        "github.com&#x2F;VectorLogoZone&#x2F;logosearch#credits</a><p>[3] <a href=\"https:&#x2F;&#x2F;" +
                        "github.com&#x2F;VectorLogoZone&#x2F;git-svg-icons\" rel=\"nofollow\">https:&#x2F;&#x2F;" +
                        "github.com&#x2F;VectorLogoZone&#x2F;git-svg-icons</a><p>[4] <a href=\"https:&#x2F;&#x2F;" +
                        "iconsear.ch&#x2F;alternatives&#x2F;index.html\" rel=\"nofollow\">https:&#x2F;&#x2F;iconsear.ch&#x2F;" +
                        "alternatives&#x2F;index.html</a>",
                        "Fileformat",
                        "24607452",
                        1601221309
                ),
                new BaseComment(
                        "24607452",
                        "24614240",
                        "I really like this project, so thanks for making it!<p>One improvement that I can think of" +
                            " is to be able to see the license the icon is released under next to the icon. " +
                            "Another is to filter by available license types.",
                        "tareqak",
                        "24607453",
                        1601280995
                ),
                new BaseComment(
                        "24607452",
                        "24615797",
                        "I can see why he’d rather not have to deal with the licensing information (pretty soon you" +
                                " want an UI to decipher the alphabet-soup of CCGPLBSD&#x2F;NSA&#x2F;NZDAP-3.2xp...), " +
                                "but yeah, it would be nice. Iconfinder has that sort of thing and it speeds up matters" +
                                " a lot.",
                        "toyg",
                        "24614240",
                        1601295572
                ),
                new BaseComment(
                        "24607452",
                        "24617078",
                        "I think this is doable, at least for repos on Github, since they have an API to get the " +
                            "license for a repo:<p><a href=\"https:&#x2F;&#x2F;developer.github.com&#x2F;v3&#x2F;" +
                            "licenses&#x2F;#get-the-license-for-a-repository\" rel=\"nofollow\">https:&#x2F;&#x2F;" +
                            "developer.github.com&#x2F;v3&#x2F;licenses&#x2F;#get-the-license-fo...</a>",
                        "Fileformat",
                        "24614240",
                        1601303931
                ),
                new BaseComment(
                        "24607452",
                        "24616843",
                        "I missed your Show HN a few months ago for the Logo Search. That thing is awesome!" +
                            "<p>Most of the ones I&#x27;ve come across in the past are great for finding logos for " +
                            "companies in the tech-sphere, but fall short outside of that. This one seems to have " +
                            "a whole lot more comprehensive coverage.",
                        "cosmie",
                        "24607453",
                        1601302779
                )
        );

        List<BaseComment> expectedCommentList = hnCrawler.getArticleComments(articleLink);

        assertThat(commentList).isEqualTo(expectedCommentList);

        verify(mockClient).target("https://hacker-news.firebaseio.com/v0/item/" + articleLink + ".json")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        verifyNoMoreInteractions(hnCrawler);
    }
}
