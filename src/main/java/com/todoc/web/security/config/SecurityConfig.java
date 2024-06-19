package com.todoc.web.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.security.jwt.JwtEntryPoint;
import com.todoc.web.security.jwt.JwtTokenProvider;
import com.todoc.web.security.jwt.UserPrincipalDetailsService;
import com.todoc.web.security.oauth.handler.OAuth2LoginFailureHandler;
import com.todoc.web.security.oauth.handler.OAuth2LoginSuccessHandler;
import com.todoc.web.security.oauth.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig 
{
    private final UserPrincipalDetailsService userPrincipalDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oauth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
	private final JwtEntryPoint jwtEntryPoint;
    
    private static final String[] PERMIT_URL = 
    	{
    		"/", "/static/**", "/templates/**", "/js/**", "/image/**", "/css/**", "/RTCPeerConnection/**", "**/favicon.ico",
	        "/layouts/**", "/fragments/**", "/main-page", "/login-page", "/register-page","/medical-register-page", "/medicalSign/**", 
	        "/login/**", "/sign/**", "/token/api"
    	};
    
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception 
    {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception 
    {

    	try
    	{
    		@SuppressWarnings("unused")
			AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();

            http
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(management -> management
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider, objectMapper), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests(requests -> requests
                            .antMatchers(PERMIT_URL).permitAll()
                            .antMatchers("/mypage/**").authenticated()
                            .antMatchers("/pay/**").authenticated()
                            .antMatchers("/user/**").hasRole("USER")
                            .antMatchers("/medical/**").hasRole("MEDICAL") // 의사, 약사
                            .antMatchers("/admin/**").hasRole("ADMIN") // 관리자
                            .anyRequest().permitAll())
                    .httpBasic(basic -> basic.disable())
                    .exceptionHandling(handling -> handling.authenticationEntryPoint(jwtEntryPoint))
                    .logout(logout -> logout
                                    .logoutUrl("/logout").logoutSuccessUrl("/main-page")
                                    .deleteCookies("Authorization")
                                    .permitAll())
                            // 소셜로그인
                            .oauth2Login((oauth2) -> oauth2
                                    .successHandler(oAuth2LoginSuccessHandler)
                                    .failureHandler(oauth2LoginFailureHandler)
                                    .userInfoEndpoint()
                                    .userService(customOAuth2UserService)); // customUserService 설정
            		
            return http.build();
    	}
    	catch(Exception e)
    	{
    		log.error("Security configuration failed", e);
            throw new IllegalStateException("Failed to configure HttpSecurity", e);
    	}
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() 
    {
        return new JwtAuthorizationFilter(jwtTokenProvider, objectMapper);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() 
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() 
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userPrincipalDetailsService);
        
        return provider;
    }
}

