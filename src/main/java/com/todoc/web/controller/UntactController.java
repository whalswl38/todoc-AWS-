package com.todoc.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.xml.Log4jEntityResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.todoc.web.dto.Paging;
import com.todoc.web.dto.PayLog;
import com.todoc.web.dto.Presc;
import com.todoc.web.dto.Reserve;
import com.todoc.web.dto.Untact;
import com.todoc.web.dto.User;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.security.jwt.JwtProperties;
import com.todoc.web.service.UntactService;
import com.todoc.web.service.UserService;
import com.todoc.web.util.StringUtil;

import lombok.val;

@Controller
@RequestMapping
public class UntactController {
	@Autowired
	private UntactService untactService;
	private UserService userService;
	private final JwtAuthorizationFilter jwtFilter;
	
	@Autowired
    private TemplateEngine templateEngine;
	
	public UntactController(JwtAuthorizationFilter jwtFilter){
		this.jwtFilter = jwtFilter;
	}
	
	private static final int LIST_COUNT = 10;	//한 페이지의 게시물 수
	private static final int PAGE_COUNT = 5;	//페이징 수 
	
	//비대면-과목선택
    @GetMapping("/select-subject-page")
    public String selectSubject( ) {
        return "untact/selectSubject";
    }

    //비대면-증상선택
    @GetMapping("/select-item-page")
    public String selectItem( ) {
        return "untact/selectItem";
    }
	
    //비대면-병원리스트
    @GetMapping("/select-clinic-page")
    public String subjectList(Model model,  HttpServletRequest request) {
    	List<Untact> list = new ArrayList<Untact>();
    	Untact untact = new Untact();
    	List<Untact> dtmList = new ArrayList<>();
    	Paging paging = null;
    	LocalDate currentDate = LocalDate.now();
    	LocalTime currentTime = LocalTime.now();
    	String clinicTime = null;
    	int dayOfWeekIndex = currentDate.getDayOfWeek().getValue();

    	String subject = request.getParameter("subject");
    	String symptom = request.getParameter("symptom");
    	String searchWord = request.getParameter("searchWord");
    	String sortType = request.getParameter("sortType");
    	String location = request.getParameter("location");  	
    	String status = request.getParameter("status");  	
    	
    	
    	long curPage = 1; 
    	if(request.getParameter("curPage") != null)
    	 curPage = (long)Integer.parseInt(request.getParameter("curPage"));
    	
    	if(subject != null) 
    	untact.setClinicSubject(subject);
    	
    	if(symptom != null) 
    	untact.setClinicSymptom(symptom);
    	
    	if(searchWord != null)
    	untact.setSearchWord(searchWord);
    	
    	if(sortType != null)
    	untact.setSortType(sortType);

    	if(location != null)
    	untact.setLocation(location);
    	
    	if(status == null)
    		status="";
    	
		int totalCount = untactService.subjectListCount(untact);
		if (totalCount > 0) {
			paging = new Paging("/select-clinic-page", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
			untact.setStartRow(paging.getStartRow()); 
			untact.setEndRow(paging.getEndRow());
	    	
	    	list = untactService.subjectList(untact);
	    	
	    	for(Untact val : list) {
	    		String[] dtm = val.getClinicTime().split(",");
	    		val.setClinicTime(dtm[dayOfWeekIndex-1]);
	    		clinicTime = dtm[dayOfWeekIndex-1];

	    		if (clinicTime.equals("휴무")) {
	    			val.setClinicStatus("N");
	    			val.setClinicNight("N");
	    		} else {
		    		LocalTime startTime = LocalTime.parse(clinicTime.split("-")[0]);
		    		LocalTime endTime = LocalTime.parse(clinicTime.split("-")[1]);
	  
		    		if (endTime.isAfter(LocalTime.parse("18:00"))) {
		                val.setClinicNight("Y");
		            } else {
		                val.setClinicNight("N");
		            }
	
		    		if(currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
		    			val.setClinicStatus("Y");
		    		} else {
		    			val.setClinicStatus("N");
		    		}
	    		}
	    		dtmList.add(val);
	    	}
	    	
	    	List statusList = new ArrayList();
	    	//진료중 클릭했을때,
	    	if(status.equals("Y")) {
	    		//기존 N ,Y 데이터 모두 가져와서 Y 데이터만 다시 리스트 넣어서 뿌릴거
	    		for(Untact val : dtmList) {
	    			if(val.getClinicStatus().equals("Y")) {
	    				statusList.add(val);
	    			}
	    		}
	    		dtmList = statusList;
	    	}
	    	model.addAttribute("subject", dtmList);
		}
		
		model.addAttribute("clinicSymptom", symptom);
		model.addAttribute("clinicSubject", subject);
		model.addAttribute("untact", untact);
    	model.addAttribute("curPage", curPage);
		model.addAttribute("paging", paging);
		
        return "untact/selectClinic";
    }
  
    //비대면-병원상세
    @GetMapping("/select-clinic-detail-page")
    public String clinicDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		String clinicInstinum = request.getParameter("clinicInstinum");
		List<Untact> reviewList2 = new ArrayList<Untact>(); 
		Untact untact = new Untact(); 
		   
		untact.setClinicInstinum(clinicInstinum); 
		untact = untactService.selectClinicDetail(untact);
		reviewList2 = untactService.reviewList(untact); 
		   
		String subject = "";
		String career = "";
		String time = "";
		   
		if(untact.getClinicSubject() != null) 
		subject = untact.getClinicSubject();
		   
		if(untact.getClinicCareer() != null) 
		career = untact.getClinicCareer();
		   
		if(untact.getClinicTime() != null) 
		time = untact.getClinicTime();
		   
		String[] subjectList = subject.split(",");
		String[] careerList = career.split(",");
		String[] timeList = time.split(",");
       
		model.addAttribute("clinicInstinum", clinicInstinum);
		model.addAttribute("untact", untact);
		model.addAttribute("subjectList", subjectList);
		model.addAttribute("careerList", careerList);
		model.addAttribute("timeList", timeList);
		model.addAttribute("reviewList", reviewList2);
		
		return "untact/selectClinicDetail";
    }
    
