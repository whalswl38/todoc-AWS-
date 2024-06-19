package com.todoc.web.dto;

import lombok.Data;

@Data
public class MegazinesLike {


	private long likeNewsSeq;//좋아요 번호
	private String userEmail;	//좋아요 누른 아이디
	private long newsSeq;	//좋아요 누른 아이디

	
}
