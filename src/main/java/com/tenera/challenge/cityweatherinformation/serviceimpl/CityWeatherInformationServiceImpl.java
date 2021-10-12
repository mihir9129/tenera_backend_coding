package com.tenera.challenge.cityweatherinformation.serviceimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tenera.challenge.cityweatherinformation.entity.HistoricalInformationEntity;
import com.tenera.challenge.cityweatherinformation.exception.ResourceNotFoundException;
import com.tenera.challenge.cityweatherinformation.repository.HistoricalInformationRepository;
import com.tenera.challenge.cityweatherinformation.response.*;
import com.tenera.challenge.cityweatherinformation.service.CityWeatherInformationService;
import com.tenera.challenge.cityweatherinformation.utils.ApplicationRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CityWeatherInformationServiceImpl implements CityWeatherInformationService {
    @Value("${openweathermap.current.api.url}")
    private String currentWeatherInformationApiUrl;

    @Value("${openweathermap.appid}")
    private String openWeatherMapAppId;
    //kept the information in the properties file, so can be updated easily as per requirements.
    @Value("${umbrella.weather.conditions}")
    String umbrellaWeatherConditions;

    @Autowired
    HistoricalInformationRepository historicalInformationRepository;

    @Autowired
    ApplicationRestTemplate applicationRestTemplate;
    final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");

    public CurrentWeatherInformation getCurrentWeatherInformation(String city){
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        log.info("Inside the current weather information service at time ::" + sdf.format(new Date()));


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        CurrentWeatherInformation currentWeatherInformation = new CurrentWeatherInformation();

        String url = currentWeatherInformationApiUrl + "?q=" + city + "&appid=" + openWeatherMapAppId;

        OpenWeatherApiResponse openWeatherApiResponseWrapper = applicationRestTemplate.submitRequest(url, HttpMethod.GET,httpEntity,new HashMap<>());
        if(openWeatherApiResponseWrapper.getCod() == HttpStatus.OK.value()) {

            OpenWeatherApiResponse detailsFromOpenWeatherApiBody = openWeatherApiResponseWrapper;

            currentWeatherInformation.setTemperatureInCelsius(convertTempToCelsius(detailsFromOpenWeatherApiBody.getMain().getTemp()));

            currentWeatherInformation.setPressureInHpa(detailsFromOpenWeatherApiBody.getMain().getPressure());

            List<Weather> weatherInfo = detailsFromOpenWeatherApiBody.getWeather();

            currentWeatherInformation.setUmbrellaRequired(isUmbrellaRequired(weatherInfo));

            currentWeatherInformation.setCityName(openWeatherApiResponseWrapper.getName());

            saveHistory(detailsFromOpenWeatherApiBody,weatherInfo);

        } else if(openWeatherApiResponseWrapper.getCod() == HttpStatus.NOT_FOUND.value()){
            throw new ResourceNotFoundException(openWeatherApiResponseWrapper.getMessage());
        }

        log.info("Exiting from the current weather information service at timestamp ::" + sdf.format(new Date()));
        return currentWeatherInformation;
    }

    public void saveHistory(OpenWeatherApiResponse node,List<Weather> weatherInfo){
        HistoricalInformationEntity historicalInformationEntity = new HistoricalInformationEntity();

        historicalInformationEntity.setCityName(node.getName());
        historicalInformationEntity.setTempInCelsius(convertTempToCelsius(node.getMain().getTemp()));
        historicalInformationEntity.setPressureInHpa(node.getMain().getPressure());
        historicalInformationEntity.setUmbrellaRequired(isUmbrellaRequired(weatherInfo));

        historicalInformationRepository.save(historicalInformationEntity);
    }

    public double convertTempToCelsius(double temp){
        return temp - 273.15;
    }

    public boolean isUmbrellaRequired(List<Weather> weather){
        List<String> umbrellaConditions = Arrays.asList(umbrellaWeatherConditions.split(","));
        if(!weather.isEmpty()){
            List<Weather> isUmbrellaRequired = weather.stream()
                    .filter(w -> umbrellaConditions.contains(w.getMain()))
                    .collect(Collectors.toList());
            /*check for all the weather condition, and verify if any weather condition matches the condition for
            rain in the city.*/
            return !isUmbrellaRequired.isEmpty();
        } else {
            //setting default to false, in case of size of weather data is size 0.
            return false;
        }
    }

    public HistoricalWeatherInformation getHistoricalWeatherInformation(String city){
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        log.info("Inside the historical weather information service at timestamp ::" + sdf.format(new Date()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        HistoricalWeatherInformation historicalWeatherInformation = new HistoricalWeatherInformation();

        String url = currentWeatherInformationApiUrl + "?q=" + city + "&appid=" + openWeatherMapAppId;

        OpenWeatherApiResponse openWeatherApiResponseWrapper = applicationRestTemplate.submitRequest(url, HttpMethod.GET,httpEntity,new HashMap<>());

        if(openWeatherApiResponseWrapper.getCod() == HttpStatus.OK.value()){
            //getting the actual city name.
            String actualCityName = openWeatherApiResponseWrapper.getName();
            List<HistoricalInformationEntity> historicalInformationEntities = historicalInformationRepository.findByCityNameOrderByCreatedAtDesc(actualCityName);

            List<CurrentWeatherInformation> currentWeatherInformationList = new ArrayList<>();

            AtomicInteger i = new AtomicInteger(1);

            for(HistoricalInformationEntity historicalInformationEntity: historicalInformationEntities){
                if(i.get() <= 5){
                    //getting only the latest 5 data for the same city as required, so keeping the static value.

                    CurrentWeatherInformation currentWeatherInformation = new CurrentWeatherInformation();
                    currentWeatherInformation.setTemperatureInCelsius(historicalInformationEntity.getTempInCelsius());
                    currentWeatherInformation.setUmbrellaRequired(historicalInformationEntity.getIsUmbrellaRequired());
                    currentWeatherInformation.setPressureInHpa(historicalInformationEntity.getPressureInHpa());
                    currentWeatherInformation.setCityName(historicalInformationEntity.getCityName());

                    currentWeatherInformationList.add(currentWeatherInformation);
                    i.incrementAndGet();
                } else {
                    break;
                }

            }

            historicalWeatherInformation.setAverageTempInCelsius(getAverageTemperature(currentWeatherInformationList));
            historicalWeatherInformation.setAveragePressureInHpa(getAveragePressure(currentWeatherInformationList));
            historicalWeatherInformation.setHistory(currentWeatherInformationList);
        } else if(openWeatherApiResponseWrapper.getCod() == HttpStatus.NOT_FOUND.value()){
            throw new ResourceNotFoundException(openWeatherApiResponseWrapper.getMessage());
        }
        log.info("Exiting the historical weather information service at timestamp ::" + sdf.format(new Date()));

        return historicalWeatherInformation;
    }

    public double getAverageTemperature(List<CurrentWeatherInformation> currentWeatherInformationList){
        return currentWeatherInformationList.stream().map(CurrentWeatherInformation::getTemperatureInCelsius)
                .mapToDouble(current -> current).average().orElse(0.0d);
    }

    public double getAveragePressure(List<CurrentWeatherInformation> currentWeatherInformationList){
        return currentWeatherInformationList.stream().map(CurrentWeatherInformation::getPressureInHpa)
                .mapToDouble(current -> current).average().orElse(0.0d);
    }
}
