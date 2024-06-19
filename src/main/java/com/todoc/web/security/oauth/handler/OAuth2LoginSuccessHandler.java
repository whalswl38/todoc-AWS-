package com.todoc.web.security.oauth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.todoc.web.dao.UserDao;
import com.todoc.web.dto.User;
import com.todoc.web.security.jwt.JwtProperties;
import com.todoc.web.security.jwt.JwtTokenProvider;
import com.todoc.web.security.oauth.info.CustomOAuth2User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler 
{
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDao userDao;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException 
    {
        log.info("OAuth login success !");
     	
        try 
        {
        	 CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        	 User user = null;

         	 if(oAuth2User.getEmail().equals("id")) user = userDao.findByPwd(oAuth2User.getName());
         	 else if(oAuth2User.getEmail().contains("com")) user = userDao.findByEmail(oAuth2User.getEmail());
         	 else user = userDao.findByPwd(oAuth2User.getEmail());
         	
             if(user.getUserType().equals("GUEST")) 
             {                   
                  try
                  {   
                	  String accessToken = jwtTokenProvider.socialGenerateToken(authentication, false);
                	  String refreshToken = jwtTokenProvider.socialGenerateToken(authentication, true);

                	  Cookie cookie = new Cookie(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
                	  cookie.setHttpOnly(true);
                	  cookie.setMaxAge(JwtProperties.EXPIRATION_TIME);
                	  cookie.setPath("/");
                	  cookie.setSecure(true);
                	  response.addCookie(cookie);

                      user.setUserRefreshToken(refreshToken);
                      userDao.refreshTokenUpdate(user);

                      response.sendRedirect("/login/oauthRegister");
                   }
                   catch(Exception e)
                   {
                       log.error(e.getMessage());
                   }
             }
             else
             {
            	   loginSuccess(response, oAuth2User, authentication, user);
             }
        } 
        catch (Exception e) 
        {
            throw e;
        }
    }
    
    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User, Authentication authentication, User user) throws IOException
    {
    	String refreshToken = jwtTokenProvider.socialGenerateToken(authentication, true);
    	String accessToken = jwtTokenProvider.socialGenerateToken(authentication, false);
    	
    	user.setUserRefreshToken(refreshToken);
    	userDao.refreshTokenUpdate(user);
    	
    	Cookie cookie = new Cookie(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
    	cookie.setHttpOnly(true);
    	cookie.setMaxAge(JwtProperties.EXPIRATION_TIME);
    	cookie.setPath("/");
    	cookie.setSecure(true);
    	response.addCookie(cookie);
    	
    	response.sendRedirect("/main-page");
    }
}