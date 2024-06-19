package com.todoc.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.Presc;
import com.todoc.web.dto.Reserve;
import com.todoc.web.dto.Untact;

@Mapper
public interface UntactDao {

	List<Untact> subjectList(Untact untact);
	
	Untact selectClinicDetail(Untact untact);
	
	List<Untact> reviewList(Untact untact);

	int subjectListCount(Untact untact);

	int insertReservation(Reserve rsve);
	
	Reserve reserveCheck(Reserve rsve);
	
	Untact precriptionWrite(Untact untact);

	int prescriptionInsert(Presc presc);

	int getprescriptionSeq();
	
	List<Presc> prescriptionDetail(Presc presc);

	int updateReserStatus(int reservationSeq);

	String getClinicInfo(String clinicInstinum);

	int updatePrescriptionStatus(int reservationSeq);

}
