package com.todoc.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoc.web.dao.ReviewDao;
import com.todoc.web.dto.Review;

@Service
public class ReviewService {
	@Autowired
	private ReviewDao reviewDao;
	
	private static Logger logger = LoggerFactory.getLogger(ReviewService.class);
	
	public List<Review> reviewList(String userEmail)
	{
		return reviewDao.reviewList(userEmail);
	}
	
	//후기 리스트 더보기
	public List<Review> reviewListPlus(Review review)
	{
		List<Review> list = null;
		
		try
		{
			list = reviewDao.reviewListPlus(review);
		}
		catch(Exception e)
		{
			logger.error("[ReviewService] reviewListPlus Exception",e);
		}
				
		
		return list;
	}
	
	//후기리스트 토탈카운트
	public int reviewTotal(String userEmail)
	{
		int count = 0;
		
		try
		{
			count = reviewDao.reviewTotal(userEmail);
		}
		catch(Exception e)
		{
			logger.error("[ReviewService] reviewTotal Exception",e);
		}
		
		return count;
	}
	
	//후기 작성
	public int reviewInsert(Review review)
	{
		int count = 0;
		
		try
		{
			count = reviewDao.reviewInsert(review);
		}
		catch(Exception e)
		{
			logger.error("[ReviewService] reviewInsert Exception",e);
		}
		
		return count;
	}
	
	//리뷰 번호로 리뷰정보
	public Review reviewSeqList(long reviewSeq)
	{
		Review review = null;
		
		try
		{
			review = reviewDao.reviewSeqList(reviewSeq);
		}
		catch(Exception e)
		{
			logger.error("[ReviewService] reviewSeqList Exception",e);
		}
		
		return review;
	}
	
	//리뷰 수정
	public int reviewUpdate(Review review)
	{
		int count = 0;
		
		try
		{
			count = reviewDao.reviewUpdate(review);
		}
		catch(Exception e)
		{
			logger.error("[ReviewService] reviewUpdate Exception",e);
		}
		
		return count;
	}
	
	//리뷰 삭제
	public int reviewDelete(Review review)
	{
		int count = 0;
		
		try
		{
			count = reviewDao.reviewDelete(review);
		}
		catch(Exception e)
		{
			logger.error("[ReviewService] reviewDelete Exception",e);
		}
		
		return count;
	}
}
