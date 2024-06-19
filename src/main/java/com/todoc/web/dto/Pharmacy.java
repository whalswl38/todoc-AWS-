package com.todoc.web.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Data
public class Pharmacy 
{
	private String pharmacyInstinum;
	private String pharmacyRegnum;
	private String pharmacyPhone;
	private String pharmacyName; // 약국명
	private String pharmacistName;
	private String userEmail;
	private String userPwd;
	private String pharmacyDayoff;
	private String pharmacyTime;
	private String pharmacyZipcode;
	private String pharmacyAddr;
	private String pharmacyStatus;
	private String pharmacyRefreshToken;
	private String pharmacyFax;
	private String pharmacyCareer;
	
	public Pharmacy encodePassword(PasswordEncoder passwordEncoder) 
	{
		this.userPwd = passwordEncoder.encode(this.userPwd);
		return this;
	} 
}
