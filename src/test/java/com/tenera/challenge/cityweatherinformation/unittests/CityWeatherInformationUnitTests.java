package com.tenera.challenge.cityweatherinformation.unittests;

import com.tenera.challenge.cityweatherinformation.response.CurrentWeatherInformation;
import com.tenera.challenge.cityweatherinformation.response.HistoricalWeatherInformation;
import com.tenera.challenge.cityweatherinformation.response.Weather;
import com.tenera.challenge.cityweatherinformation.serviceimpl.CityWeatherInformationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CityWeatherInformationUnitTests {

    @Autowired
    CityWeatherInformationServiceImpl cityWeatherInformationServiceImpl;

    @Test
    void checkTemperature(){
        double outputTemp = cityWeatherInformationServiceImpl.convertTempToCelsius(273.15d);
        assertEquals(0.0d,outputTemp);
    }

    @Test
    void checkUmbrellaRequired(){
        List<Weather> weatherList1 = getWeatherList1();
        boolean umbrellaRequired = cityWeatherInformationServiceImpl.isUmbrellaRequired(weatherList1);
        assertFalse(umbrellaRequired);

        List<Weather> weatherList2 = getWeatherList2();
        boolean umbrellaRequired2 = cityWeatherInformationServiceImpl.isUmbrellaRequired(weatherList2);
        assertTrue(umbrellaRequired2);

        List<Weather> weatherList3 = getWeatherList3();
        boolean umbrellaRequired3 = cityWeatherInformationServiceImpl.isUmbrellaRequired(weatherList3);
        assertFalse(umbrellaRequired3);

        List<Weather> weatherList4 = getWeatherList4();
        boolean umbrellaRequired4 = cityWeatherInformationServiceImpl.isUmbrellaRequired(weatherList4);
        assertTrue(umbrellaRequired4);
    }

    @Test
    void testCurrentWeatherInformation(){
        CurrentWeatherInformation currentWeatherInformation = cityWeatherInformationServiceImpl.getCurrentWeatherInformation("berlin");
        assertNotNull(currentWeatherInformation);
        assertNotNull(currentWeatherInformation.getCityName());
        assertEquals("Berlin",currentWeatherInformation.getCityName());
        assertNotNull(currentWeatherInformation.getTemperatureInCelsius());
    }

    @Test
    void testHistoryWeatherInformation(){
        testCurrentWeatherInformation();
        HistoricalWeatherInformation historicalWeatherInformation = cityWeatherInformationServiceImpl.getHistoricalWeatherInformation("berlin");
        assertNotNull(historicalWeatherInformation);
        assertNotNull(historicalWeatherInformation.getAverageTempInCelsius());
        assertNotNull(historicalWeatherInformation.getAveragePressureInHpa());
        assertEquals("Berlin",historicalWeatherInformation.getHistory().get(0).getCityName());
        assertNotNull(historicalWeatherInformation.getHistory());
    }

    @Test
    void testAverageTempInCelsius(){
        CurrentWeatherInformation currentWeatherInformationLondon = cityWeatherInformationServiceImpl.getCurrentWeatherInformation("london");
        CurrentWeatherInformation currentWeatherInformationBerlin = cityWeatherInformationServiceImpl.getCurrentWeatherInformation("berlin");
        List<CurrentWeatherInformation> currentWeatherInformationList = Arrays.asList(currentWeatherInformationBerlin,currentWeatherInformationLondon);
        double averageTemp = cityWeatherInformationServiceImpl.getAverageTemperature(currentWeatherInformationList);
        assertNotNull(averageTemp);
    }

    @Test
    void testAveragePressure(){
        CurrentWeatherInformation currentWeatherInformationLondon = cityWeatherInformationServiceImpl.getCurrentWeatherInformation("london");
        CurrentWeatherInformation currentWeatherInformationBerlin = cityWeatherInformationServiceImpl.getCurrentWeatherInformation("berlin");
        List<CurrentWeatherInformation> currentWeatherInformationList = Arrays.asList(currentWeatherInformationBerlin,currentWeatherInformationLondon);
        double averagePressure = cityWeatherInformationServiceImpl.getAveragePressure(currentWeatherInformationList);
        assertTrue(averagePressure > 0.0d);
    }

    private List<Weather> getWeatherList1(){
        return new ArrayList<>();
    }

    private List<Weather> getWeatherList2(){
        return new ArrayList<>(Arrays.asList(
                new Weather(100,"Clouds","few clouds","02d"),
                new Weather(101,"Thunderstorm","few clouds","02d")
        ));
    }

    private List<Weather> getWeatherList3(){
        return new ArrayList<>(Arrays.asList(
                new Weather(100,"Clouds","few clouds","02d"),
                new Weather(101,"Thunder","few clouds","02d")
        ));
    }

    private List<Weather> getWeatherList4(){
        return new ArrayList<>(Arrays.asList(
                new Weather(100,"Clouds","few clouds","02d"),
                new Weather(101,"Thunder","thunder clouds","03d"),
                new Weather(102,"Clear","Clear sky","04d"),
                new Weather(103,"Drizzle","Drizzle clouds","05d")
        ));
    }

}


