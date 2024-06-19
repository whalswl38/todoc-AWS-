package com.todoc.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.ClinicContact;
import com.todoc.web.dto.ReservationContact;
import com.todoc.web.dto.Reserve;


@Mapper
public interface ClinicContactDao {
	/*희주*/
	//병원 리스트 조회 
	public List <ClinicContact> clinicList();
	
	//병원 리스트 조회(category)
	public List <ClinicContact> clinicListCategory(ClinicContact search);
	
	//게시물 수
	public long listCount(ClinicContact search);
	
	//병원 리스트 total
	public List<ClinicContact> clinicListTotal(ClinicContact search);
	
	//영업시간 전체 리스트
	public List<ClinicContact> clinicTimeList();
	
	//병원 상세페이지 조회
	public ClinicContact clinicDetail(String clinicInstinum);
	
	//대면 예약
	public int reservationInsert(ReservationContact reservation);
	
	//병원 오늘 영업시간
	public ClinicContact timeOnly(String clinicInstinum);
	
	//해당날짜 이미 예약된 시간대 찾기
	public List<ReservationContact> reservedTime(ReservationContact reservation);
	
	//대면 예약 확인
	public ReservationContact contactReservationCheck(ReservationContact ReservationContact);
	
	/*승준*/
	//예약확인 리스트
	List<ReservationContact> reservationList(String clinicInstinum);

	//예약확인 의사 상세
	ClinicContact clinicListView(String userEmail);
	
	//예약리스트 승인
	int reservationApprove(long reservationSeq);
	
	//예약리스트 취소
	int reservationCancel(long reservationSeq);
	
	//예약 승인리스트 토탈 카운트
	int reservationListTotal(String clinicInstinum);
	
	//진료 대기 리스트 토탈 카운트
	int contactListTotal(String clinicInstinum);
	
	//이메일로 병원정보 불러오기
	ClinicContact clinicfindByEmail(String userEmail);
	/////////////////////////// 5/22 승준코드
	//testTime
	ClinicContact testTime(String userEmail);
	
	//마이페이지 진료내역 불러오기
	ReservationContact mypageReservationList(String userEmail);
	
	//예약번호로 리스트
	ReservationContact resrvationClickMapping(long reservationSeq);
	
	//예약상태 진료입장으로 변경
	int reservationStatusUpdate(long reservationSeq);
	
	//예약상태 진료완료
	int streamEnd(long reservationSeq);
	
	//예약취소
	int contactCancel(long reservationSeq);
	
	//대면 진료리스트
	List<ReservationContact> contactLogList2(ReservationContact reservationContact);
	
	//대면 진료리스트 토탈
	int contactLogTotal2(String userEmail);
	/////////////////////////////////// 5/24 승준코드///////////////////////////////////
	
		
	

	
	

}
