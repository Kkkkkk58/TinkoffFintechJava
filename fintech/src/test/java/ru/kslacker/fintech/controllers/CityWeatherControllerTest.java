package ru.kslacker.fintech.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.kslacker.fintech.dataaccess.entities.City;
import ru.kslacker.fintech.dataaccess.entities.Weather;
import ru.kslacker.fintech.dataaccess.enums.WeatherType;
import ru.kslacker.fintech.dataaccess.repositories.api.CityRepository;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherRepository;
import ru.kslacker.fintech.dto.CreateCityDto;
import ru.kslacker.fintech.dto.CreateWeatherDto;
import ru.kslacker.fintech.dto.WeatherDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
public class CityWeatherControllerTest extends TestContainersH2Test {
    private static final String CITY_WEATHER_API = "/api/weather";
    private static final UUID TEST_ID = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private WeatherRepository weatherRepository;


    @Test
    void getWeather_existentCity_isOkReturnsWeatherForDate() throws Exception {
        List<CreateWeatherDto> createWeatherDtos = getValidCreateWeatherDtos();
        CreateCityDto createCityDto = new CreateCityDto(getTestCity().getName(), createWeatherDtos);
        performCityCreation(createCityDto);

        LocalDate date = createWeatherDtos.get(0).dateTime().toLocalDate();
        MvcResult mvcResult = performGetWeather(date)
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        List<WeatherDto> responseWeather = Arrays.asList(objectMapper.readValue(responseBody, WeatherDto[].class));

        assertAll(
                () -> assertThat(responseWeather)
                        .allMatch(r -> date.equals(r.dateTime().toLocalDate())),
                () -> assertThat(responseWeather.size())
                                .isEqualTo(createWeatherDtos.stream().filter(r -> date.equals(r.dateTime().toLocalDate())).count())
        );
    }

    @Test
    void getWeather_nonExistentCity_isNotFound() throws Exception {
        performGetWeather(LocalDate.now())
                .andExpect(status().isNotFound());
    }

    @Test
    void createCity_newCityValidModel_isCreated() throws Exception {
        List<CreateWeatherDto> createWeatherDtos = getValidCreateWeatherDtos();
        CreateCityDto createCityDto = new CreateCityDto(getTestCity().getName(), createWeatherDtos);
        assertThat(cityRepository.existsById(TEST_ID)).isFalse();

        performCityCreation(createCityDto)
                .andExpect(status().isOk());
        assertAll(
                () -> assertThat(cityRepository.existsById(TEST_ID)).isTrue(),
                () -> assertThat(getWeatherByCityAndDate(createWeatherDtos)).isNotEmpty()
        );

    }

    @Test
    void createCity_existentCity_isConflict() throws Exception {
        List<CreateWeatherDto> createWeatherDtos = getValidCreateWeatherDtos();
        CreateCityDto createCityDto = new CreateCityDto(getTestCity().getName(), createWeatherDtos);

        performCityCreation(createCityDto);

        performCityCreation(createCityDto)
                .andExpect(status().isConflict());
    }

    @Test
    void createCity_newCityInvalidModel_isBadRequest() throws Exception {
        List<CreateWeatherDto> createWeatherDtos = getInvalidCreateWeatherDtos();
        CreateCityDto createCityDto = new CreateCityDto(getTestCity().getName(), createWeatherDtos);

        performCityCreation(createCityDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWeather_validParams_isOkWeatherUpdated() throws Exception {
        List<CreateWeatherDto> createWeatherDtos = getValidCreateWeatherDtos();
        CreateCityDto createCityDto = new CreateCityDto(getTestCity().getName(), createWeatherDtos);
        performCityCreation(createCityDto);

        CreateWeatherDto dtoToUpdate = createWeatherDtos.get(0);
        double newTemperature = dtoToUpdate.temperatureValue() + 98;
        CreateWeatherDto newDto = new CreateWeatherDto(
                dtoToUpdate.type(),
                newTemperature,
                dtoToUpdate.dateTime());

        performWeatherUpdate(newDto)
                .andExpect(status().isOk());
        List<Weather> values = weatherRepository.getByCityIdAndDateTimeBetween(TEST_ID, newDto.dateTime(), newDto.dateTime().plusDays(1));
        assertAll(
                () -> assertThat(values).isNotEmpty(),
                () -> assertThat(values)
                        .filteredOn(Weather::getDateTime, newDto.dateTime())
                        .map(Weather::getTemperatureValue)
                        .allMatch(t -> t == newTemperature)
        );
    }

    @Test
    void updateWeather_nonExistentCity_isNotFound() throws Exception {
        performWeatherUpdate(getValidCreateWeatherDtos().get(0))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCity_existingCity_isOkCityDeleted() throws Exception {
        List<CreateWeatherDto> createWeatherDtos = getValidCreateWeatherDtos();
        CreateCityDto createCityDto = new CreateCityDto(getTestCity().getName(), createWeatherDtos);
        performCityCreation(createCityDto);

        performCityDeletion()
                .andExpect(status().isOk());
        assertAll(
                () -> assertThat(cityRepository.existsById(TEST_ID)).isFalse(),
                () -> assertThat(getWeatherByCityAndDate(createWeatherDtos)).isEmpty()
        );
    }

    @Test
    void deleteCity_nonExistentCity_isNotFound() throws Exception {
        performCityDeletion()
                .andExpect(status().isNotFound());
    }

    private ResultActions performGetWeather(LocalDate date) throws Exception {
        return mockMvc.perform(get(CITY_WEATHER_API + "/{cityId}", TEST_ID)
                .param("date", String.valueOf(date)));
    }

    private ResultActions performCityCreation(CreateCityDto createCityDto) throws Exception {
        return mockMvc.perform(post(CITY_WEATHER_API + "/{cityId}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCityDto)));
    }

    private ResultActions performWeatherUpdate(CreateWeatherDto createWeatherDto) throws Exception {
        return mockMvc.perform(put(CITY_WEATHER_API + "/{cityId}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createWeatherDto)));
    }

    private ResultActions performCityDeletion() throws Exception {
        return mockMvc.perform(delete(CITY_WEATHER_API + "/{cityId}", TEST_ID));
    }

    private List<Weather> getWeatherByCityAndDate(List<CreateWeatherDto> createWeatherDtos) {
        return weatherRepository.getByCityIdAndDateTimeBetween(
                TEST_ID,
                createWeatherDtos.get(0).dateTime(),
                createWeatherDtos.get(createWeatherDtos.size() - 1).dateTime().plusDays(1));
    }

    private List<CreateWeatherDto> getValidCreateWeatherDtos() {
        return List.of(
                new CreateWeatherDto(WeatherType.FOG, 42, LocalDateTime.now()),
                new CreateWeatherDto(WeatherType.SNOW, -42, LocalDateTime.now().plusDays(5))
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

}
