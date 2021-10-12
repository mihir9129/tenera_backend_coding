package com.tenera.challenge.cityweatherinformation.controller;

import com.tenera.challenge.cityweatherinformation.exception.ResourceNotFoundException;
import com.tenera.challenge.cityweatherinformation.response.ApiResponse;
import com.tenera.challenge.cityweatherinformation.response.CurrentWeatherInformation;
import com.tenera.challenge.cityweatherinformation.response.HistoricalWeatherInformation;
import com.tenera.challenge.cityweatherinformation.service.CityWeatherInformationService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value="/api/v1/weather-information",produces = MediaType.APPLICATION_JSON_VALUE)
public class CityWeatherInformationController {
    @Autowired
    CityWeatherInformationService cityWeatherInformationService;

    @GetMapping(value = "/current")
    public ResponseEntity<ApiResponse<CurrentWeatherInformation>> getCurrentWeatherInfoBasedOnCity(
            @NonNull  @RequestParam("location") String city){

        log.info("inside the current weather information controller");
        ApiResponse<CurrentWeatherInformation> apiResponse = new ApiResponse<>();

        try{
            CurrentWeatherInformation currentWeatherInformation = cityWeatherInformationService.getCurrentWeatherInformation(city);
            apiResponse.setResponse(currentWeatherInformation);
            apiResponse.setSuccess(Boolean.TRUE);
            apiResponse.setMessage("Details fetched successfully");
            return ResponseEntity.ok().body(apiResponse);

        } catch (ResourceNotFoundException e){
            apiResponse.setResponse(null);
            apiResponse.setMessage(e.getMessage());
            apiResponse.setSuccess(Boolean.FALSE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
        catch(Exception e){

            apiResponse.setResponse(null);
            apiResponse.setMessage("Error in fetching data, kindly check the city name");
            apiResponse.setSuccess(Boolean.FALSE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

        }

    }

    @GetMapping(value = "history")
    public ResponseEntity<ApiResponse<HistoricalWeatherInformation>> getHistoricalWeatherInfoBasedOnCity(
            @NonNull @RequestParam("location") String city){

        log.info("inside the historical weather information controller");
        ApiResponse<HistoricalWeatherInformation> apiResponse = new ApiResponse<>();
        if(city.trim().equalsIgnoreCase("null")){
            apiResponse.setResponse(null);
            apiResponse.setMessage("City name is not proper, kindly provide correct city name");
            apiResponse.setSuccess(Boolean.FALSE);

            log.info("City name is not proper");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
        try{
            HistoricalWeatherInformation historicalWeatherInformation = cityWeatherInformationService.getHistoricalWeatherInformation(city);
            apiResponse.setResponse(historicalWeatherInformation);
            apiResponse.setSuccess(Boolean.TRUE);
            apiResponse.setMessage("Details fetched successfully");

            log.info("Exiting the historical weather information controller successfully");
            return ResponseEntity.ok().body(apiResponse);

        } catch(Exception e){

            apiResponse.setResponse(null);
            apiResponse.setMessage("Error in fetching data");
            apiResponse.setSuccess(Boolean.FALSE);

            log.info("Exiting the historical weather information controller successfully");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

        }
    }
}
