package com.todoc.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.util.StringUtils;

import com.todoc.web.dto.FileData;
import com.todoc.web.dto.Megazines;
import com.todoc.web.dto.MegazinesFile;
import com.todoc.web.dto.MegazinesLike;
import com.todoc.web.dto.Paging;
import com.todoc.web.dto.Response;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.service.MegazinesService;
import com.todoc.web.service.UserService;
import com.todoc.web.util.HttpUtil;



@Controller
@RequestMapping
public class MegazinesController {
	
	private static Logger logger = LoggerFactory.getLogger(MegazinesController.class);

	
	@Autowired
	private MegazinesService megazinesService;
	
	@Autowired
	private UserService userService;
	
	//매거진 이미지 파일 경로
	@Value("${megazines.upload.dir}")
	private String megazinesFileUploadDir;
	
	private final JwtAuthorizationFilter jwtFilter;
	
	public MegazinesController(JwtAuthorizationFilter jwtFilter){
		this.jwtFilter = jwtFilter;
	}
	
	
	private static final int LIST_COUNT = 6;
	private static final int PAGE_COUNT = 5;
	
		
	   //매거진 리스트
	   @GetMapping("/megazine-list-page")
	   public String list(Model model, HttpServletRequest request) {
		 String token = jwtFilter.extractJwtFromCookie(request);
	     String userEmail = jwtFilter.getUsernameFromToken(token); 
	     String userType = null;
		   
		 //정렬필터타입(1:최신순, 2: 조회, 3: 추천)
		 String newsFilter = request.getParameter("newsFilter");
		 if(newsFilter == null) {
			 newsFilter = "1";
		 }
		 
	     //검색 (진료과,증상,제목,내용)
	     String searchValue = request.getParameter("searchValue");
	      
	     //현재페이지
	     long curPage = 1;
	     
	     //게시물 리스트
	     List<Megazines> list = null;
	     
	
	     //조회객체
	     Megazines search = new Megazines();
	     //페이징 객체
	     Paging paging = null;
	     //총 게시물
	     long totalCount = 0;
	     
	     search.setNewsFilter(newsFilter);
	     search.setSearchValue(searchValue);
	     
	     totalCount = megazinesService.megazinesListCount(search);	
	    
	     
		if(totalCount > 0) {  
		 	if(request.getParameter("curPage") != null)
		    	 curPage = (long)Integer.parseInt(request.getParameter("curPage"));
			paging = new Paging("/megazine-list-page",totalCount,LIST_COUNT,PAGE_COUNT ,curPage,"curPage");
		
			search.setStartRow(paging.getStartRow());
			search.setEndRow(paging.getEndRow());

		}
		
		list = megazinesService.MegazinesList(search);
		
		
		
		
		model.addAttribute("search",search);
		model.addAttribute("list", list);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("curPage",curPage); 
		model.addAttribute("paging",paging);
		model.addAttribute("newsFilter",newsFilter);
		model.addAttribute("userEmail",userEmail);

		   
	       return "megazines/megazineList";
	   }
	   
	   
	   
	   //매거진 글쓰기 페이지
	   @GetMapping("/megazine-write-page")
	   public String write(HttpServletRequest request) {
			String token = jwtFilter.extractJwtFromCookie(request);
	    	String userEmail = jwtFilter.getUsernameFromToken(token);
		   
	    	if(userEmail == null) {
	    		return "login/login";
	    	}else {
	    		if(!"ADMIN".equals(userService.findByEmail(userEmail).getUserType())) {
	    			return "redirect:/logout";
	    		}
	    	}

	    	
	       return "megazines/megazineWrite";
	   }
	   
	   
	   


