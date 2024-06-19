package com.todoc.web.security.jwt;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.auth0.jwt.JWT;
import com.todoc.web.dto.Clinic;
import com.todoc.web.dto.Pharmacy;
import com.todoc.web.dto.User;
import com.todoc.web.security.dto.UserPrincipal;
import com.todoc.web.service.UserService;

import lombok.extern.slf4j.Slf4j;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

// 토큰을 생성,검증, 사용자 인증 처리하는데에 사용
@Slf4j
public class JwtTokenProvider
{
    @Autowired
    private UserService userService;
    
    // token 생성
    public String generateToken(Authentication authentication, boolean isRefreshToken) 
    {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + (isRefreshToken ? JwtProperties.REFRESH_EXPIRATION_TIME : JwtProperties.EXPIRATION_TIME));
        
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toList());
        
        String token = JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withExpiresAt(expiryDate)
                .withClaim("roles", roles)
                .sign(HMAC512(JwtProperties.SECRET.getBytes()));

        log.info("Token generated for user : " + userPrincipal.getUsername() + " with expiry on : " + expiryDate );

        return token;
    }
    
    // 소셜로그인용
    public String socialGenerateToken(Authentication authentication, boolean isRefreshToken) 
    {
        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + (isRefreshToken ? JwtProperties.REFRESH_EXPIRATION_TIME : JwtProperties.EXPIRATION_TIME));
        
        String token = JWT.create()
                .withSubject(authentication.getName())
                .withExpiresAt(expiryDate)
                .sign(HMAC512(JwtProperties.SECRET.getBytes()));

        log.info("Token generated for user : " + authentication.getName() + " with expiry on : " + expiryDate );

        return token;
    }
    
    // jwt 추출 데이터 Authentication 에 넣어주기
    public Authentication getAuthentication(String token) 
    {
        token = token.replace(JwtProperties.TOKEN_PREFIX, "");
        
        String email = JWT.require(HMAC512(JwtProperties.SECRET.getBytes()))
                          .build()
                          .verify(token)
                          .getSubject();
        
        UserPrincipal principal = findUserByEmail(email);
        
        if (principal != null) 
        {
            return new UsernamePasswordAuthenticationToken(email, null, principal.getAuthorities());
        }
        
        return null;
    }

    private UserPrincipal findUserByEmail(String email) 
    {    	
        User user = userService.findByEmail(email);
        
        if (user != null) 
        {
            return new UserPrincipal(user);
        }
        
        Clinic clinic = userService.findClinicEmail(email);
       
        if (clinic != null) 
        {
            return new UserPrincipal(clinic);
        }
        
        Pharmacy pharmacy = userService.findPharmEmail(email);
        
        if (pharmacy != null) 
        {
            return new UserPrincipal(pharmacy);
        }
        
        return null;
    }
}
