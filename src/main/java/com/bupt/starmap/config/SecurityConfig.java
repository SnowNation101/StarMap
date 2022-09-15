package com.bupt.starmap.config;

import com.bupt.starmap.filter.CustomAuthenticationFilter;
import com.bupt.starmap.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    CustomAuthenticationFilter customAuthenticationFilter
        = new CustomAuthenticationFilter(authenticationManagerBean());
    customAuthenticationFilter.setFilterProcessesUrl("/api/login");
    http
        .cors()
        .configurationSource(corsConfigurationSource())
        .and()
        .csrf().disable();
    http
        .sessionManagement().sessionCreationPolicy(STATELESS);
    http
        .authorizeRequests()
//        .antMatchers("/").permitAll()
//        .antMatchers("/api/user/register", "/api/logout").permitAll()
//        .antMatchers("/api/login/**", "/api/token/refresh/**", "index").permitAll()
//        .antMatchers("api/**").hasAnyAuthority("ROLE_USER")
//        .antMatchers(GET, "api/user/**").hasAnyAuthority("ROLE_USER")
//        .antMatchers(POST, "api/user/save/**").hasAnyAuthority("ROLE_USER")
//        .antMatchers(POST, "/starmap").permitAll()
//        .antMatchers(POST, "/api/user/save").permitAll()
//        .antMatchers("/api/users").hasAnyAuthority("ROLE_USER")
        .antMatchers("/**").permitAll()
//        .antMatchers("/api/user/save").hasAnyAuthority("ROLE_USER")
        .anyRequest().authenticated()
        .and()
        .addFilter(customAuthenticationFilter)
        .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    http
        .rememberMe()
        .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(200))
        .key("somethingverysecured");
    http
        .logout()
        .logoutUrl("/api/logout")
        .logoutRequestMatcher(new AntPathRequestMatcher("/api/logout", "GET"))
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID", "remember-me");
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
    configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    configuration.setAllowCredentials(false);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}