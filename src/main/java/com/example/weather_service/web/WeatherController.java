package com.example.weather_service.web;


import com.example.weather_service.service.GeocodingService;
import com.example.weather_service.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final GeocodingService geocodingService;
    private final WeatherService weatherService;

    public WeatherController(GeocodingService geocodingService, WeatherService weatherService) {
        this.geocodingService = geocodingService;
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public Mono<ResponseEntity<String>> getWeatherByCity(@PathVariable String city) {
        return geocodingService.getCoordinatesByCity(city)
                .flatMap(coordinates -> weatherService.getWeather(coordinates.getLatitude(), coordinates.getLongitude()))
                .map(weatherData -> ResponseEntity.ok(weatherData))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(500).body("Error fetching weather data")));
    }
}
