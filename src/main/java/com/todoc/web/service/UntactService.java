package com.todoc.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoc.web.dao.UntactDao;
import com.todoc.web.dto.Presc;
import com.todoc.web.dto.Reserve;
import com.todoc.web.dto.Untact;

@Service
public class UntactService {
	@Autowired
	private UntactDao untactDao;
	
	public List<Untact> subjectList(Untact untact) {
		
		
		return untactDao.subjectList(untact);
	}
	
	public Untact selectClinicDetail(Untact untact) {
		/*
		 * Untact untact2 = null; try { untact2 = untactDao.selectClinicDetail(untact);
		 * } catch(Exception e) { System.out.println("서비스단 오류" + e );; } return untact2;
		 */
		 return untactDao.selectClinicDetail(untact);

	}
	
	public List<Untact> reviewList(Untact untact) {
		return untactDao.reviewList(untact);
	}

	public int subjectListCount(Untact untact) {
		//과목
				if(untact.getClinicSubject() != null)
				switch (untact.getClinicSubject()) {
		        case "01":
		        	untact.setClinicSubject("피부과");
		        	break;
		        case "02":
		        	untact.setClinicSubject("산부인과");
		        	break;
		        case "03":
		        	untact.setClinicSubject("이비인후과");
		        	break;
		        case "04":
		        	untact.setClinicSubject("내과");
		        	break;
		        case "05":
		        	untact.setClinicSubject("안과");
		        	break;
		        case "06":
		        	untact.setClinicSubject("가정의학과");
		        	break;
		        case "07":
		        	untact.setClinicSubject("소아과");
		        	break;
		        case "08":
		        	untact.setClinicSubject("정형외과");
		        	break;
		        case "09":
		        	untact.setClinicSubject("정신건강의학과");
		        	break;
		        case "10":
		        	untact.setClinicSubject("비뇨기과");
		        	break;
		        case "11":
		        	untact.setClinicSubject("치과");
		        	break;
		        case "12":
		        	untact.setClinicSubject("신경외과");
		        	break;
		        default :
		        	untact.setClinicSubject("");
		        	}
				//증상
				if(untact.getClinicSymptom() != null)
				switch (untact.getClinicSymptom()) {
		        case "a":
		        	untact.setClinicSymptom("감기/몸살");
		        	break;
		        case "b":
		        	untact.setClinicSymptom("소화불량");
		        	break;
		        case "c":
		        	untact.setClinicSymptom("알러지");
		        	break;
		        case "d":
		        	untact.setClinicSymptom("비염");
		        	break;
		        case "e":
		        	untact.setClinicSymptom("여드름");
		        	break;
		        case "f":
		        	untact.setClinicSymptom("탈모");
		        	break;
		        case "g":
		        	untact.setClinicSymptom("다이어트");
		        	break;
		        case "h":
		        	untact.setClinicSymptom("인공눈물");
		        	break;
		        case "i":
		        	untact.setClinicSymptom("위염");
		        	break;
		        case "j":
		        	untact.setClinicSymptom("장염");
		        	break;
		        case "k":
		        	untact.setClinicSymptom("당뇨");
		        	break;
		        case "l":
		        	untact.setClinicSymptom("고혈압");
		        	break;
		        case "m":
		        	untact.setClinicSymptom("여성질환");
		        	break;
		        case "n":
		        	untact.setClinicSymptom("만성질환");
		        	break;
		        case "o":
		        	untact.setClinicSymptom("복통");
		        	break;
		        case "p":
		        	untact.setClinicSymptom("두통");
		        	break;
		        default :
		        	untact.setClinicSymptom("");
		    }
		return untactDao.subjectListCount(untact);
	}

	public int insertReservation(Reserve rsve) {
		return untactDao.insertReservation(rsve);
	}
	
	public Reserve reserveCheck(Reserve rsve) {
		 return untactDao.reserveCheck(rsve);
	}
	
	public Untact precriptionWrite(Untact untact) {
		return untactDao.precriptionWrite(untact);
	}

	public int prescriptionInsert(Presc presc) {
		return untactDao.prescriptionInsert(presc);
	}

	public int getprescriptionSeq() {
		return untactDao.getprescriptionSeq();
	}

	public List<Presc> prescriptionDetail(Presc presc) {
		return untactDao.prescriptionDetail(presc);
	}

	//결제완료 후 예약 상태 변경
	public int updateReserStatus(int reservationSeq) {
		return untactDao.updateReserStatus(reservationSeq);
	}

	public String getClinicInfo(String clinicInstinum) {
		return untactDao.getClinicInfo(clinicInstinum);
	}
	
	//처방완료 후 예약 상태 변경
	public int updatePrescriptionStatus(int reservationSeq) {
		return untactDao.updatePrescriptionStatus(reservationSeq);
	}
	
}
