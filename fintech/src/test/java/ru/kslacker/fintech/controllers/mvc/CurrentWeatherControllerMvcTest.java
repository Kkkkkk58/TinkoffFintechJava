package ru.kslacker.fintech.controllers.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.kslacker.fintech.controllers.CurrentWeatherController;
import ru.kslacker.fintech.dataaccess.enums.UserRole;
import ru.kslacker.fintech.dto.CurrentWeatherDto;
import ru.kslacker.fintech.dto.FullWeatherInfoDto;
import ru.kslacker.fintech.dto.LocationDto;
import ru.kslacker.fintech.exceptions.*;
import ru.kslacker.fintech.service.api.CurrentWeatherService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(CurrentWeatherController.class)
@WithMockUser(username = "user", authorities = UserRole.Code.USER)
public class CurrentWeatherControllerMvcTest {
    private static final String API_CURRENT_WEATHER = "/api/weather/current";
    private static final String CITY_NAME = "Moscow";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CurrentWeatherService currentWeatherService;

    @Test
    public void getCurrentWeather_validRequest_isOk() throws Exception {
        FullWeatherInfoDto response = getTestResponse();
        given(currentWeatherService.getCurrentWeather(CITY_NAME)).willReturn(response);

        MvcResult mvcResult = performGetRequest()
                .andExpect(status().isOk())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        assertThat(actual).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(response));
    }

    @Test
    public void getCurrentWeather_apiKeyIsInvalid_isServiceUnavailable() throws Exception {
        given(currentWeatherService.getCurrentWeather(anyString())).willThrow(new InvalidRemoteServiceApiKeyException());

        performGetRequest()
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void getCurrentWeather_badRemoteRequest_isBadRequest() throws Exception {
        given(currentWeatherService.getCurrentWeather(anyString())).willThrow(BadRemoteRequestException.InvalidRequestUrl());

        performGetRequest()
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCurrentWeather_tooBigRequestBulkBody_isTooManyRequests() throws Exception {
        given(currentWeatherService.getCurrentWeather(anyString())).willThrow(new RemoteRequestBulkBodyTooBigException());

        performGetRequest()
                .andExpect(status().isTooManyRequests());
    }

    @Test
    public void getCurrentWeather_noRemoteServiceResponse_isServiceUnavailable() throws Exception {
        given(currentWeatherService.getCurrentWeather(anyString())).willThrow(new NoRemoteServiceResponseException());

        performGetRequest()
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void getCurrentWeather_locationNotFound_isNotFound() throws Exception {
        given(currentWeatherService.getCurrentWeather(anyString())).willThrow(new RemoteServiceLocationNotFoundException());

        performGetRequest()
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCurrentWeather_remoteServiceUnavailable_isServiceUnavailable() throws Exception {
        given(currentWeatherService.getCurrentWeather(anyString())).willThrow(new RemoteServiceUnavailableException());

        performGetRequest()
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void getCurrentWeather_unknownResponse_isInternalServerError() throws Exception {
        given(currentWeatherService.getCurrentWeather(anyString())).willThrow(new UnknownRemoteWeatherClientResponseException());

        performGetRequest()
                .andExpect(status().isInternalServerError());
    }

    private ResultActions performGetRequest() throws Exception {
        return mockMvc.perform(get(API_CURRENT_WEATHER)
                .param("location", CITY_NAME));
    }

    private FullWeatherInfoDto getTestResponse() {
        return FullWeatherInfoDto.builder()
                .location(LocationDto.builder()
                        .region(CITY_NAME)
                        .build())
                .current(CurrentWeatherDto.builder()
                        .tempCelsius(36.6)
                        .lastUpdated(LocalDateTime.now())
                        .build())
                .build();
    }
}
