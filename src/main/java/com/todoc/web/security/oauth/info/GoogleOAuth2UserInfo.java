package com.todoc.web.security.oauth.info;

import java.util.Map;

// 구글 소셜로그인할 때 받아올 값
public class GoogleOAuth2UserInfo extends OAuth2UserInfo
{
	public GoogleOAuth2UserInfo(Map<String, Object> attributes)
	{
		super(attributes);
	}
	
	@Override
	public String getId()
	{
		return attributes.get("sub").toString();
	}
	
	@Override
	public String getName()
	{
		return attributes.get("name").toString();
	}
	
	@Override
	public String getEmail()
	{
		return attributes.get("email").toString();
	}
}
