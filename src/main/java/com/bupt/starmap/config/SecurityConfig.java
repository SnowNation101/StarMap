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

import java.util.concurrent.TimeUnit;

import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;

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
                .csrf().disable();
        http
                .sessionManagement().sessionCreationPolicy(IF_REQUIRED);
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/login/**", "/api/token/refresh/**", "index").permitAll()
//                .antMatchers(GET, "api/user/**").hasAnyAuthority("ROLE_USER")
//                .antMatchers(POST, "api/user/save/**").hasAnyAuthority("ROLE_USER")
//                .antMatchers(POST, "/starmap").permitAll()
//                .antMatchers(POST, "/api/user/save").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin().loginPage("/login").permitAll()
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/starmap", true);
        http
                .rememberMe()
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(20))
                .key("somethingverysecured");
        http
                .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
