package ru.kslacker.fintech.controllers.cache;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.kslacker.fintech.cache.inmemory.InMemoryWeatherCacheRepository;
import ru.kslacker.fintech.controllers.CityWeatherController;
import ru.kslacker.fintech.dataaccess.entities.City;
import ru.kslacker.fintech.dataaccess.entities.Weather;
import ru.kslacker.fintech.dataaccess.enums.WeatherType;
import ru.kslacker.fintech.dataaccess.repositories.api.CityRepository;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherRepository;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherTypeInfoRepository;
import ru.kslacker.fintech.dto.CreateWeatherDto;
import ru.kslacker.fintech.dto.WeatherDto;
import ru.kslacker.fintech.mapping.WeatherMapper;
import ru.kslacker.fintech.service.CityWeatherServiceImpl;
import ru.kslacker.fintech.validation.service.api.ValidationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CityWeatherController.class)
@Import(CityWeatherControllerCachingConfig.class)
@MockBean({WeatherTypeInfoRepository.class, ValidationService.class, WeatherTypeInfoRepository.class})
@SpyBean(CityWeatherServiceImpl.class)
public class CityWeatherControllerCachingTest {
    private static final String CITY_WEATHER_API = "/api/weather";
    private static final UUID TEST_ID = UUID.randomUUID();
    private static final LocalDate DATE = LocalDate.now();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WeatherRepository weatherRepository;
    @MockBean
    private CityRepository cityRepository;
    @SpyBean
    private InMemoryWeatherCacheRepository cacheRepository;

    @BeforeEach
    public void setUp() {
        given(cityRepository.existsById(any())).willReturn(true);
        given(weatherRepository.getByCityIdAndDateTimeBetween(any(), any(), any())).willReturn(getTestWeather(DATE));
        cacheRepository.clear();
    }

    @Test
    public void getWeather_noRecordInCache_retrievesFromRepository() throws Exception {
        performGetWeatherRequest();

        verify(weatherRepository)
                .getByCityIdAndDateTimeBetween(TEST_ID, DATE.atStartOfDay(), DATE.atStartOfDay().plusDays(1));
    }

    @Test
    public void getWeather_hasRecordsInCache_retrievesFromCache() throws Exception {
        performGetWeatherRequest();                        // data from repository
        MvcResult mvcResult = performGetWeatherRequest()   // data from cache
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        verify(weatherRepository, times(1))  // Repository is called only the first time
                .getByCityIdAndDateTimeBetween(TEST_ID, DATE.atStartOfDay(), DATE.atStartOfDay().plusDays(1));
        List<WeatherDto> expected = getTestWeather(DATE).stream().map(WeatherMapper::asDto).toList();
        Assertions.assertThat(responseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expected));
    }

    @Test
    public void updateWeather_caching_invalidatesCache() throws Exception {
        performGetWeatherRequest();    // from repository
        performGetWeatherRequest();    // from cache
        performUpdateWeatherRequest(); // invalidates cache
        performGetWeatherRequest();    // from repository

        verify(weatherRepository, times(2))
                .getByCityIdAndDateTimeBetween(TEST_ID, DATE.atStartOfDay(), DATE.atStartOfDay().plusDays(1));
    }

    private ResultActions performGetWeatherRequest() throws Exception {
        return mockMvc.perform(get(CITY_WEATHER_API + "/{cityId}", TEST_ID)
                .param("date", String.valueOf(DATE)))
                .andExpect(status().isOk());
    }

    private ResultActions performUpdateWeatherRequest() throws Exception {
        return mockMvc.perform(put(CITY_WEATHER_API + "/{cityId}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCreateWeatherDto(DATE)))
        ).andExpect(status().isOk());
    }

    private List<Weather> getTestWeather(LocalDate date) {
        City city = getTestCity();
        return List.of(
                new Weather(city, null, 42, date.atStartOfDay()),
                new Weather(city, null, -42, date.atStartOfDay())
        );
    }

    private City getTestCity() {
        return new City(TEST_ID, "Test", new ArrayList<>());
    }

    private CreateWeatherDto getCreateWeatherDto(LocalDate date) {
        return new CreateWeatherDto(WeatherType.FOG, 42, date.atStartOfDay());
    }
}
