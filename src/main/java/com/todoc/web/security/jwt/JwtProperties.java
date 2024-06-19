package com.todoc.web.security.jwt;

// == jwt config
public class JwtProperties 
{	
	// secretKey
    public static final String SECRET = "jdn3920ei20eei39u38ne03u8neqjd3e98uhfuqfq";
    
    // accessToken 유지시간
    public static final int EXPIRATION_TIME = 1000*60*60*1; // 3 시간
    
    // refreshToken 유지시간
    public static final int REFRESH_EXPIRATION_TIME = 1000*60*60*24*14; // 2주
    
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";
}
