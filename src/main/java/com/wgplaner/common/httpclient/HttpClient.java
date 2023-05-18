package com.wgplaner.common.httpclient;

import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class HttpClient {
    private static final WebClient webClient = WebClient.create();

    public static <T> T makeRequest(String uri, MultiValueMap<String, String> headers, String body, Class<T> responseType) {
        return webClient.post()
                .uri(uri)
                .headers(h-> h.addAll(headers))
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

}
