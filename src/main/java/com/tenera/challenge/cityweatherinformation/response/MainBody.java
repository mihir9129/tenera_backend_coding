package com.tenera.challenge.cityweatherinformation.response;

import lombok.Data;

@Data
public class MainBody {

    private double temp;
    private double feels_like;
    private double temp_min;
    private double temp_max;
    private int pressure;
    private int humidity;

}
