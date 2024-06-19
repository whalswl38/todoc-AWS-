package com.todoc.web.dao;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.Clinic;
import com.todoc.web.dto.ClinicFile;
import com.todoc.web.dto.Institution;
import com.todoc.web.dto.Pharmacy;
import com.todoc.web.dto.StampFile;
import com.todoc.web.dto.User;

@Mapper
public interface UserDao 
{	
	// 리프레쉬 토큰 업데이트
	void refreshTokenUpdate(User user);
	
	// 병원 리프레쉬 토큰 업데이트
	void clinicRefreshTokenUpdate(Clinic clinic);
	
	// 약국 리프레쉬 토큰 업데이트
	void pharmRefreshTokenUpdate(Pharmacy pharmacy);
	
	// 이메일로 회원 확인
	User findByEmail(String userEmail);
	
	// 이메일로 병원 회원 확인
	Clinic findClinicEmail(String userEmail);
	
	// 이메일로 약국 회원 확인
	Pharmacy findPharmEmail(String userEmail);
	
	// 회원가입
	int userInsert(User user);
	
	// 병의원, 약국 회원가입시 요양기관 가입여부 확인
	Institution findInstitution(String institutionNum);

	// 이메일 중복확인
	int checkEmail(String userEmail);
	
	// 병원 사진 등록
	int insertClinicFile(ClinicFile clinicFile) throws Exception;
	
	// 인감 사진 등록
	int insertStampFile(StampFile stampFile) throws Exception;
	
	// 약국 회원가입
	int insertPharmacy(Pharmacy pharmacy);
	
	// 병원 회원가입
	int insertClinic(Clinic clinic);
	
	// 일반 유저 아이디/비밀번호 찾기
	User findUser(User user);
	
	// 병원 유저 아이디/비밀번호 찾기
	Clinic findClinic(Clinic clinic);
	
	// 약국 유저 아이디/비밀번호 찾기
	Pharmacy findPharmacy(Pharmacy pharmacy);
	
	// 비밀번호 수정
	int updateUser(User user);
	
	int updatePharm(Pharmacy pharmacy);
	
	int updateClinic(Clinic clinic);
	
	// 소셜로그인 회원 정보 저장
	int insertSocial(User user);
	
	// 제휴
	int alliance(Institution institution);
	
	/*승준*/
	// 회원정보 수정
	int userUpdate(User user);
	
	// 소셜로그인 회원 정보 추가 입력
	int oauthUpdate(User user);

	// 소셜로그인 키값으로 조회
	User findByPwd(String userPwd);
	
	//의사 약국 회원정보수정
	int updateMedicalClinic(Clinic clinic);
	
	//의사 사진파일
	ClinicFile clinicFileView(String clinicInstinum);
	
	//인감 사진파일
	StampFile clinicStampFileView(String userEmail);
	
	//의사 사진파일 삭제
	int clinicFileDelete(String clinicInstinum);
	//인감 사진파일 삭제
	int clinicStampDelete(String userEmail);
}

