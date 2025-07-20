package cz.promtply.backend.config;

import cz.promtply.backend.enums.UserRolesEnum;
import cz.promtply.backend.filter.ApiKeyFilter;
import cz.promtply.backend.filter.JwtFilter;
import cz.promtply.backend.service.ClientTokenService;
import cz.promtply.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final ClientTokenService clientTokenService;

    @Bean
    @Order(1)
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
    @Order(2)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(Constants.INTERNAL_API_ROUTE_PREFIX + "/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(Constants.INTERNAL_API_ROUTE_PREFIX + "/auth/login").permitAll()

                        .requestMatchers(Constants.INTERNAL_API_ROUTE_PREFIX + "/auth/**")
                            .hasAnyRole(
                                UserRolesEnum.NON_TOTP.name(),
                                UserRolesEnum.USER.name(),
                                UserRolesEnum.ADMIN.name()
                            )

                        .anyRequest()
                            .hasAnyRole(
                                UserRolesEnum.USER.name(),
                                UserRolesEnum.ADMIN.name()
                            )
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