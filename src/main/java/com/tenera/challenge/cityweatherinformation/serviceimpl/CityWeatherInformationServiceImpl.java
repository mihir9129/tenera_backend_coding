package com.tenera.challenge.cityweatherinformation.serviceimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tenera.challenge.cityweatherinformation.entity.HistoricalInformationEntity;
import com.tenera.challenge.cityweatherinformation.repository.HistoricalInformationRepository;
import com.tenera.challenge.cityweatherinformation.response.CurrentWeatherInformation;
import com.tenera.challenge.cityweatherinformation.service.CityWeatherInformationService;
import com.tenera.challenge.cityweatherinformation.utils.ApplicationRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CityWeatherInformationServiceImpl implements CityWeatherInformationService {
    @Value("${openweathermap.current.api.url}")
    private String currentWeatherInformationApiUrl;

    @Value("${openweathermap.appid}")
    private String openWeatherMapAppId;

    @Value("${umbrella.weather.conditions}")
    String umbrellaWeatherConditions;

    @Autowired
    HistoricalInformationRepository historicalInformationRepository;

    @Autowired
    ApplicationRestTemplate applicationRestTemplate;

    public CurrentWeatherInformation getCurrentWeatherInformation(String city){
        log.info("Inside the current weather information service");

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("cityName",city);
        inputMap.put("appId",openWeatherMapAppId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        CurrentWeatherInformation currentWeatherInformation = new CurrentWeatherInformation();

        String url = currentWeatherInformationApiUrl + "?q=" + city + "&appid=" + openWeatherMapAppId;

        ObjectNode detailsFromOpenWeatherApi = applicationRestTemplate.submitRequest(url, HttpMethod.GET,httpEntity,inputMap);
        if(detailsFromOpenWeatherApi.get("success").asBoolean() && detailsFromOpenWeatherApi.get("data").get("cod").asInt() == 200) {

            JsonNode detailsFromOpenWeatherApiBody = detailsFromOpenWeatherApi.get("data");

            currentWeatherInformation.setTemperatureInCelsius(convertTempToCelcius(detailsFromOpenWeatherApiBody.get("main").get("temp").asDouble()));

            currentWeatherInformation.setPressureInHpa(detailsFromOpenWeatherApiBody.get("main").get("pressure").asInt());

            ArrayNode weatherInfo = (ArrayNode) detailsFromOpenWeatherApiBody.get("weather");

            currentWeatherInformation.setUmbrellaRequired(isUmbrellaRequired(weatherInfo));

            saveHistory(detailsFromOpenWeatherApiBody,weatherInfo);

        }
        return currentWeatherInformation;
    }

    public void saveHistory(JsonNode node,ArrayNode weatherInfo){
        HistoricalInformationEntity historicalInformationEntity = new HistoricalInformationEntity();

        historicalInformationEntity.setCityName(node.get("name").asText());
        historicalInformationEntity.setTempInCelsius(convertTempToCelcius(node.get("main").get("temp").asDouble()));
        historicalInformationEntity.setPressureInHpa(node.get("main").get("pressure").asInt());
        historicalInformationEntity.setUmbrellaRequired(isUmbrellaRequired(weatherInfo));

        historicalInformationRepository.save(historicalInformationEntity);
    }

    private double convertTempToCelcius(double temp){
        return temp - 273.15;
    }

    private boolean isUmbrellaRequired(ArrayNode weather){
        List<String> umbrellaConditions = Arrays.asList(umbrellaWeatherConditions.split(","));
        if(weather.size() > 0){
            List<String> isUmbrellaRequired = umbrellaConditions.stream()
                    .filter(u -> u.equalsIgnoreCase(weather.get(0).get("main").asText()))
                    .collect(Collectors.toList());
            return !isUmbrellaRequired.isEmpty();
        } else {
            //setting default to false, in case of no sizeweather present.
            return false;
        }
    }
}
