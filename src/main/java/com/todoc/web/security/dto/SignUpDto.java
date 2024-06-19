package com.todoc.web.security.dto;


import javax.validation.constraints.*;

import lombok.Data;

// 병원이랑 약국 회원가입용
@Data
public class SignUpDto 
{
	private String userType; // 가입유형
	
	private String contactType; // 진료방식
	
	private String night; // 야간 진료 여부
	
	private String weekend; // 주말 진료 여부
	
	private String breakTime; // 점심시간
	
	@NotEmpty(message="요양기관번호는 필수 입력 값입니다.")
	private String institutionNum; // 요양기관번호
	
	@NotEmpty(message="요양기관명은 필수 입력 값입니다.")
	private String institutionName; // 요양기관명
	
	@NotEmpty(message="사업자등록번호는 필수 입력 값입니다.")
	private String regNum; // 사업자등록번호
	
	@NotBlank(message="이메일은 필수 입력 값입니다.")
	@Email(message="이메일 형식이 올바르지 않습니다.")
	private String userEmail; // 이메일
	
	@NotEmpty(message="비밀번호는 필수 입력 값입니다.")
	private String userPwd; // 비밀번호
	
	@NotEmpty(message="우편번호는 필수 입력 값입니다.")
	private String zipcode; // 우편번호
	
	@NotEmpty(message="주소는 필수 입력 값입니다.")
	private String addr; // 주소
	
	private String detail; // 상세설명
	
	private String career;
	
	@NotEmpty(message="진료일은 필수 입력 값입니다.")
	private String dayOn; // 진료일
	
	@NotEmpty(message="진료시간은 필수 입력 값입니다.")
	private String dayTime; // 진료시간
	
	private String dayOff; // 휴무일
	
	//@NotEmpty(message="진료과목는 필수 입력 값입니다.")
	private String subject; // 진료과목
	
	//@NotEmpty(message="진료항목은 필수 입력 값입니다.")
	private String symptop; // 증상 == 진료항목
	
	@NotEmpty(message="전화번호는 필수 입력 값입니다.")
	private String userPhone; // 전화번호
	
	private String faxNum; // 팩스 번호
	
	@NotEmpty(message="이름은 필수 입력 값입니다.")
	@Size(min=2, message="이름은 2자 이상 입력하셔야합니다.")
	private String userName; // 이름	
	
	private String homePageAdd; // 홈페이지 주소 -> 상세 설명 컬럼에 같이 들어감
	
}
