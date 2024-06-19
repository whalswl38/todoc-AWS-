package com.todoc.web.dto;


import lombok.Data;

@Data
public class ClinicFile 
{		
	private long fileSeq; // 파일 순서
	private String clinicInstinum;
	private String fileOrgName; // 파일 원본명
	private String fileName; // 파일명
	private String fileExt; // 파일 확장자
	private long fileSize; // 파일 크키
	private String fileRegdate; // 파일 등록일
}