    //로그인 체크
    @GetMapping("/loginCheck")
    @ResponseBody
    public String loginCheck(HttpServletRequest request) {
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	if(userEmail == null) {
    	    String testString = userEmail.toString(); //일부러 널포인트 발생
    	    return null;
    	}
    	
    	return userEmail;
    }
    
    //비대면-병원예약
    @GetMapping("/clinic-reserve-application-page")
    public String clinicReserveApp(Model model, HttpServletRequest request) {
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	String clinicInstinum = request.getParameter("clinicInstinum");
    	
    	Untact untact = new Untact();   
    	untact.setClinicInstinum(clinicInstinum);
    	
        
    	LocalDate currentDate = LocalDate.now();
    	LocalTime currentTime = LocalTime.now();
    	String clinicTime = null;
    	int dayOfWeekIndex = currentDate.getDayOfWeek().getValue();
    	
    	untact = untactService.selectClinicDetail(untact);
    	
		String[] dtm = untact.getClinicTime().split(",");
		untact.setClinicTime(dtm[dayOfWeekIndex-1]);
		clinicTime = dtm[dayOfWeekIndex-1];
		if (!clinicTime.equals("휴무")) {

			LocalTime startTime = LocalTime.parse(clinicTime.split("-")[0]);
    		LocalTime endTime = LocalTime.parse(clinicTime.split("-")[1]);

    		List<LocalTime> timeSlots = new ArrayList<>();

    		if (currentTime.isAfter(endTime)) {
    		    model.addAttribute("message", "진료 시간이 끝났습니다.");
    		} else {
    		    if (currentTime.isAfter(startTime)) {
    		    	
    		        //timeSlots.add(currentTime);
    		        currentTime = currentTime.plusMinutes(20);
    		    }

    		    while (currentTime.isBefore(endTime)) {
    		    	
    		        if (currentTime.getMinute() % 20 != 0) {
    		            int minutesToAdd = 20 - (currentTime.getMinute() % 20);
    		            currentTime = currentTime.plusMinutes(minutesToAdd);
    		        }
    		        
    		        if (!currentTime.equals(LocalTime.MIDNIGHT)) {
    		            timeSlots.add(currentTime);
    		        }
    		        
    		        currentTime = currentTime.plusMinutes(20);
    		    }
    		}
    		model.addAttribute("clinic", untact);
    		model.addAttribute("timeSlots", timeSlots);
		}
		
		//untact = untactService.selectClinicDetail(untact);
        return "untact/clinicReservation";
    }
    
