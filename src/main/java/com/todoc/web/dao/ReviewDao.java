package com.todoc.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.Review;

@Mapper
public interface ReviewDao {
	
	//후기 리스트
	List<Review> reviewList(String userEmail);
	
	//후기 리스트 더보기
	List<Review> reviewListPlus(Review review);
	
	//후기 리스트 토탈카운트
	int reviewTotal(String userEmail);
	
	//후기 작성
	int reviewInsert(Review review);
	
	//리뷰번호로 리뷰정보
	Review reviewSeqList(long reviewSeq);
	
	//리뷰 수정
	int reviewUpdate(Review review);
	
	//리뷰 삭제
	int reviewDelete(Review review);
}
