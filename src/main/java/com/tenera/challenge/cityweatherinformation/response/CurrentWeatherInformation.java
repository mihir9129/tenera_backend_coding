package com.tenera.challenge.cityweatherinformation.response;

import lombok.Data;

@Data
public class CurrentWeatherInformation {
    private double temperatureInCelsius;
    private int pressureInHpa;
    private boolean isUmbrellaRequired;
}