    //비대면-진료예약
    @PostMapping("/clinic-reserve-page")
    @ResponseBody
    public Map clinicReserve(HttpServletRequest request, HttpServletResponse response) {
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	
    	String clinicInstinum = request.getParameter("clinicInstinum");
    	
    	String date= request.getParameter("date");
    	String time= request.getParameter("time");
    	String symptoms= request.getParameter("symptoms");
    	
    	Reserve rsve = new Reserve();
    	Map map = new HashMap();

    	if(clinicInstinum!= null) {
    		rsve.setClinicName(untactService.getClinicInfo(clinicInstinum));
    	}
    	if(!StringUtil.isEmpty(userEmail) && !StringUtil.isEmpty(clinicInstinum)) {
	    	if(!StringUtil.isEmpty(symptoms) &&!StringUtil.isEmpty(time)&&!StringUtil.isEmpty(date)) {
	    		rsve.setUserEmail(userEmail);
	    		rsve.setClinicInstinum(clinicInstinum);
	    		rsve.setReservationSymptom(symptoms);
	    		rsve.setReservationTime(time);
	    		
	    		if(date.equals("오늘")){
	    			rsve.setReservationDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
	    		} else {
	    			rsve.setReservationDate(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
	    		}
	    	} else {
	    		map.put("code", 200);
	    	}
    	}
    	
    	int res = 0;
    	if((res = untactService.insertReservation(rsve)) > 0 ) {
    		map.put("code", 1);
     	} else {
    		map.put("code", 250);
    	}
    	response.addIntHeader("res", res);
    	map.put("res", res);
    	map.put("rsve", rsve);
    	return  map;
    }

    //비대면-결제
    @GetMapping("/clinic-reserve-payment-page")
    public String clinicReservePay(Model model, HttpServletRequest request, HttpServletResponse response) {		
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	//TODO: 마이페이지 연결되면 값 바꾸기
    	String reservationSeqS = request.getParameter("reservationSeq"); 
    	//String reservationSeqS = request.getParameter("72"); 
    	int reservationSeq = Integer.parseInt(reservationSeqS);
    	//int reservationSeq = Integer.parseInt("72");
    	
    	PayLog payLog = new PayLog();
    	
    	//TODO: 마이페이지 연결되면 값 바꾸기
    	//payLog.setReservationSeq(reservationSeq);
    	payLog.setReservationSeq(reservationSeq);
    	payLog.setUserEmail(userEmail);
    	
    	model.addAttribute("payLog", payLog);
    	
    	return "untact/clinicReservationPayment";
    }
    
    
    @GetMapping("/clinic-reserve-user-page")
    public String clinicReserveUser(Model model, HttpServletRequest request, HttpServletResponse response) {
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);

    	String rsveUserEmail      = request.getParameter("rsveUserEmail");
    	String rsveClinicInstinum = request.getParameter("rsveClinicInstinum");
    	String rsveSymptoms       = request.getParameter("rsveSymptoms");
    	String reservationDate    = request.getParameter("reservationDate");
    	String reservationTime    = request.getParameter("reservationTime");
    	String rsveClinicName     = request.getParameter("rsveClinicName");
    	
    	Reserve rsve = new Reserve();
    	
    	if(!StringUtil.isEmpty(userEmail)) {
    		rsve.setUserEmail(userEmail);
    		rsve.setClinicInstinum(rsveClinicInstinum);
    		rsve.setReservationSymptom(rsveSymptoms);
    		rsve.setReservationDate(reservationDate);
    		rsve.setReservationTime(reservationTime);
    		rsve.setClinicName(rsveClinicName);
    	}
    	
    	//rsve = untactService.reserveCheck(rsve);
    	
    	model.addAttribute("rsve", rsve);
    	
    	return "untact/reservationUserView";
    }
    
    //약국리스트
    @GetMapping("/select-pharm-page")
    public String pharmList() {
    	return "untact/pharmacyList";
    }
    
    //처방전 작성페이지
    @GetMapping("/prescription")
    public String prescription(Model model, HttpServletRequest request, HttpServletResponse response) {
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	String reservationSeqS = request.getParameter("reservationSeq");
    	int reservationSeq = Integer.parseInt(reservationSeqS);
    	Untact untact = new Untact(); 
    	
    	untact.setReservationSeq(reservationSeq);
    	untact.setUserEmail(userEmail);
    	untact = untactService.precriptionWrite(untact);
    	model.addAttribute("untact", untact);
    	return "untact/prescription";
    }
    
