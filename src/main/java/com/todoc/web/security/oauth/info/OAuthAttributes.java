package com.todoc.web.security.oauth.info;

import java.util.Map;

import com.todoc.web.dto.User;
import com.todoc.web.security.oauth.SocialType;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

// 각 소셜마다 가져오는 데이터가 다르므로 그거를 분기해줄 클래스
@Slf4j
@Getter
public class OAuthAttributes 
{
	private String userNameAttributeKey; // 소셜로그인시 키가 되는 값
	private OAuth2UserInfo oauth2User; // 소셜 타입별 로그인 유저 정보
	
	@Builder
	private OAuthAttributes(String userNameAttributeKey, OAuth2UserInfo oauth2User)
	{		
		this.userNameAttributeKey = userNameAttributeKey;
		this.oauth2User = oauth2User;
	}
	
	// 소셜 타입에 맞는 메소드를 호출해서 OAuthAttributes 객체 반환
	// 파라미터는 로그인시 OAuth 서비스의 유저 정보들, 소셜별 of메소드들은 각각 소셜 로그인 api 에서 제공하는 식별값 등등을 저장하고 build
	public static OAuthAttributes of(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes)
	{
		if(socialType == SocialType.NAVER)
		{
			return ofNaver(userNameAttributeName, attributes);
		}
		else if(socialType == SocialType.KAKAO)
		{
			return ofKakao(userNameAttributeName, attributes);
		}
		
		return ofGoogle(userNameAttributeName, attributes);
	}
	
	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes)
	{
		return OAuthAttributes.builder()
				.userNameAttributeKey(userNameAttributeName)
				.oauth2User(new KakaoOAuth2UserInfo(attributes))
				.build();
	}
	
	private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes)
	{
		log.info(userNameAttributeName);
		return OAuthAttributes.builder()
				.userNameAttributeKey(userNameAttributeName)
				.oauth2User(new NaverOAuth2UserInfo(attributes))
				.build();
	}
	
	private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes)
	{
		return OAuthAttributes.builder()
				.userNameAttributeKey(userNameAttributeName)
				.oauth2User(new GoogleOAuth2UserInfo(attributes))
				.build();
	}
	
	// of 메소드로 OAuthAttributes 객체가 생성돼서, 유저 정보들이 담긴 info 가 소셜 타입별로 주입됨
	// userType 은 GUEST
	// 이후에 CustomOAuth2UserService 에서 DB 에 저장될 것임
	public User toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo)
	{
		User user = new User();
				
		if(oauth2UserInfo.getEmail() != null)
		{
			user.setUserEmail(oauth2UserInfo.getEmail());
		}
		
		if(oauth2UserInfo.getId() != null)
		{
			user.setUserPwd(oauth2UserInfo.getId());
		}
		
		if(oauth2UserInfo.getName() != null)
		{
			user.setUserName(oauth2UserInfo.getName());
		}
		
		user.setSocialType(socialType.toString());
		user.setUserType("GUEST");
		
		return user;
	}
}
