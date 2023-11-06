package ru.kslacker.fintech.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.kslacker.fintech.dataaccess.enums.UserRole;
import ru.kslacker.fintech.dataaccess.enums.WeatherType;
import ru.kslacker.fintech.dto.CreateCityDto;
import ru.kslacker.fintech.dto.CreateWeatherDto;
import ru.kslacker.fintech.service.api.CityWeatherService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CityWeatherControllerAuthTest {
    private static final String CITY_WEATHER_API = "/api/weather";
    private static final UUID TEST_ID = UUID.randomUUID();
    private static final String CITY_NAME = "Moscow";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CityWeatherService service;

    @Test
    public void getWeather_unauthorized_isUnauthorized() throws Exception {
        performGetWeatherRequest()
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(UserRole.Code.ADMIN)
    public void getWeather_isAdmin_isOk() throws Exception {
        given(service.getWeather(any(), any())).willReturn(List.of());

        performGetWeatherRequest()
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(UserRole.Code.USER)
    public void getWeather_isUser_isOk() throws Exception {
        given(service.getWeather(any(), any())).willReturn(List.of());

        performGetWeatherRequest()
                .andExpect(status().isOk());
    }

    @Test
    public void createCity_unauthorized_isUnauthorized() throws Exception {
        performCreateCityRequest()
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(UserRole.Code.ADMIN)
    public void createCity_isAdmin_isOk() throws Exception {
        performCreateCityRequest()
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(UserRole.Code.USER)
    public void createCity_isUser_isForbidden() throws Exception {
        performCreateCityRequest()
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateWeather_unauthorized_isUnauthorized() throws Exception {
        performUpdateWeatherRequest()
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(UserRole.Code.ADMIN)
    public void updateWeather_isAdmin_isOk() throws Exception {
        performUpdateWeatherRequest()
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(UserRole.Code.USER)
    public void updateWeather_isUser_isForbidden() throws Exception {
        performUpdateWeatherRequest()
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteCity_unauthorized_isUnauthorized() throws Exception {
        performDeleteCityRequest()
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(UserRole.Code.ADMIN)
    public void deleteCity_isAdmin_isOk() throws Exception {
        performDeleteCityRequest()
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(UserRole.Code.USER)
    public void deleteCity_isUser_isForbidden() throws Exception {
        performDeleteCityRequest()
                .andExpect(status().isForbidden());
    }

    private ResultActions performGetWeatherRequest() throws Exception {
        return mockMvc.perform(get(CITY_WEATHER_API + "/{cityId}", TEST_ID)
                .param("date", String.valueOf(LocalDate.now())));
    }

    private ResultActions performCreateCityRequest() throws Exception {
        CreateCityDto createCityDto = new CreateCityDto(CITY_NAME, List.of());

        return mockMvc.perform(post(CITY_WEATHER_API + "/{cityId}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCityDto)));
    }

    private ResultActions performUpdateWeatherRequest() throws Exception {
        CreateWeatherDto createWeatherDto = new CreateWeatherDto(WeatherType.CLEAR, 0, LocalDateTime.now());

        return mockMvc.perform(put(CITY_WEATHER_API + "/{cityId}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createWeatherDto)));
    }

    private ResultActions performDeleteCityRequest() throws Exception {
        return mockMvc.perform(delete(CITY_WEATHER_API + "/{cityId}", TEST_ID));
    }
}
