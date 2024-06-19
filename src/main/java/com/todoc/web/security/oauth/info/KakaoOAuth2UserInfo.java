package com.todoc.web.security.oauth.info;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo
{
    public KakaoOAuth2UserInfo(Map<String, Object> attributes)
    {
        super(attributes);
    }

    @Override
    public String getId() 
    {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() 
    {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties != null) return (String) properties.get("nickname");
        return null;
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null) return (String) kakaoAccount.get("email");
        return null;
    }
}
