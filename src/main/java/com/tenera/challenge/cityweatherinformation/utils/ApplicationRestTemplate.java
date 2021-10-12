package com.tenera.challenge.cityweatherinformation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tenera.challenge.cityweatherinformation.response.OpenWeatherApiResponse;
import com.tenera.challenge.cityweatherinformation.response.OpenWeatherApiResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class ApplicationRestTemplate {

    RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private ObjectMapper objectMapper;

    public OpenWeatherApiResponse submitRequest(String url, HttpMethod httpMethod, HttpEntity httpEntity, Map<String, Object> map) {
        log.info("Entered into SubmitRequest method URL: " + url + " Http Method:" + httpMethod.name() + " Headers:" + httpEntity.getHeaders() + "Request Body:" + httpEntity.getBody()
        );
        OpenWeatherApiResponse openWeatherApiResponse = new OpenWeatherApiResponse();
        ResponseEntity<OpenWeatherApiResponse> responseEntity =null;

        try {
            responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, OpenWeatherApiResponse.class, map);
            openWeatherApiResponse = responseEntity.getBody();
            openWeatherApiResponse.setSuccess(Boolean.TRUE);
            log.info("Exit from SubmitRequest with Response: " + responseEntity + " TimeStamp:");
            return openWeatherApiResponse;
        } catch (RestClientException e) {
            openWeatherApiResponse.setMessage(Objects.isNull(responseEntity.getBody()) ? responseEntity.getBody().getMessage() : "Internal server error");
            openWeatherApiResponse.setSuccess(Boolean.FALSE);
            log.error("RestClientException in SubmitRequest " + e.getMessage(), e);
            return openWeatherApiResponse;

        }

    }
}
