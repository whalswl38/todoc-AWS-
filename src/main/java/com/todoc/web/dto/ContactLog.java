package com.todoc.web.dto;

import lombok.Data;

@Data
public class ContactLog {
	private long contactSeq;
    private String userEmail;
    private String clinicName;
    private String status;
    private String contactDate;
    private String reservationSymptom;
    private String clinicInstinum;
    private String userName;
    private String advice;
    
    //병원테이블
    private String clinicDoctor;
    
    //결제내역
    private int payPrice;
    private String payDate;
    
    //페이지
    private long startRow;
    private long endRow;
    
    private long reviewSeq;
    
    //민지 추가
    private long reservationSeq;
    private String reservationStatus;
}
