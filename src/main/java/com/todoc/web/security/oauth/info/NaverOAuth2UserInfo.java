package com.todoc.web.security.oauth.info;

import java.util.Map;

// 네이버는 response key 안에 사용할 정보들이 있기 때문에 key 로 먼저 꺼낸 후애 사용할 정보를 get 해준다
public class NaverOAuth2UserInfo extends OAuth2UserInfo
{
	public NaverOAuth2UserInfo(Map<String, Object> attributes)
	{
		super(attributes);
	}
	
	@Override
	public String getId()
	{
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return response.get("id").toString();
	}
	
	@Override
	public String getName()
	{	
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return response.get("name").toString();
	}
	
	@Override
	public String getEmail()
	{
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return response.get("email").toString();
	}
}
