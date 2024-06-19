package com.todoc.web.dto;

import lombok.Data;

@Data
public class MegazinesFile {
	

	private long newsSeq;			//게시물 번호
	private short fileSeq;			//파일번호(MX + 1)
	private String fileOrgName;		//원본 파일명
	private String fileName;		//파일명
	private String fileExt;			//파일확장자
	private long fileSize;			//파일 크기
	private String regDate;			//등록일

}
