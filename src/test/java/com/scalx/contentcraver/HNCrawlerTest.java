package com.scalx.contentcraver;

import com.sun.source.tree.ModuleTree;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;

import static org.mockito.Mockito.isA;
import java.util.concurrent.TimeUnit;

@QuarkusTest
public class HNCrawlerTest {

    @BeforeAll
    public void start() {
        ResteasyClientBuilder clientBuilder = Mockito.mock(ResteasyClientBuilder.class);
        ResteasyClient client = Mockito.mock(ResteasyClient.class);

        String mockJsonResponse = "";

        Mockito.when(clientBuilder
                    .connectionPoolSize(30)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build()
                ).thenReturn(client);

        Mockito.when(client
                    .target(isA(String.class))
                    .request(MediaType.APPLICATION_JSON)
                    .get(String.class)
                ).thenReturn(mockJsonResponse);

    }

    @Test
    public void test_HNCrawler_whenTargetUrlIsNotPresent() {
        //javax.ws.rs.ProcessingException -> target URI = ""
    }

    @Test
    public void test_HNCrawler_whenArticleTopicIsNotPresent() {
        //javax.ws.rs.NotAuthorizedException

    }

    @Test
    public void test_HNCrawler_whenMethodsArePresent() {

    }



}
