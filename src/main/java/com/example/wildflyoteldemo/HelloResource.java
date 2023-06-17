package com.example.wildflyoteldemo;

import io.opentelemetry.api.trace.Span;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import lombok.extern.slf4j.Slf4j;

@Path("/")
@Slf4j
public class HelloResource {
    @GET
    @Path("/hello")
    @Produces("text/plain")
    public String hello() {
        log.info("### got hello in trace {}", Span.current().getSpanContext().getTraceId());
        return "Hello, World!";
    }
}
