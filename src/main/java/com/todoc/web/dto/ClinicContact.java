package com.todoc.web.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ClinicContact {
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
	
	private String searchValue;	
	private String category;
	private String textSearch;	
	private Integer guValue;
	private String guName;
	private List runningNumList;
	private List<String> guList;
	private String closeFlag;
	
	private long startRow;
	private long endRow;
	
	private String fileName;	
	private String fileOrgName;	
	

    public ClinicContact() {
    	guList = new ArrayList<>();
    	guList.add("강남구");
    	guList.add("강동구");
    	guList.add("강북구");
    	guList.add("강서구");
    	guList.add("관악구");
    	guList.add("광진구");
    	guList.add("구로구");
    	guList.add("금천구");
    	guList.add("노원구");
    	guList.add("도봉구");
    	guList.add("동대문구");
    	guList.add("동작구");
    	guList.add("마포구");
    	guList.add("서대문구");
    	guList.add("서초구");
    	guList.add("성동구");
    	guList.add("성북구");
    	guList.add("송파구");
    	guList.add("양천구");
    	guList.add("영등포구");
    	guList.add("용산구");
    	guList.add("은평구");
    	guList.add("종로구");
    	guList.add("중구");
    	guList.add("중랑구");
    }
	
	

	
	
}