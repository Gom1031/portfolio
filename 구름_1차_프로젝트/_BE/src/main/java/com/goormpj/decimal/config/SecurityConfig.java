package com.goormpj.decimal.config;

import com.goormpj.decimal.jwt.CustomLogoutFilter;
import com.goormpj.decimal.jwt.JWTFilter;
import com.goormpj.decimal.jwt.JWTProvider;
import com.goormpj.decimal.jwt.LoginFilter;
import com.goormpj.decimal.user.repository.RefreshTokenRepository;
import com.goormpj.decimal.user.oauth2.handler.CustomSuccessHandler;
import com.goormpj.decimal.user.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity //추가
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    private final JWTProvider jwtProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    private final CustomOAuth2UserService customOAuth2UserService; //추가

    private final CustomSuccessHandler customSuccessHandler; //추가

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration) , jwtProvider, refreshTokenRepository);
        loginFilter.setFilterProcessesUrl("/api/login");

        http.httpBasic(basic->basic.disable())
                .formLogin(form -> form.disable())
                .csrf(csrf->csrf.disable())
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.setAllowedOrigins(List.of("https://groomcosmos.site"));
                            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                            config.setAllowedHeaders(List.of("*"));
                            config.setAllowCredentials(true);
                            config.setMaxAge(3600L);

                            config.setExposedHeaders(Arrays.asList("Set-Cookie"));
                            config.setExposedHeaders(Arrays.asList("access_token"));
                            return config;
                        }))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/users/signup", "/", "/api/login","/api/logout").permitAll()
                        .requestMatchers("/api/reissue").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JWTFilter(jwtProvider), LoginFilter.class)
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler))
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLogoutFilter(jwtProvider, refreshTokenRepository), LogoutFilter.class)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


}
