package com.tenera.challenge.cityweatherinformation.service;

import com.tenera.challenge.cityweatherinformation.response.CurrentWeatherInformation;
import com.tenera.challenge.cityweatherinformation.response.HistoricalWeatherInformation;

public interface CityWeatherInformationService {

    CurrentWeatherInformation getCurrentWeatherInformation(String city);

    HistoricalWeatherInformation getHistoricalWeatherInformation(String city);
}
