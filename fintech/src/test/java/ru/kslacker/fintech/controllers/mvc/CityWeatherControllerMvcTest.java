package ru.kslacker.fintech.controllers.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.kslacker.fintech.controllers.CityWeatherController;
import ru.kslacker.fintech.dataaccess.entities.City;
import ru.kslacker.fintech.dataaccess.entities.Weather;
import ru.kslacker.fintech.dataaccess.enums.WeatherType;
import ru.kslacker.fintech.dataaccess.repositories.api.CityRepository;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherRepository;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherTypeInfoRepository;
import ru.kslacker.fintech.dto.CreateCityDto;
import ru.kslacker.fintech.dto.CreateWeatherDto;
import ru.kslacker.fintech.dto.WeatherDto;
import ru.kslacker.fintech.mapping.WeatherMapper;
import ru.kslacker.fintech.service.CityWeatherServiceImpl;
import ru.kslacker.fintech.validation.service.ValidationServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CityWeatherController.class)
@SpyBean(CityWeatherServiceImpl.class)
public class CityWeatherControllerMvcTest {
    private static final String CITY_WEATHER_API = "/api/weather";
    private static final UUID TEST_ID = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WeatherRepository weatherRepository;
    @MockBean
    private CityRepository cityRepository;
    @MockBean
    private WeatherTypeInfoRepository weatherTypeInfoRepository;
    @SpyBean
    private ValidationServiceImpl validationService;

    @Test
    public void getWeather_existingCity_isOkReturnsWeather() throws Exception {
        given(cityRepository.existsById(TEST_ID)).willReturn(true);
        List<Weather> testWeather = getTestWeather();
        given(weatherRepository.getByCityIdAndDateTimeBetween(eq(TEST_ID), any(), any())).willReturn(testWeather);

        LocalDate date = getDate();
        MvcResult mvcResult = mockMvc.perform(get(CITY_WEATHER_API + "/{cityId}", TEST_ID)
                        .param("date", String.valueOf(date)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        List<WeatherDto> expected = testWeather.stream().map(WeatherMapper::asDto).toList();
        assertThat(responseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expected));
        verify(weatherRepository)
                .getByCityIdAndDateTimeBetween(TEST_ID, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    @Test
    public void getWeather_nonExistentCity_isNotFound() throws Exception {
        given(cityRepository.existsById(TEST_ID)).willReturn(false);

        LocalDate date = getDate();
        mockMvc.perform(get(CITY_WEATHER_API + "/{cityId}", TEST_ID)
                        .param("date", String.valueOf(date)))
                .andExpect(status().isNotFound());
        verify(cityRepository)
                .existsById(TEST_ID);
    }

    @Test
    public void createCity_nonExistentCity_isOk() throws Exception {
        given(cityRepository.existsById(TEST_ID)).willReturn(false);
        List<CreateWeatherDto> createWeatherDtos = getValidCreateWeatherDtos();
        CreateCityDto createCityDto = new CreateCityDto(getTestCity().getName(), createWeatherDtos);

        mockMvc.perform(post(CITY_WEATHER_API + "/{cityId}", TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCityDto)))
                .andExpect(status().isOk());
        verify(cityRepository)
                .save(any());
    }

    @Test
    public void createCity_existingCity_isConflict() throws Exception {
        given(cityRepository.existsById(TEST_ID)).willReturn(true);
        List<CreateWeatherDto> createWeatherDtos = getValidCreateWeatherDtos();
        CreateCityDto createCityDto = new CreateCityDto(getTestCity().getName(), createWeatherDtos);

        mockMvc.perform(post(CITY_WEATHER_API + "/{cityId}", TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCityDto)))
                .andExpect(status().isConflict());
        verify(cityRepository)
                .existsById(TEST_ID);
    }

    @Test
    public void createCity_weatherListHasIntersections_isBadRequest() throws Exception {
        List<CreateWeatherDto> createWeatherDtos = getInvalidCreateWeatherDtos();
        CreateCityDto createCityDto = new CreateCityDto(getTestCity().getName(), createWeatherDtos);

        mockMvc.perform(post(CITY_WEATHER_API + "/{cityId}", TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCityDto)))
                .andExpect(status().isBadRequest());
        verify(validationService).validate(createCityDto);
    }

    private List<Weather> getTestWeather() {
        City city = getTestCity();
        return List.of(
                new Weather(city, null, 42, LocalDateTime.now()),
                new Weather(city, null, -42, LocalDateTime.now())
        );
    }

    private List<CreateWeatherDto> getValidCreateWeatherDtos() {
        return List.of(
                new CreateWeatherDto(WeatherType.FOG, 42, LocalDateTime.now()),
                new CreateWeatherDto(WeatherType.SNOW, -42, LocalDateTime.now().plusDays(1))
        );
    }

    private List<CreateWeatherDto> getInvalidCreateWeatherDtos() {
        LocalDateTime dateTime = LocalDateTime.now();

        return List.of(
                new CreateWeatherDto(WeatherType.FOG, 42, dateTime),
                new CreateWeatherDto(WeatherType.SNOW, -42, dateTime)
        );
    }

    private City getTestCity() {
        return new City(TEST_ID, "Test", new ArrayList<>());
    }

    private LocalDate getDate() {
        return LocalDate.now();
    }
}
