package com.todoc.web.dto;

import lombok.Data;

@Data
public class Megazines {
	
	private long newsSeq;
	private String userEmail;
	private String newsTitle;
	private String newsSubtitle;
	private String newsContent;
	private String newsDepartment;
	private String newsSymptom;
	private String newsRegDate;
	private String newsUpdateDate;
	private int newsReadCnt;
	private int newsLikeCnt;
	private String newsDelFlag;
	private String newsThumbnail;
	
	private String newsFilter;	//정렬필터타입(1:최신순, 2: 조회, 3: 추천)
	private String searchValue;			//검색 값(진료과,증상,제목,내용)
	private long startRow;				//시작 rownum
	private long endRow;				//끝 rownum

	private MegazinesFile megazinesFile;

	public Megazines() {
		
		newsFilter = "";
		searchValue = "";
		
		
		
	}
	
	
	
}
