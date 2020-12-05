package com.epam.esm.config;

import com.epam.esm.filter.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/token").permitAll()
                .antMatchers(HttpMethod.GET, "/gift-certificates/certificates").permitAll()
                .antMatchers(HttpMethod.GET, "/gift-certificates/certificates/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/gift-certificates/certificates/search").permitAll()
                .antMatchers(HttpMethod.GET, "/gift-certificates/tags").permitAll()
                .antMatchers(HttpMethod.GET, "/gift-certificates/tags/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/gift-certificates/tags/popular").permitAll()
                .antMatchers(HttpMethod.GET, "/gift-certificates/login").permitAll()
                .antMatchers(HttpMethod.POST, "/gift-certificates/login").permitAll()
                .antMatchers(HttpMethod.GET, "/gift-certificates/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/gift-certificates/signup").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        ;
    }
}
