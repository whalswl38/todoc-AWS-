package com.todoc.web.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.todoc.web.security.jwt.JwtTokenProvider;

@Configuration
public class JwtConfig 
{
    @Bean
    public JwtTokenProvider jwtTokenProvider() 
    {
        return new JwtTokenProvider();
    }
}
