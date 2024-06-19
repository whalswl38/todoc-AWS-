package com.todoc.web.dto;

import lombok.Data;

@Data
public class ReservationContact {

	private long reservationSeq;
	private String userEmail;
	private String clinicInstinum;
	private String reservationDate;
	private String reservationTime;
	private String reservationSymptom;
	private String reservationFlag;
	private String regdate;
	private String reservationStatus;
	
	//예약확인 리스트(승준)
	private String userName;
	private String clinicDoctor;
	private String clinicName;
	private String status;
	private String doctorEmail;
	private long startRow;
	private long endRow;
	



	
	
	
	
	
	
	
}
