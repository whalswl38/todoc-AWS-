package com.todoc.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoc.web.dao.ContactLogDao;
import com.todoc.web.dto.ContactLog;
import com.todoc.web.dto.ReservationContact;

@Service
public class ContactLogService {
	@Autowired
	private ContactLogDao contactLogDao;
	
	//진료내역 리스트
	public List<ContactLog> contactList(ContactLog contactLog)
	{
		return contactLogDao.contactList(contactLog);
	}
	
	//진료세부내역
	public ContactLog contactViewList(long contactSeq)
	{
		return contactLogDao.contactViewList(contactSeq);
	}
	
	//진료내역 토탈카운트
	public int contactLogTotal(String userEmail)
	{
		return contactLogDao.contactLogTotal(userEmail);
	}
	
	//진료내역 리뷰체크
	public int contactSeqCheck(long contactSeq)
	{
		return contactLogDao.contactSeqCheck(contactSeq);
	}
	
	//진료내역 대면
	public List<ReservationContact> contactList2(ReservationContact reservationContact)
	{
		return contactLogDao.contactList2(reservationContact);
	}
	
	//진료내역 작성
	public int contactLogInsert(ContactLog contactLog)
	{
		return contactLogDao.contactLogInsert(contactLog);
	}
}
