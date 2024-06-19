package com.todoc.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PayLog {
	
	private String paySeq;
	private String payMethod;
	private String payPrice;
	private String payDate;
	private String payFlag;
	private String userEmail;
	private int reservationSeq;
}
