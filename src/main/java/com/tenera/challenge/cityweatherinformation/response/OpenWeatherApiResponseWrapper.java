package com.tenera.challenge.cityweatherinformation.response;

import lombok.Data;

@Data
public class OpenWeatherApiResponseWrapper {

    private OpenWeatherApiResponse data;
    private boolean success;
    private String error;

    public boolean getSuccess(){
        return this.success;
    }
}