	   //글 등록 ajax
	   @PostMapping("/writeProc")
	   @ResponseBody
	   public Response<Object> writeProc(MultipartHttpServletRequest request, HttpServletResponse response){
		   Response<Object> ajaxResponse = new Response<Object>();

		   String token = jwtFilter.extractJwtFromCookie(request);
		   String userEmail = jwtFilter.getUsernameFromToken(token);
		   
		   String newsTitle = request.getParameter("newsTitle");
		   String newsSubtitle = request.getParameter("newsSubtitle");
		   String newsContent = request.getParameter("newsContent");
		   String newsDepartment = request.getParameter("newsDepartment");
		   String newsSymptom = request.getParameter("newsSymptom");
		   
		   FileData fileData = HttpUtil.getFile(request, "imgFile", megazinesFileUploadDir); 
		   


	    	if(userEmail != null) {
	    		if(newsTitle != null && newsSubtitle != null && newsContent != null && newsDepartment != null && newsSymptom != null) {
	    			Megazines megazines = new Megazines();
	    			megazines.setNewsTitle(newsTitle);
	    			megazines.setNewsSubtitle(newsSubtitle);
	    			megazines.setNewsContent(newsContent);
	    			megazines.setNewsDepartment(newsDepartment);
	    			megazines.setNewsSymptom(newsSymptom);
	    			megazines.setUserEmail(userEmail);
	    			if(fileData != null && fileData.getFileSize() > 0) {
	    				MegazinesFile megazinesFile = new MegazinesFile();
	    				megazinesFile.setFileName(fileData.getFileName());
	    				megazinesFile.setFileExt(fileData.getFileExt());
	    				megazinesFile.setFileOrgName(fileData.getFileOrgName());
	    				megazinesFile.setFileSize(fileData.getFileSize());
	    				megazines.setMegazinesFile(megazinesFile);
	    				
	    			}else {
	    				ajaxResponse.setResponse(101, "No fileData"); //파일 없음
	    			}
	    			
	    		try {
					if(megazinesService.megazinesInsert(megazines) > 0) {
						ajaxResponse.setResponse(0, "Success"); //성공
					}else {
						ajaxResponse.setResponse(500, "Internal server error");//인서트 실패
					}
				} catch (Exception e) {
					ajaxResponse.setResponse(500, "Internal server error");//인서트 실패
				}
	    			
	    			
	    			
	    		}else {
	    			ajaxResponse.setResponse(303, "Internal server error");//파라미터 
	    		}
	    		
	    	}else {//userEmail
	    		ajaxResponse.setResponse(202, "Internal server error");//로그인 재요청
	    	}
		   
		   
		   
		    return ajaxResponse;
	   }
	   

	   
	   //매거진 글 상세페이지
	   @GetMapping("/megazine-detail-page")
	   public String detail(HttpServletRequest request,@RequestParam("newsSeq") long newsSeq, Model model) {
	    	String token = jwtFilter.extractJwtFromCookie(request);
	    	String userEmail = jwtFilter.getUsernameFromToken(token);
	    	String isLikedFlag = null;
	    	String delFlag = null;
		   
		   long totalLike = 0;
		   Megazines megazines = new Megazines();
		   if(newsSeq > 0) {
			   megazines = megazinesService.megazinesDetail(newsSeq);
		   }
		   if(userEmail != null) {
			   MegazinesLike megazinesLike = new MegazinesLike();
			   megazinesLike.setUserEmail(userEmail);
			   megazinesLike.setNewsSeq(newsSeq);
			   if(megazinesService.checkLikeCnt(megazinesLike)>= 1){
				   isLikedFlag = "Y";
			   }else {
				   isLikedFlag = "N";
			   }
			   
			   if(megazines.getUserEmail().equals(userEmail)) {
				   delFlag="Y";
			   }else {
				   delFlag="N";
			   }
		   }
		   
		   
		   
		   model.addAttribute("megazines",megazines);
		   model.addAttribute("newsSeq",newsSeq);
		   model.addAttribute("isLikedFlag",isLikedFlag);
		   model.addAttribute("delFlag",delFlag);

	       return "megazines/megazineDetail";
	   }
	   
	   //좋아요 ajax 
	    @PostMapping("/likeCnt")
	    @ResponseBody
		public int likeCnt(HttpServletRequest request, @RequestParam("newsSeq") String newsSeq){
	    	String token = jwtFilter.extractJwtFromCookie(request);
	    	String userEmail = jwtFilter.getUsernameFromToken(token);
	    	
	    	long _newsSeq = Long.parseLong(newsSeq);
	    	if(!StringUtils.isEmpty(userEmail)) {
	    		
	    		MegazinesLike megazinesLike = new MegazinesLike();
	    		megazinesLike.setUserEmail(userEmail);
	    		megazinesLike.setNewsSeq(_newsSeq);
	    		try {
		    		if(megazinesService.megazinesLike(megazinesLike) > 0 ) {
		    			Megazines megazines = new Megazines();
		    			megazines = megazinesService.megazinesDetail(_newsSeq);
		    			
		    			return megazines.getNewsLikeCnt(); // 성공
		    		}else {
		    			return -3; //좋아요 = 0 
		    		}
				} catch (Exception e) {
					return -2;	//좋아요 오류
				}
	    	}else {
	    		return -1; //로그인 하지 않음
	    	}
	    	
	    	
	    	
	    }
	    
