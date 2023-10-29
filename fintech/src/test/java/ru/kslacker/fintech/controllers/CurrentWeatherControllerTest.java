package ru.kslacker.fintech.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.kslacker.fintech.dataaccess.repositories.api.CityRepository;
import ru.kslacker.fintech.dto.FullWeatherInfoDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        properties = {
                "resilience4j.ratelimiter.instances.remote-weather-service.limitForPeriod=5",
                "resilience4j.ratelimiter.instances.remote-weather-service.limitRefreshPeriod=1s",
                "resilience4j.ratelimiter.instances.remote-weather-service.timeoutDuration=250ms"
        }
)
@AutoConfigureMockMvc
@Testcontainers
public class CurrentWeatherControllerTest extends TestContainersH2Test {
    private static final String API_CURRENT_WEATHER = "/api/weather/current";
    private static final String CITY_NAME = "Moscow";
    private static final int RATELIMITER_RPS = 5;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CityRepository cityRepository;

    @Test
    public void getCurrentWeather_exceedingRps_isTooManyRequests() {
        Map<Integer, Integer> responseStatusCount = new ConcurrentHashMap<>(RATELIMITER_RPS * 2);

        IntStream.rangeClosed(0, RATELIMITER_RPS * 2)
                .parallel()
                .forEach(i -> {
                    try {
                        MvcResult result = performGetRequest(CITY_NAME)
                                .andReturn();

                        int statusCode = result.getResponse().getStatus();
                        responseStatusCount.put(statusCode, responseStatusCount.getOrDefault(statusCode, 0) + 1);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        assertAll(
                () -> assertThat(responseStatusCount.keySet().size()).isEqualTo(2),
                () -> assertThat(responseStatusCount.keySet()).contains(HttpStatus.TOO_MANY_REQUESTS.value())
        );
    }

    @Test
    public void getCurrentWeather_existingIRLCity_isOkReturnsWeather() throws Exception {
        MvcResult mvcResult = performGetRequest(CITY_NAME)
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertDoesNotThrow(() -> objectMapper.readValue(responseBody, FullWeatherInfoDto.class));
    }

    @Test
    public void getCurrentWeather_nonExistentIRLCity_isNotFound() throws Exception {
        performGetRequest("TestCity")
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCurrentWeather_unknownExistingCity_isOkSavedToDb() throws Exception {
        String cityName = "Saratov";
        assertThat(cityRepository.findByName(cityName)).isEmpty();

        performGetRequest(cityName)
                .andExpect(status().isOk());
        assertThat(cityRepository.findByName(cityName)).isNotEmpty();
    }

    private ResultActions performGetRequest(String cityName) throws Exception {
        return mockMvc.perform(get(API_CURRENT_WEATHER)
                .param("location", cityName));
    }
}
