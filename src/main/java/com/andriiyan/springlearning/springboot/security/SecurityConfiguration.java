package com.andriiyan.springlearning.springboot.security;

import com.andriiyan.springlearning.springboot.impl.service.CustomOAuth2UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@OpenAPIDefinition(
        info = @Info(description = "<a href=\"${spring.security.oauth2.social-login-url}github\">Login via  GitHub</a>")
)
@SecurityScheme(
        name = SecurityConfiguration.SCHEME,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SecurityConfiguration {
    public static final String SCHEME = "Bearer token";

    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain web(HttpSecurity http, OAuth2AuthenticationSuccessHandler auth2AuthorizationSuccessHandler, @Value("${spring.security.oauth2.social-login-url}") String oauthBaseUri) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .antMatchers("/", "/**.html", "/**.js", "/**.ico").permitAll()
                        .antMatchers("/api/v1/login", "/login/oauth2/code/**", "/api/v1/logout", "/api/v1/login/provider/**").permitAll()
                        .antMatchers("/api/v1/admin/test").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri(oauthBaseUri)
                .and()
                .defaultSuccessUrl("/api/v1/login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(auth2AuthorizationSuccessHandler)
                .and()
                .logout()
                .logoutUrl("/api/v1/logout")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint());
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
