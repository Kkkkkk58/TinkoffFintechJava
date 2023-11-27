package ru.kslacker.fintech.controllers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.kslacker.fintech.annotations.TestAsUser;
import ru.kslacker.fintech.dataaccess.repositories.api.CityRepository;
import ru.kslacker.fintech.dto.AirQualityDto;
import ru.kslacker.fintech.dto.ConditionDto;
import ru.kslacker.fintech.dto.CurrentWeatherDto;
import ru.kslacker.fintech.dto.FullWeatherInfoDto;
import ru.kslacker.fintech.dto.LocationDto;
import ru.kslacker.fintech.exceptions.RemoteServiceLocationNotFoundException;
import ru.kslacker.fintech.service.api.CurrentWeatherService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        properties = {
                "resilience4j.ratelimiter.instances.remote-weather-service.limitForPeriod=15",
                "resilience4j.ratelimiter.instances.remote-weather-service.limitRefreshPeriod=1s",
                "resilience4j.ratelimiter.instances.remote-weather-service.timeoutDuration=10ms"
        }
)
@AutoConfigureMockMvc
@Testcontainers
@TestAsUser
public class CurrentWeatherControllerTest extends TestContainersH2Test {
    private static final String API_CURRENT_WEATHER = "/api/weather/current";
    private static final String CITY_NAME = "Moscow";
    private static final int RATELIMITER_RPS = 15;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CityRepository cityRepository;

    @MockBean
    private CurrentWeatherService currentWeatherService;

    @Test
    public void getCurrentWeather_exceedingRps_isTooManyRequests() {
        setUpSuccessfulReturn();
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
                () -> assertThat(responseStatusCount.keySet()).contains(HttpStatus.UNAUTHORIZED.value())
        );
    }

    @Test
    public void getCurrentWeather_existingIRLCity_isOkReturnsWeather() throws Exception {
        setUpSuccessfulReturn();
        MvcResult mvcResult = performGetRequest(CITY_NAME)
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertDoesNotThrow(() -> objectMapper.readValue(responseBody, FullWeatherInfoDto.class));
    }

    @Test
    public void getCurrentWeather_nonExistentIRLCity_isNotFound() throws Exception {
        setUpUnsuccessfulReturn();
        performGetRequest("TestCity")
                .andExpect(status().isNotFound());
    }

    private ResultActions performGetRequest(String cityName) throws Exception {
        return mockMvc.perform(get(API_CURRENT_WEATHER)
                .param("location", cityName));
    }

    private void setUpSuccessfulReturn() {
        given(currentWeatherService.getCurrentWeather(anyString())).willReturn(
                FullWeatherInfoDto.builder()
                        .location(LocationDto.builder().build())
                        .current(CurrentWeatherDto.builder()
                                .condition(ConditionDto.builder().build())
                                .airQuality(AirQualityDto.builder().build())
                                .build())
                        .build()
        );
    }

    private void setUpUnsuccessfulReturn() {
        given(currentWeatherService.getCurrentWeather(anyString())).willThrow(new RemoteServiceLocationNotFoundException());
    }
}
