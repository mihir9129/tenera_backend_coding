package com.tenera.challenge.cityweatherinformation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

@Component
@Slf4j
public class ApplicationRestTemplate {

    RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private ObjectMapper objectMapper;

    public ObjectNode submitRequest(String url, HttpMethod httpMethod, HttpEntity httpEntity, Map<String, Object> map) {
        log.info("Entered into SubmitRequest method URL: " + url + " Http Method:" + httpMethod.name() + " Headers:" + httpEntity.getHeaders() + "Request Body:" + httpEntity.getBody()
        );
        ObjectNode resultNode = JsonNodeFactory.instance.objectNode();
        try {
            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, JsonNode.class, map);
            JsonNode responseJsonNode = responseEntity.getBody();
            resultNode.put("success", true);
            resultNode.set("data", responseJsonNode);
            log.info("RestTemplate Response Status:" + responseEntity.getStatusCode() + ", Response Body:" + responseJsonNode + " TimeStamp:" );
        } catch (RestClientException e) {
            resultNode.put("success", false);
            resultNode.put("error", e.getMessage());
            log.error("RestClientException in SubmitRequest " + e.getMessage(), e);
        } catch (Exception e) {
            resultNode.put("success", false);
            resultNode.put("error", e.getMessage());
            log.error("UnknownException in SubmitRequest " + e.getMessage(), e);
        }
        log.info("Exit from SubmitRequest with Response: " + resultNode + " TimeStamp:" );
        return resultNode;
    }
}
