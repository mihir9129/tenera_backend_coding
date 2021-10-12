package com.tenera.challenge.cityweatherinformation.response;

import lombok.Data;

import java.util.List;

@Data
public class HistoricalWeatherInformation {

    private double averageTempInCelsius;
    private double averagePressureInHpa;
    private List<CurrentWeatherInformation> history;
}
