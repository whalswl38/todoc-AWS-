package com.todoc.web.security.jwt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.todoc.web.dto.User;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.security.jwt.JwtProperties;
import com.todoc.web.security.jwt.JwtTokenProvider;
import com.todoc.web.service.UserService;

// 토큰 시간 만료될 경우 처리해줄 컨트롤러
@RestController
@RequestMapping
public class JwtTokenController 
{
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private JwtAuthorizationFilter jwtFilter;
	
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@PostMapping("/token/api")
	public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response)
	{
		// 토큰 값 추출
		String token = jwtFilter.extractJwtFromCookie(request);
		
		// 토큰에서 유저 이메일 추출
		String userEmail = jwtFilter.getUsernameFromToken(token);
		
		// 유저 리프레쉬 토큰 있는지 확인
		User user = userService.findByEmail(userEmail);
		
		Authentication auth =jwtTokenProvider.getAuthentication(token);
		
        if (token != null && jwtFilter.validateToken(token) && user.getUserRefreshToken() != null) 
        {
            // 토큰을 검증하고, 만료 시간을 연장하여 새로운 토큰을 생성
            String newToken = jwtTokenProvider.generateToken(auth, false);
            
            // 새로운 토큰을 응답으로 반환
            Cookie cookie = new Cookie("token", newToken);
    	    cookie.setHttpOnly(true);
    	    cookie.setMaxAge(JwtProperties.EXPIRATION_TIME);
    	    cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok().build();
        } 
        else 
        {
            return ResponseEntity.badRequest().build();
        }
	}
}
