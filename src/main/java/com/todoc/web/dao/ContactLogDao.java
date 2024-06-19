package com.todoc.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.ContactLog;
import com.todoc.web.dto.ReservationContact;

@Mapper
public interface ContactLogDao {
	
	//진료내역 리스트
	List<ContactLog> contactList(ContactLog contactLog);
	
	//진료세부내역
	ContactLog contactViewList(long contactSeq);
	
	//진료내역 토탈카운트
	int contactLogTotal(String userEmail);
	
	//진료내역 리뷰체크
	int contactSeqCheck(long contactSeq);
	
	//진료내역 대면
	List<ReservationContact> contactList2(ReservationContact reservationContact);
	
	//진료내역 작성
	int contactLogInsert(ContactLog contactLog);
}
