package com.todoc.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.todoc.web.dto.ContactLog;
import com.todoc.web.dto.Paging;
import com.todoc.web.dto.ReservationContact;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.service.ClinicContactService;
import com.todoc.web.service.ContactLogService;
import com.todoc.web.service.ReviewService;

@Controller
@RequestMapping
public class ContactLogController {
	
	private static Logger logger = LoggerFactory.getLogger(ContactLogController.class);
	
	private static final int LIST_COUNT = 5;	//한 페이지의 게시물 수
	private static final int PAGE_COUNT = 5;	//페이징 수 
	
	@Autowired
	private ContactLogService contactLogService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ClinicContactService clinicContactService;
	
	private final JwtAuthorizationFilter jwtFilter;
	
	public ContactLogController(JwtAuthorizationFilter jwtFilter)
	{
		this.jwtFilter = jwtFilter;
	}
	
	 @GetMapping("/medical-history-page")
     public String contactList(HttpServletRequest request, Model model, @RequestParam(value="curPage", defaultValue= "1") long curPage) {
		 
		 List<ContactLog> list = null;
		 ContactLog contactLog = new ContactLog();
		 
		 String token = jwtFilter.extractJwtFromCookie(request);
    	 String userEmail = jwtFilter.getUsernameFromToken(token);
	     Paging paging = null;
	     
	     long totalCount = 0;
	     
	     totalCount = contactLogService.contactLogTotal(userEmail);
	     
    	 if(userEmail != null)
    	 {
    		 paging = new Paging("/medical-history-page", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
    		 
    		 contactLog.setStartRow(paging.getStartRow());
    		 contactLog.setEndRow(paging.getEndRow());
    		 contactLog.setUserEmail(userEmail);
    		 
    		 list = contactLogService.contactList(contactLog);
    		 
    		 
    		 for(int i =0; i< list.size(); i++)
        	 {
        		 logger.error("list++++++++" + list.get(i));
        	 }
    	 }
    	 else
 		 {
 			 return "redirect:login-page";
 		 }
	     
    	 model.addAttribute("paging", paging);
    	 model.addAttribute("list", list);
	     model.addAttribute("curPage", curPage);
		 
        return "mypage/medicalHistory";
     }
	 
	 
	 
	//대면 진료리스트
    @GetMapping("/contactHistory-page")
    public String contactHistory(HttpServletRequest request, Model model, @RequestParam(value="curPage", defaultValue= "1") long curPage) 
    {
    	 List<ReservationContact> list = null;
		 ReservationContact reservationContact = new ReservationContact();
		 
		 String token = jwtFilter.extractJwtFromCookie(request);
		 String userEmail = jwtFilter.getUsernameFromToken(token);
	     Paging paging = null;
	     
	     long totalCount = 0;
	     
	     totalCount = clinicContactService.contactLogTotal2(userEmail);
	     
	     logger.error("totalCount : " + totalCount );
	     logger.error("userEmail : " + userEmail );
	     
	   	 if(userEmail != null)
	   	 {
	   		 paging = new Paging("/contactHistory-page", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
	   		 
	   		 logger.error("paging : " + paging);
	   		 
	   		 reservationContact.setStartRow(paging.getStartRow());
	   		 reservationContact.setEndRow(paging.getEndRow());
	   		 reservationContact.setUserEmail(userEmail);
	   		 
	   		 logger.error("reservationContact : " + reservationContact);
	   		 
	   		 list = clinicContactService.contactLogList2(reservationContact);
	   		 
	   		 
	   		 for(int i =0; i< list.size(); i++)
	       	 {
	       		 logger.error("list++++++++" + list.get(i));
	       	 }
	   	 }
	   	 else
		 {
			 return "redirect:login-page";
		 }
		     
		   	 model.addAttribute("paging", paging);
		   	 model.addAttribute("list", list);
		     model.addAttribute("curPage", curPage);
	     
	     
	       return "mypage/contactHistory";
	    }
	 
	 @GetMapping("/medical-history-detail-page")
	    public String contactViewList(HttpServletRequest request, Model model, @RequestParam(value="contactSeq", defaultValue="0") long contactSeq) 
	 	{
		 	 
		 	ContactLog contactLog = null;
		 	
			 String token = jwtFilter.extractJwtFromCookie(request);
	    	 String userEmail = jwtFilter.getUsernameFromToken(token);
	    	 
	    	 
	    	 if(!userEmail.isEmpty())
	    	 {
	    		 logger.error("contactSeq : " + contactSeq);
	    		 
	    		 if(contactSeq != 0)
	    		 {
	    			 contactLog = contactLogService.contactViewList(contactSeq);
	    			 int reviewCheck = contactLogService.contactSeqCheck(contactSeq);
	    			 
	    			 logger.error("contactLog : " + contactLog);
	    			 logger.error("reviewCheck : " + reviewCheck);
	    			 
	    			 
	    			 if(contactLog != null && reviewCheck >= 0)
	    			 {
	    				 model.addAttribute("contactLog", contactLog);
	    				 model.addAttribute("reviewSeq", reviewCheck);
	    			 }
	    			 else
	    			 {
	    				 return "redirect:/main-page";
	    			 }
	    			 
	    		 }
	    	 }
	    	 else
	 		 {
	 			 return "redirect:login-page";
	 		 }
		     
	    	 
		 
	        return "mypage/medicalHistoryDetail";
	    }
	
}
