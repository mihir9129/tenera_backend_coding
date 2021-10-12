package com.tenera.challenge.cityweatherinformation.controllertest;

import org.h2.server.web.WebApp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CityWeatherInformationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("GET success - /api/v1/weather-information/current")
    void current_api_should_return_success() throws Exception{
        mockMvc.perform(get("/api/v1/weather-information/current?location=london"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("GET valid data - /api/v1/weather-information/current")
    void current_api_should_return_valid_data() throws Exception{
        mockMvc.perform(get("/api/v1/weather-information/current?location=london"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.temperatureInCelsius").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pressureInHpa").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.umbrellaRequired").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.cityName").value("London"));

    }

    @Test
    @DisplayName("GET for error - /api/v1/weather-information/current")
    void current_api_should_return_error() throws Exception{
        // there is no city named lond, so it would return error
        mockMvc.perform(get("/api/v1/weather-information/current?location=lond"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("GET success - /api/v1/weather-information/history")
    void history_api_should_return_success() throws Exception{
        mockMvc.perform(get("/api/v1/weather-information/history?location=london"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("GET valid data - /api/v1/weather-information/history")
    void history_api_should_return_valid_data() throws Exception{
        mockMvc.perform(get("/api/v1/weather-information/history?location=london"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.averageTempInCelsius").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.averagePressureInHpa").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.history").exists());
    }

    @Test
    @DisplayName("GET for error - /api/v1/weather-information/history")
    void history_api_should_return_error() throws Exception{
        // there is no city named lond, so it would return error
        mockMvc.perform(get("/api/v1/weather-information/history?location=lond"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("GET valid data with history information - /api/v1/weather-information/history")
    void history_api_should_return_proper_history_data() throws Exception{
        // calling the current api, which would store the data properly.
        current_api_should_return_valid_data();

        mockMvc.perform(get("/api/v1/weather-information/history?location=london"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.averageTempInCelsius").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.averagePressureInHpa").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.history").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.history[0].cityName").value("London"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.history[0].temperatureInCelsius").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.history[0].pressureInHpa").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.history[0].umbrellaRequired").exists());
    }
}
