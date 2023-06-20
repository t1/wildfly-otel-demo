package com.example.wildflyoteldemo;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
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
        log.info("### got REST hello in trace {}", Span.current().getSpanContext().getTraceId());
        // io.opentelemetry.api.logs.Logger customAppenderLogger =
        //     GlobalOpenTelemetry.get().getLogsBridge().get("custom-log-appender");
        // customAppenderLogger
        //     .logRecordBuilder()
        //     .setSeverity(Severity.INFO)
        //     .setBody("A log message from a custom appender without a span")
        //     .setAttribute(AttributeKey.stringKey("key"), "value")
        //     .emit();
        var greeting = graphql.hello();
        var greetee = rest.target();
        Span.current().addEvent("greet", Attributes.of(
            AttributeKey.stringKey("greeting"), greeting,
            AttributeKey.stringKey("greetee"), greetee));
        return greeting + ", " + greetee + "!";
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
