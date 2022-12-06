package ru.practicum.ewm.stats.clients;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.exceptions.IncorrectParameterException;
import ru.practicum.ewm.stats.hit.Stat;

import java.util.Arrays;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, body);
    }

    protected <T> ResponseEntity<Stat[]> get(String path, Map<String, Object> parameters) {
        return makeAndSendRequest(path, parameters);
    }

    private <T> ResponseEntity<Stat[]> makeAndSendRequest(String path, Map<String, Object> parameters) {
        ResponseEntity<Stat[]> response;

        try {
            response = rest.getForEntity(path, Stat[].class, parameters);
        } catch (HttpStatusCodeException e) {
            throw new IncorrectParameterException("sdf");
        }
        System.out.println(Arrays.toString(response.getBody()));
        return asd(response);
    }

    private static ResponseEntity<Stat[]> asd(ResponseEntity<Stat[]> response) {
        if (response.getStatusCode().is2xxSuccessful())
            return response;

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody())
            return responseBuilder.body(response.getBody());

        return responseBuilder.build();
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
