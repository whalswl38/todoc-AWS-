package com.todoc.web.security.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

// 병원, 약국, 일반 회원, 관리자 계정을 하나의 Dto 에 세팅해서 토큰 생성
// UserPrincipal implements UserDetails 여기에 전달할 때 사용할 것
@Data
public class LoginDto 
{
	private String username;
	private String password;
	private String userType;
	
	public LoginDto encodePassword(PasswordEncoder passwordEncoder) 
	{
		this.password = passwordEncoder.encode(this.password);
		return this;
	} 
}