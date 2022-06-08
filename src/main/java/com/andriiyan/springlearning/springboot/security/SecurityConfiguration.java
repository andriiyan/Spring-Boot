package com.andriiyan.springlearning.springboot.security;

import com.andriiyan.springlearning.springboot.impl.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration {

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
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/secured").authenticated()
                        .antMatchers("/api/v1/login").permitAll()
                        .antMatchers("/login/oauth2/code/github").permitAll()
                        .antMatchers("/api/v1/scope_admin").hasAuthority("ADMIN")
                        .antMatchers("/api/v1/**").authenticated()
                        .mvcMatchers("/github/**").permitAll()
                        .antMatchers("/", "/**.html", "/**.js").permitAll()
                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login()
                .defaultSuccessUrl("/secured")
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
