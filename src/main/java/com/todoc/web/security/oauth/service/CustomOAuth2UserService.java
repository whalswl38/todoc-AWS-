package com.todoc.web.security.oauth.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import com.todoc.web.dao.UserDao;
import com.todoc.web.dto.User;
import com.todoc.web.security.oauth.SocialType;
import com.todoc.web.security.oauth.info.CustomOAuth2User;
import com.todoc.web.security.oauth.info.OAuthAttributes;

import edu.emory.mathcs.backport.java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// OAuth2 로그인 로직을 담당
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>
{
	private final UserDao userDao;
	
	private static final String NAVER = "naver";
	private static final String KAKAO = "kakao";
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException 
	{
	    log.info("socialLogin loadUser 실행 - OAuth2 로그인 요청 진입");

	    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
	    OAuth2User oAuth2User = delegate.loadUser(userRequest);

	    String registrationId = userRequest.getClientRegistration().getRegistrationId();
	    SocialType socialType = getSocialType(registrationId);

	    Map<String, Object> attributes = oAuth2User.getAttributes();

	    log.info("Received attributes from OAuth2 provider:");
	    
	    for (Map.Entry<String, Object> entry : attributes.entrySet()) 
	    {
	        System.out.println(entry.getKey() + ": " + entry.getValue());
	    }
	    
        String userIdAttributeName = null;
        String userNameAttributeName = null;
        String email = null;

        if (NAVER.equals(registrationId))
        {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            
            email = (String) response.get("id");
            userNameAttributeName = "response";
            userIdAttributeName = "response";
        }
        else if (KAKAO.equals(registrationId))
        {   
            userNameAttributeName = "id";
            userIdAttributeName = "id";
            email = "id";
        }
        else 
        {
            email = (String) attributes.get("email");
            userNameAttributeName = "email";
        }

	    if (email == null) 
	    {
	        throw new OAuth2AuthenticationException(new OAuth2Error("email_not_found"), "Email not found");
	    }

	    log.info("=== loadUser === : " + email);

	    OAuthAttributes oauthAtt = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

	    User user = getUser(oauthAtt, socialType);
	    
	    return new CustomOAuth2User(
	            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getUserType())),
	            attributes,
	            userNameAttributeName,
	            email,
	            "ROLE_" + user.getUserType()
	    );
	}
    
	// 해당하는 registration 의 소셜 타입 가져오기
	private SocialType getSocialType(String registrationId)
	{
		if(NAVER.equals(registrationId))
		{
			return SocialType.NAVER;
		}
		else if(KAKAO.equals(registrationId))
		{
			return SocialType.KAKAO;
		}
		
		return SocialType.GOOGLE;
	}
	
	// 소셜 로그인의 정보와 회원을 찾아 반환하는 메소드
	// 만약 회원 정보가 조회된다면, 그대로 반환하고 없다면 DB 에 회원 저장
	private User getUser(OAuthAttributes attributes, SocialType socialType)
	{
		User findUser = userDao.findByEmail(attributes.getOauth2User().getEmail());
		
		if(findUser != null)
		{
			return findUser;
		}
		
		return saveUser(attributes, socialType);
	}
	
	// OAuthAtribute 의 toEntity 메소드를 총해서 User 객체 생성 후 반환 >> DB 저장
	private User saveUser(OAuthAttributes attributes, SocialType socialType)
	{
		User user = attributes.toEntity(socialType, attributes.getOauth2User());
		userDao.insertSocial(user);
		return user;
	}
}
