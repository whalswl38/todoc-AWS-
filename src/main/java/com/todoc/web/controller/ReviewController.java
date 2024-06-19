package com.todoc.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.todoc.web.dto.ContactLog;
import com.todoc.web.dto.Paging;
import com.todoc.web.dto.Review;
import com.todoc.web.dto.User;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.service.ContactLogService;
import com.todoc.web.service.ReviewService;
import com.todoc.web.service.UserService;

@Controller
@RequestMapping
public class ReviewController {
	
	private static Logger logger = LoggerFactory.getLogger(ReviewController.class);
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContactLogService contactLogService;
	
	private static final int LIST_COUNT = 5;	//한 페이지의 게시물 수
	private static final int PAGE_COUNT = 5;	//페이징 수 
	
	private final JwtAuthorizationFilter jwtFilter;
	
	public ReviewController(JwtAuthorizationFilter jwtFilter)
	{
		this.jwtFilter = jwtFilter;
	}
	
	/*
	@GetMapping("/review-detail-page")
    public String reviewList(HttpServletRequest request, ModelMap model) {
		
		List<Review> list = null;
		Review review = new Review();
		
		String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	list = reviewService.reviewList(userEmail);
    	
	
		if(userEmail != null)
		{
			list = reviewService.reviewList(userEmail);
		}
		else
		{
			return "redirect:login-page";
		}
		
		model.addAttribute("list", list);
		
        return "mypage/reviewDetail";
    }
	*/
	
	@GetMapping("/review-detail-page")
    public String reviewList(HttpServletRequest request, ModelMap model, @RequestParam(value="curPage", defaultValue= "1") long curPage ) {
		
		List<Review> list = null;
		Review review = new Review();
		
		String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	Paging paging = null;
    	long totalCount = reviewService.reviewTotal(userEmail);
    	
    	
    	
		if(userEmail != null && totalCount >=0)
		{
			paging = new Paging("/review-detail-page", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
			
			review.setStartRow(paging.getStartRow());
			review.setEndRow(paging.getEndRow());
			review.setUserEmail(userEmail);
			
			list = reviewService.reviewListPlus(review);
			
			for(int i=0; i < list.size(); i++)
			{
				logger.error("list : " + list.get(i));
			}
		}
		else
		{
			return "redirect:login-page";
		}
		
		model.addAttribute("paging", paging);
	    model.addAttribute("curPage", curPage);
		model.addAttribute("list", list);
		
        return "mypage/reviewDetail";
    }
	
	
	 @GetMapping("/review-page")
	    public String reviewView(HttpServletRequest request, Model model, @RequestParam(value="contactSeq", defaultValue="0") long contactSeq) {
		 	
		 	logger.error("contactSeq : " + contactSeq);
		 
		 	model.addAttribute("contactSeq", contactSeq);
		 	
	        return "mypage/review";
	    }
	
	 //리뷰 작성 ajax
	 @PostMapping("/reviewWrite")
	 @ResponseBody
	 	public int reviewWrite(@RequestBody Review review, HttpServletRequest request, HttpServletResponse response) 
	    {
		 	String token = jwtFilter.extractJwtFromCookie(request);
	    	String userEmail = jwtFilter.getUsernameFromToken(token);
	    	
	    	Review reviewInsert = new Review();
	    	
	    	
	    	ContactLog contactLog = contactLogService.contactViewList(review.getContactSeq());
	    	
	    	logger.error("contactLog : " + contactLog);
	    	
	    	if(userEmail != null)
	    	{
	    		User user = userService.findByEmail(userEmail);
	    		
	    		if(user != null)
	    		{
	    			reviewInsert.setUserEmail(userEmail);
	    			reviewInsert.setUserName(user.getUserName());
	    			reviewInsert.setReviewTitle(review.getReviewTitle());
	    			reviewInsert.setReviewContent(review.getReviewContent());
	    			reviewInsert.setReviewGrade(review.getReviewGrade());
	    			reviewInsert.setClinicInstinum(contactLog.getClinicInstinum());
	    			reviewInsert.setContactSeq(review.getContactSeq());
	    			
	    			
		    		if(!review.getReviewTitle().isEmpty() && !review.getReviewContent().isEmpty() )
		    		{
	    				if(reviewService.reviewInsert(reviewInsert) > 0)
		    			{
		    				return 0;
		    			}
	    				else
	    				{
	    					return 1;
	    				}
		    		}
		    		else
		    		{
		    			return 2;
		    		}
	    		}
	    		else
	    		{
	    			return 3;
	    		}
	    	}
	    	else
	    	{
	    		return 4;
	    	}
		 	
		 	
	 	}
	 	
	 //리뷰수정 페이지
	 @GetMapping("/reviewUpdate-page")
	    public String reviewUpdate(HttpServletRequest request, Model model, @RequestParam(value="reviewSeq", defaultValue="0") long reviewSeq) 
	   {
		    String token = jwtFilter.extractJwtFromCookie(request);
	    	String userEmail = jwtFilter.getUsernameFromToken(token);
	    	
	    	
	    	if(reviewSeq > 0)
	    	{
	    		Review review = reviewService.reviewSeqList(reviewSeq);
	    		
	    		logger.error("review : " + review);
	    		
	    		if(review != null)
	    		{
	    			model.addAttribute("review", review);
	    		}
	    	}
		 	
		 	model.addAttribute("reviewSeq", reviewSeq);
		 	
	        return "mypage/reviewUpdate";
	    }
	
	 //리뷰 수정
	 @PostMapping("/reviewUpdate")
	 @ResponseBody
	 	public int reviewUpdate(@RequestBody Review review, HttpServletRequest request, HttpServletResponse response) 
	    {
		 	String token = jwtFilter.extractJwtFromCookie(request);
	    	String userEmail = jwtFilter.getUsernameFromToken(token);
	    	
	    	Review reviewUpdate = reviewService.reviewSeqList(review.getReviewSeq());
	    	
	    	
	    	if(userEmail != null)
	    	{
	    		logger.error("reviewUpdate : " + reviewUpdate);
	    		logger.error("review : " + review);
	    		
    			reviewUpdate.setReviewTitle(review.getReviewTitle());
    			reviewUpdate.setReviewContent(review.getReviewContent());
    			reviewUpdate.setReviewGrade(review.getReviewGrade());
    			
	    		if(!review.getReviewTitle().isEmpty() && !review.getReviewContent().isEmpty() && review.getReviewGrade() >= 0)
	    		{
    				if(reviewService.reviewUpdate(review) > 0)
	    			{
	    				return 0;
	    			}
    				else
    				{
    					return 1;
    				}
	    		}
	    		else
	    		{
	    			return 2;
	    		}
	    	}
	    	else
	    	{
	    		return 3;
	    	}
		 	
	 	}
	 
	 
	 @PostMapping("/reviewDelete")
	 @ResponseBody
	 	public int reviewDelete(@RequestParam("reviewSeq") long reviewSeq, HttpServletRequest request) 
	    {
		 	String token = jwtFilter.extractJwtFromCookie(request);
	    	String userEmail = jwtFilter.getUsernameFromToken(token);
	    	
	    	if(userEmail != null)
	    	{
	    		Review review = reviewService.reviewSeqList(reviewSeq);
	    		
	    		if(review != null)
	    		{
	    			if(reviewService.reviewDelete(review) > 0)
	    			{
	    				return 0;
	    			}
	    			else
	    			{
	    				return 1;
	    			}
	    		}
	    		else
	    		{
	    			return 2;
	    		}
	    	}
	    	
	    	return 3;
	 	}
}
