package com.todoc.web.security.dto;

import lombok.Data;

// 아이디, 비밀번호 찾을 때 사용할 dto
@Data
public class FindDto 
{
	private String userType; // 일반 / 약국병원
	private String userName; // 이름
	private String userPwd; // 생년월일 / 전화번호
	private String searchType; // 어떤 걸로 회원 정보 찾는지 (생년월일 / 전화번호 / 사업자등록번호)
	private String findKind; // 아이디 찾는지 , 비밀번호 찾는지
}
