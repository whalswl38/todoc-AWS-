package com.todoc.web.dto;

import lombok.Data;

@Data
public class Untact {
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
	private String clinicCareer;
	private String clinicBreak;
	private String clinicNight;
	
    private int fileSeq;
    private String fileOrgName;
    private String fileName;
    private String fileExt;
    private long fileSize;
    private String fileRegDate;
    
	private int reviewSeq;
	private String reviewTitle;
	private String reviewContent;
	private String reviewRegdate;
	private String reviewGrade;
	private String userName;
	
	private long reservationSeq;
	private String userIdentity;
	private String reservationDate;

    private String searchWord;

    private long startRow;
    private long endRow;
    private String sortType;
    private String location;
    
    
}


