package com.todoc.web.dto;

import lombok.Data;

@Data
public class Presc {
	private long reservationSeq;
	private String clinicInstinum;
	private String prescriptionSeq;
	private String medi;
	private String dose;
	private String order;
	private String userEmail;
	private String prescriptionDate;
	private String userIdentity;
	private int medicineSingleDose;
	private int medicineDailyDose;
	private String clinicName;
	private String clinicPhone;
	private String clinicDoctor;
	private String userName;
	
	
}