	    //글 삭제 ajax
	    @PostMapping("/megazine-delete-page")
	    @ResponseBody
	    public int megazineDelete(HttpServletRequest request, @RequestParam("newsSeq") String newsSeq) {
	    	String token = jwtFilter.extractJwtFromCookie(request);
	    	String userEmail = jwtFilter.getUsernameFromToken(token);
	    	long _newsSeq = Long.parseLong(newsSeq);

	    	if(_newsSeq > 0) { 
				Megazines megazines = megazinesService.megazinesSelect(_newsSeq);
				if(megazines != null) {  //하이보드 있으면 
					if(userEmail.equals(megazines.getUserEmail())) {  
						try {
							if(megazinesService.megazineDelete(_newsSeq) > 0 ) {  //삭제
								return 0;
							}else {
								return 500;//삭제 실패
							}
							
						} catch (Exception e) {
							logger.error("[MegazonesController] megazineDelete Exception" , e);
							return 500;//삭제 실패
						}
					}else {  
						
						return 202;//내 게시글이 아닌 경우
					}
				}else {  
					return 303;//해당 글이 없는 경우
				}
			}else {  
				return 404;//파라미터 값이 없는 경우
				
			}

	    	
	    }
	    	
	    //글 수정페이지
	    @GetMapping("/megazine-update-page")
	    public String megazineUpdate(Model model, HttpServletRequest request) {
	    	
	    	String token = jwtFilter.extractJwtFromCookie(request);
	    	String userEmail = jwtFilter.getUsernameFromToken(token);
	    	long newsSeq = Long.parseLong(request.getParameter("newsSeq"));

	    	Megazines megazines = new Megazines();
	    	if(newsSeq > 0) {
	    		megazines = megazinesService.megazinesSelect(newsSeq);
	    		if(megazines != null) {
	    			if(!userEmail.equals(megazines.getUserEmail())) {
	    				megazines = null;
	    			}
	    		}
			   
			   
	    	}

	    	model.addAttribute("megazines",megazines);
	    	model.addAttribute("newsSeq",newsSeq);

	    	return "megazines/megazineUpdate";
	    }
	
	    //글 수정ajax
	    @PostMapping("/updateProc")
	    @ResponseBody
	    public Response<Object> updateProc(MultipartHttpServletRequest request, HttpServletResponse response){
	    	  Response<Object> ajaxResponse = new Response<Object>();

			   String token = jwtFilter.extractJwtFromCookie(request);
			   String userEmail = jwtFilter.getUsernameFromToken(token);
			   
			   long newsSeq = Long.parseLong(request.getParameter("newsSeq"));
			   String newsTitle = request.getParameter("newsTitle");
			   String newsSubtitle = request.getParameter("newsSubtitle");
			   String newsContent = request.getParameter("newsContent");
			   String newsDepartment = request.getParameter("newsDepartment");
			   String newsSymptom = request.getParameter("newsSymptom");
			   

	    	
			   if(newsSeq > 0 && newsTitle!=null &&newsSubtitle!=null && newsContent!=null && newsDepartment!=null && newsSymptom!=null) {
				   
				   Megazines megazines = megazinesService.megazinesSelect(newsSeq);
				   
				   if(megazines != null) {
					   if(userEmail.equals(megazines.getUserEmail())) {
							megazines.setNewsTitle(newsTitle);
			    			megazines.setNewsSubtitle(newsSubtitle);
			    			megazines.setNewsContent(newsContent);
			    			megazines.setNewsDepartment(newsDepartment);
			    			megazines.setNewsSymptom(newsSymptom);
			 			   FileData fileData = HttpUtil.getFile(request, "imgFile", megazinesFileUploadDir); 
			 			   if(fileData != null && fileData.getFileSize() > 0) {
			    				MegazinesFile megazinesFile = new MegazinesFile();
			    				megazinesFile.setFileName(fileData.getFileName());
			    				megazinesFile.setFileExt(fileData.getFileExt());
			    				megazinesFile.setFileOrgName(fileData.getFileOrgName());
			    				megazinesFile.setFileSize(fileData.getFileSize());
			    				megazines.setMegazinesFile(megazinesFile);
			    			
				    			try {
				    				if(megazinesService.megazineUpdate(megazines) > 0) {
					    				ajaxResponse.setResponse(0,"success");
	
				    				}else {
					    				ajaxResponse.setResponse(500,"internal server error");
				    				}
				    			}catch (Exception e) {
				    				ajaxResponse.setResponse(500,"internal server error");
				    			}
						   }else {
				    			ajaxResponse.setResponse(405,"server error");//업로드 파일 파라미터가 올바르지 않음
						   }
					   }else {
						   ajaxResponse.setResponse(300,"server error");//userEmail값이 동일하지 않음
					   }
				   }else {
					   ajaxResponse.setResponse(404,"not found");//글이 존재하지 않음
				   }
			   }else {
				   ajaxResponse.setResponse(400,"bad request");//파라미터가 올바르지 않음
			   }
	    	
	    	
	    	
			   return ajaxResponse;
	    }
	    
	   
	   

	   
	   
	   
	
	
}
