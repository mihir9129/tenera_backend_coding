package com.tenera.challenge.cityweatherinformation.service;

import com.tenera.challenge.cityweatherinformation.response.CurrentWeatherInformation;

public interface CityWeatherInformationService {

    CurrentWeatherInformation getCurrentWeatherInformation(String city);
}
