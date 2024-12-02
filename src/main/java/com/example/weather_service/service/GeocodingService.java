package com.example.weather_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GeocodingService {

    private final WebClient webClient;

    public GeocodingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://nominatim.openstreetmap.org").build();
    }

    public Mono<Coordinates> getCoordinatesByCity(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", city)
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToFlux(Coordinates.class) // Handle array response
                .next(); // Fetch the first result
    }

    public static class Coordinates {
        private String lat;
        private String lon;

        public double getLatitude() {
            return Double.parseDouble(lat);
        }

        public double getLongitude() {
            return Double.parseDouble(lon);
        }

        // Getters and Setters
        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }
    }
}
