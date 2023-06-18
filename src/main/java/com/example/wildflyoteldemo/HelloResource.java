package com.example.wildflyoteldemo;

import io.opentelemetry.api.trace.Span;
import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/")
@Slf4j
public class HelloResource {

    @Inject @RestClient RestApi rest;
    @Inject
    GraphQLApi graphql;

    @GraphQLClientApi(endpoint = "http://localhost:8080/graphql")
    public interface GraphQLApi {
        @Query String hello();
    }

    @RegisterRestClient(baseUri = "http://localhost:8080/rest")
    public interface RestApi {
        @GET
        @Path("/target")
        @Produces("text/plain")
        String target();
    }


    @GET
    @Path("/hello")
    @Produces("text/plain")
    public String hello() {
        Span.current().getSpanContext().getTraceId();
        log.info("### got REST hello in trace {}", Span.current().getSpanContext().getTraceId());
        return graphql.hello() + ", " + rest.target() + "!";
    }

    @GET
    @Path("/target")
    @Produces("text/plain")
    public String target() {
        Span.current().getSpanContext().getTraceId();
        log.info("### got REST target in trace {}", Span.current().getSpanContext().getTraceId());
        return "World";
    }
}
