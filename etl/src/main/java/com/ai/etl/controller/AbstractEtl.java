package com.ai.etl.controller;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class AbstractEtl {

    protected WebClient readWebClient = WebClient.create("http://localhost:8090");
    protected WebClient writeWebClient = WebClient.create("http://localhost:8080");

    protected final static MediaType json = MediaType.APPLICATION_JSON;
}
