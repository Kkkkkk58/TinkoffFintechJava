package ru.kslacker.fintech.controllers.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.kslacker.fintech.annotations.TestAsAdmin;
import ru.kslacker.fintech.annotations.TestAsUser;
import ru.kslacker.fintech.service.api.CurrentWeatherService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@MockBean({CurrentWeatherService.class})
public class CurrentWeatherControllerAuthTest {
    private static final String API_CURRENT_WEATHER = "/api/weather/current";
    private static final String TEST_CITY = "Moscow";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getCurrentWeather_unauthorized_isUnauthorized() throws Exception {
        performGetRequest()
                .andExpect(status().isUnauthorized());
    }

    @Test
    @TestAsAdmin
    public void getCurrentWeather_isAdmin_isOk() throws Exception {
        performGetRequest()
                .andExpect(status().isOk());
    }

    @Test
    @TestAsUser
    public void getCurrentWeather_isUser_isOk() throws Exception {
        performGetRequest()
                .andExpect(status().isOk());
    }


    private ResultActions performGetRequest() throws Exception {
        return mockMvc.perform(get(API_CURRENT_WEATHER)
                .param("location", TEST_CITY));
    }
}
