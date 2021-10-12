package com.tenera.challenge.cityweatherinformation.response;

import lombok.Data;

import java.util.List;

@Data
public class OpenWeatherApiResponse {

   private int timezone;
   private int id;
   private String name;
   private int cod;
   private int dt;
   private int visibility;
   private String base;
   private Coordinates coord;
   private List<Weather> weather;
   private MainBody main;
   private Wind wind;
   private Clouds clouds;
   private System sys;
   private String message;
   private boolean success;
}
