package ru.kslacker.fintech.coniguration;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class TestSecurityConfig {
    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(a ->
                        a.requestMatchers("/api/auth/register").permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .httpBasic(httpBasic ->
                        httpBasic
                                .authenticationEntryPoint(
                                        (request, response, authException) ->
                                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                                ))
                .build();
    }

}
