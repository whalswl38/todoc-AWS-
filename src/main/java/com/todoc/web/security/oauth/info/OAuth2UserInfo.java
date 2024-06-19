package com.todoc.web.security.oauth.info;

import java.util.Map;

// 소셜 타입별로 유저 정보를 가지는 추상클래스
// 해당 클래스를 상속받아서 각 소셜 타입의 유저 정보 클래스 구현
public abstract class OAuth2UserInfo 
{
	protected Map<String, Object> attributes;
	
	public OAuth2UserInfo(Map<String, Object> attributes)
	{
		this.attributes = attributes;
	}
	
	public abstract String getId();
	public abstract String getEmail(); // pk
	public abstract String getName();
}
