package com.todoc.web.dto;

import lombok.Data;

@Data
public class Reserve {
	private long reservationSeq;
	private String userEmail;
	private String clinicInstinum;
	private String reservationDate;
	private String reservationTime;
	private String reservationSymptom;
	private String reservationFlag;
	private String regDate;
	private String clinicName;
	private String reservationStatus;
}


