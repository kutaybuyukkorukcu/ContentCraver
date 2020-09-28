package com.scalx.contentcraver;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.concurrent.TimeUnit;

@Named
@ApplicationScoped
public class Crawler {

    public final ResteasyClient client = new ResteasyClientBuilderImpl()
            .connectionPoolSize(20)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build();
}
