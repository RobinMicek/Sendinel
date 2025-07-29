package cz.promptly.api.config;

import cz.promptly.shared.config.Constants;
import cz.promptly.shared.enums.UserRolesEnum;
import cz.promptly.api.filter.ApiKeyFilter;
import cz.promptly.api.filter.JwtFilter;
import cz.promptly.api.service.ClientTokenService;
import cz.promptly.api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final ClientTokenService clientTokenService;

    @Bean
    @Order(1)
    public SecurityFilterChain trackingFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(Constants.TRACKING_API_ROUTE_PREFIX + "/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain clientTokenFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(Constants.EXTERNAL_API_ROUTE_PREFIX + "/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().hasRole(UserRolesEnum.CLIENT.name())
                )
                .addFilterBefore(apiKeyFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(Constants.INTERNAL_API_ROUTE_PREFIX + "/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(Constants.INTERNAL_API_ROUTE_PREFIX + "/oobe/**").permitAll()
                        .requestMatchers(Constants.INTERNAL_API_ROUTE_PREFIX + "/auth/login").permitAll()
                        .requestMatchers(Constants.INTERNAL_API_ROUTE_PREFIX + "/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtil);
    }

    public ApiKeyFilter apiKeyFilter() {
        return new ApiKeyFilter(clientTokenService);
    }
}