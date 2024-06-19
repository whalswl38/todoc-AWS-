package com.todoc.web.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.util.StringUtils;

import com.todoc.web.dto.ClinicContact;
import com.todoc.web.dto.Paging;
import com.todoc.web.dto.ReservationContact;
import com.todoc.web.dto.Reserve;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.security.jwt.JwtProperties;
import com.todoc.web.service.ClinicContactService;
import com.todoc.web.util.StringUtil;


@Controller
@RequestMapping
public class ClinicContactController {

	@Autowired
	private ClinicContactService clinicContactService;
	
	private final JwtAuthorizationFilter jwtFilter;
	
	public ClinicContactController(JwtAuthorizationFilter jwtFilter){
		this.jwtFilter = jwtFilter;
	}
	private static final Logger log = LoggerFactory.getLogger(ClinicContactController.class);
	
	private static final int LIST_COUNT = 10;
	private static final int PAGE_COUNT = 5;

	//과목선택
	@GetMapping("/clinic-contact-subject-page")
	public String selectSubject() {
		return "contact/selectSubject";
	}
	
	//증상선택
	@GetMapping("/clinic-contact-item-page")
	public String selectItem() {
		return "contact/selectItem";
	}
	
	

	
	//리스트 페이지 카테고리
	@GetMapping("/clinic-contact-category-list")
	public String clinicListCategory(
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "searchValue", required = false) String searchValue,
			@RequestParam(value = "clinicNight", required = false) String clinicNight,
			@RequestParam(value = "clinicWeekend", required = false) String clinicWeekend, 
			@RequestParam(value = "textSearch", required = false) String textSearch, 
			@RequestParam(value = "guValue",required=false) Integer guValue,    
			@RequestParam(value = "isOpening", required = false) String isOpening,
			@RequestParam(value = "locationValue", required = false) String locationValue,
			@RequestParam(value = "curPage", required = false) Integer curPage,
			Model model){
		List<ClinicContact> list = new ArrayList<>();
		ClinicContact search=new ClinicContact();
		long totalCount = 0;
		Paging paging = null;

		
		
		search.setCategory(category);
		search.setSearchValue(searchValue);
		search.setClinicNight(clinicNight);
		search.setClinicWeekend(clinicWeekend);
		search.setTextSearch(textSearch);
		

		
		//구 지역 검색 인덱스 로 변환(현재위치구/선택구)
		if(locationValue != null) {
			int locationIndex = search.getGuList().indexOf(locationValue);

			if (locationIndex != -1) {
			    search.setGuValue(locationIndex); 
			} else {
			}
			
		}else {
			search.setGuValue(guValue); 
		}

		
		List runningNumList = new ArrayList<>();
		runningNumList = clinicContactService.clinicRunningList();
		for(int i = 0; i < runningNumList.size();i++) {  
		}
		
		//영업중인 병원 번호 리스트
		if("Y".equals(isOpening)) {
			if(runningNumList == null || runningNumList.isEmpty()) {
				runningNumList.add(0, "");
				search.setRunningNumList(runningNumList);
			}
			search.setRunningNumList(runningNumList);
		}
		
		totalCount = clinicContactService.listCount(search);
		
		
		
		if(totalCount > 0) {
			if(curPage==null) {
				curPage = 1;
			}
			paging = new Paging("/clinic-contact-category-list", totalCount, LIST_COUNT,PAGE_COUNT, curPage, "curgPage");
			search.setStartRow(paging.getStartRow());
			search.setEndRow(paging.getEndRow());
			
		}
		

		list = clinicContactService.clinicListCategory(search);
		
		  for (ClinicContact clinic : list) {
	            System.out.println(clinic);
	        }


		model.addAttribute("search",search);
		model.addAttribute("clinicList", list);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("category", category);
		model.addAttribute("textSearch", textSearch);
		model.addAttribute("guValue", guValue);
		model.addAttribute("isOpening", isOpening);
		model.addAttribute("runningNumList", runningNumList);
		model.addAttribute("curPage",curPage);
		model.addAttribute("paging",paging);
		
		

		return "contact/clinicList";
	}

		 

	@GetMapping("/clinic-contact-detail-page")
	public String clinicDetail(HttpServletRequest request,@RequestParam("clinicInstinum") String clinicInstinum, Model model) {
		
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	String loginFlag = null;
    	if(userEmail == null) {
    		loginFlag = "N";
    	}else {
    		loginFlag = "Y";
    	}
		
		ClinicContact clinicContact = new ClinicContact();
		clinicContact = clinicContactService.clinicDetail(clinicInstinum);

		//요일별 진료시간
		List<String> clinicTimeList = new ArrayList<>(); 
		String[] clinicTime = clinicContact.getClinicTime().split(",");	
		
		for(int i = 0; i < clinicTime.length ; i++) { 
			clinicTimeList.add(clinicTime[i]); 
		}
		
		//요일
		List<String> clinicDayList = new ArrayList<>();
		String[] clinicDay = clinicContact.getClinicDay().split(",");
		
		for(int i = 0; i < clinicDay.length; i++) {
			clinicDayList.add(clinicDay[i]);
		}
		
		//진료과목 
		List<String> clinicSubjectList = new ArrayList<>();
		String[] clinicSubject = clinicContact.getClinicSubject().split(",");
		
		for(int i = 0; i < clinicSubject.length; i++) {  
			clinicSubjectList.add(clinicSubject[i]);
		}
		
		//경력사항
		String clinicCareer = clinicContact.getClinicCareer();
		List<String> careerList = new ArrayList<>();

		if (clinicContact.getClinicCareer() != null && !clinicContact.getClinicCareer().trim().isEmpty()) {
		    String[] career = clinicCareer.split(",");
		    for (String careerItem : career) {
		        careerList.add(careerItem);
		    }
		}


		model.addAttribute("clinic", clinicContact);
		model.addAttribute("clinicTimeList", clinicTimeList);
		model.addAttribute("clinicDayList", clinicDayList);
		model.addAttribute("clinicSubjectList", clinicSubjectList);
		model.addAttribute("careerList", careerList);
		model.addAttribute("loginFlag", loginFlag);



		return "contact/clinicDetail";
	}
	
	//대면 병원 예약페이지
    @GetMapping("/contact-clinic-reserve-page")
    public String reservation(@RequestParam(value = "clinicInstinum", required = false) String clinicInstinum,
    HttpServletRequest request,Model model) {

    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	if(userEmail == null) {
    		return "login/login";
    	}
    	
    	//현재 시점의 최초 timeSlots
    	List<String> timeSlots = clinicContactService.reserveTimebutton(clinicInstinum);    	

        model.addAttribute("timeSlots", timeSlots);
        model.addAttribute("clinicInstinum",clinicInstinum);
    	
        //공휴일플래그
        String holidayFlag = clinicContactService.isHoliday(clinicInstinum);

        model.addAttribute("holidayFlag",holidayFlag);
    	
    	return "contact/clinicReservation";
    }
    
    //예약확인 ajax로 
    @PostMapping("/reservationPost")
    @ResponseBody
	public int reservationPost(HttpServletRequest request, @RequestParam("comments") String comments,@RequestParam("selectedDate") String selectedDate, @RequestParam("selectedTime") String selectedTime, @RequestParam("clinicInstinum") String clinicInstinum ){
    	
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	

    	if(!StringUtils.isEmpty(userEmail)) {
    		
	    		if(!StringUtils.isEmpty(clinicInstinum)){
	    			
	    			ClinicContact clinic = new ClinicContact();
	    			
	    			clinic = clinicContactService.clinicDetail(clinicInstinum);
	    			//해당 병원 요일별 진료시간 
	    			String[] clinicTime = clinic.getClinicTime().split(",");
	    			
			    	if(!StringUtils.isEmpty(comments) && !StringUtils.isEmpty(selectedDate) &&!StringUtils.isEmpty(selectedTime)) {
			    	     
			    		if(clinicContactService.dayCheck(selectedDate,selectedTime,clinicTime)) {
			    	    	 
			    	    	 
			    	    	 ReservationContact reservation = new ReservationContact();
			    	    	 reservation.setClinicInstinum(clinicInstinum);
			    	    	 reservation.setReservationDate(selectedDate);
			    	    	 reservation.setReservationTime(selectedTime);
			    	    	 reservation.setReservationSymptom(comments);
			    	    	 reservation.setUserEmail(userEmail);
			    	    	 
			    	    	 if(clinicContactService.reservationInsert(reservation)>0) {
			    	    		 
			    		   	 
			    		   	 
			    			    return 0;	
			    	    	 }else {
			    	    		 return 202;
			    	    	 }
			    	     }else {
			    	     }
			    			
			    	}else {
			    		
			    	}
	    			
	    		}else {
	    			
	    		}
	    	 
	    }else {
	    	return 101;
	    }
    	return 404;    	

    }

    @ResponseBody 
    @GetMapping("/getAvailableTimeSlots")
    public ResponseEntity<?> getAvailableTimeSlotsY(@RequestParam("selectedDate") String selectedDate, @RequestParam("clinicInstinum") String clinicInstinum, @RequestParam(value = "_isHoliday", required = false) String _isHoliday) {
       

    	List<String> timeSlots = null;
    	
    	timeSlots = clinicContactService.reserveTimebutton(selectedDate,clinicInstinum,_isHoliday);    


    	
    	return ResponseEntity.ok(timeSlots);
    }
    


    

    


}

