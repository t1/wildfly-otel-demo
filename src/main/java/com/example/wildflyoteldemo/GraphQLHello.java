package com.example.wildflyoteldemo;

import io.opentelemetry.api.trace.Span;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

@GraphQLApi
@Slf4j
public class GraphQLHello {
    @Query public String hello() {
        log.info("### got GraphQL hello in trace {}", Span.current().getSpanContext().getTraceId());
        return "Hello";
    }
}
