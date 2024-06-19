package com.todoc.web.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Data
public class Clinic 
{
	private String clinicInstinum;
	private String clinicRegnum;
	private String clinicPhone;
	private String clinicName;
	private String clinicSubject;
	private String clinicSymptom;
	private String userEmail;
	private String userPwd;
	private String clinicDay;
	private String clinicTime;
	private String clinicDayoff;
	private String clinicZipcode;
	private String clinicAddr;
	private String clinicNonBenefit;
	private String clinicContactFlag;
	private String clinicDetail;
	private String clinicStatus;
	private String clinicDoctor;
	private String clinicRefreshToken;
	private String clinicFax;
	private String clinicCareer;
	private String clinicBreak;
	private String clinicNight;
	private String clinicWeekend;
	
	public Clinic encodePassword(PasswordEncoder passwordEncoder) 
	{
		this.userPwd = passwordEncoder.encode(this.userPwd);
		return this;
	} 
}