    @PostMapping("/prescriptionInsert")
    public String prescriptionInsert(Model model, HttpServletRequest request, HttpServletResponse response) {
    	String reservationSeq =request.getParameter("reservationSeq");
    	String clinicInstinum = request.getParameter("clinicInstinum");
    	String dose = request.getParameter("dose");
    	Presc presc = new Presc();
    	Reserve rsve = new Reserve();
    	int res= 0;
    	
    	List<String> list = new ArrayList<String>();
    	for(int i = 1; i<=6; i++) {
    		if(!(StringUtil.isEmpty(request.getParameter("medi"+i)) || request.getParameter("medi"+i).equals("")))
    			list.add(request.getParameter("medi"+i));
    	}
		int idx = 1;
		String prescriptionSeq = String.valueOf(untactService.getprescriptionSeq());
    	for(Object var  : list) {
    		presc.setPrescriptionSeq(prescriptionSeq);
    		presc.setMedi((String)var);
    		presc.setClinicInstinum(clinicInstinum);
    		presc.setDose(dose);
    		presc.setReservationSeq(Integer.parseInt(reservationSeq));
    		presc.setOrder(String.valueOf(idx++));
    		res += untactService.prescriptionInsert(presc);
    	}
    	if(res > 0) { 
    		rsve.setReservationSeq(Integer.parseInt(reservationSeq));
    		int updt = untactService.updatePrescriptionStatus(Integer.parseInt(reservationSeq));
    			model.addAttribute("rsve",rsve);
    			return "redirect:/reservationList-page";
    	} else { 
    		return "redirect:/reservationList-page";
    	}
    }
    
    @GetMapping("/prescriptionDetail")
    public void prescriptionDetail(Model model, HttpServletRequest request, HttpServletResponse response) {
    	String token = jwtFilter.extractJwtFromCookie(request);
    	String userEmail = jwtFilter.getUsernameFromToken(token);
    	String reseSeq = request.getParameter("reservationSeq");
        try {	
    	
    	String download = request.getParameter("download");
    	if(download == null) download = "";
    	
    	
    	List<Presc> prescList = new ArrayList<Presc>(); 
    	Presc presc = new Presc();
    	Presc presc2 = new Presc();
    	
    	if(!StringUtil.isEmpty(userEmail)) 
    		presc.setUserEmail(userEmail);
    	
    	//todo: 마이페이지에서 seq받아오기
    	if(reseSeq != null) 
    		presc.setReservationSeq(Integer.parseInt(reseSeq));
    		//presc.setReservationSeq(21);
    	
    	prescList = untactService.prescriptionDetail(presc);
    	presc2.setClinicDoctor(prescList.get(0).getClinicDoctor());
    	presc2.setClinicInstinum(prescList.get(0).getClinicInstinum());
    	presc2.setClinicName(prescList.get(0).getClinicName());
    	presc2.setClinicPhone(prescList.get(0).getClinicPhone());
    	presc2.setPrescriptionDate(prescList.get(0).getPrescriptionDate());
    	presc2.setPrescriptionSeq(prescList.get(0).getPrescriptionSeq());
    	presc2.setUserName(prescList.get(0).getUserName());
    	presc2.setUserIdentity(prescList.get(0).getUserIdentity());

		model.addAttribute("presc2", presc2);
		model.addAttribute("prescList", prescList);

		if ("Y".equals(download)) {
			
			 if (!response.isCommitted()) {
			// Thymeleaf 템플릿 엔진을 사용하여 HTML 생성
			Context context = new Context();
            context.setVariables(model.asMap());
            String htmlContent = templateEngine.process("untact/prescriptionDetail", context);
            // String htmlContent = templateEngine.process("C:\\project\\final\\todoc\\src\\main\\resources\\templates\\untact\\prescriptionDetail.html", context);

			ITextRenderer renderer = new ITextRenderer();
			renderer.getFontResolver()
			//폰트를 설정한다.
		        .addFont(
		                new ClassPathResource("/templates/NanumBarunGothic.ttf")
		                        .getURL()
		                        .toString(),
		                BaseFont.IDENTITY_H,
		                BaseFont.EMBEDDED);
			renderer.setDocumentFromString(htmlContent);
			renderer.layout();

			// PDF 생성
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			renderer.createPDF(outputStream);
			renderer.finishPDF();
			byte[] pdfBytes = outputStream.toByteArray();

			// HTTP 응답 헤더 설정
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=Prescription.pdf");

			// PDF 파일 전송
			OutputStream servletOutputStream = response.getOutputStream();
			servletOutputStream.write(pdfBytes);
			servletOutputStream.flush();
			servletOutputStream.close();
			 } 
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
	}

}

}
