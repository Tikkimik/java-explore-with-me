package ru.practicum.ewm.stats.clients;

import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, body);
    }

    protected <T> ResponseEntity<Object> get(String path, Map<String, Object> parameters) {
        return makeAndSendRequest(path, parameters);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(String path, Map<String, Object> parameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<T> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> statServerResponse;

        try {
            statServerResponse = rest.exchange(path, HttpMethod.GET, requestEntity, Object.class, parameters);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareStatResponse(statServerResponse);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);

        ResponseEntity<Object> serverResponse;
        try {
            serverResponse = rest.exchange(path, method, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareStatResponse(serverResponse);
    }

    private static ResponseEntity<Object> prepareStatResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful())
            return response;

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody())
            return responseBuilder.body(response.getBody());

        return responseBuilder.build();
    }
}
