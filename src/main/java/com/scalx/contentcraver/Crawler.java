package com.scalx.contentcraver;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.concurrent.TimeUnit;

@Named
@ApplicationScoped
public class Crawler {

    public final ResteasyClient CLIENT = new ResteasyClientBuilderImpl()
            .connectionPoolSize(30)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build();

}
