//package com.myprojects.demo.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class SecurityConfiguration {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((authorizer) -> {
//                    try {
//                        authorizer.requestMatchers("/users/**").anonymous().anyRequest()
//                                .authenticated().and().csrf().disable().cors()
//                                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .httpBasic(Customizer.withDefaults());
//        return http.build();
//    }
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//
//        List<String> allowedOrigins;
//        // For now this is only for local purposes.
//        allowedOrigins = Arrays.asList("*");
//
//        config.setAllowedOrigins(allowedOrigins);
//        List<String> allowedMethods = new ArrayList<>();
//        allowedMethods.add("PUT");
//        allowedMethods.add("GET");
//        allowedMethods.add("POST");
//        allowedMethods.add("DELETE");
//        allowedMethods.add("HEAD");
//        allowedMethods.add("OPTIONS");
//        config.setAllowedMethods(allowedMethods);
//        config.addAllowedHeader("X-Requested-With");
//        config.addAllowedHeader("Content-Type");
//        config.addAllowedHeader("Cache-Control");
//        config.addAllowedHeader("x-token");
//        config.setMaxAge((long) 1800);
//        source.registerCorsConfiguration("/user/**", config);
//
//        return new CorsFilter(source);
//    }
//
//}
