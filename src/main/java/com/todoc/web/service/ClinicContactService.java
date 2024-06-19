package com.todoc.web.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoc.web.dao.ClinicContactDao;
import com.todoc.web.dto.ClinicContact;
import com.todoc.web.dto.ReservationContact;



@Service
public class ClinicContactService {
	private static Logger logger = LoggerFactory.getLogger(ClinicContactService.class);
	
	@Autowired
	private ClinicContactDao clinicContactDao;
	
	//대면 병원 리스트(모두 불러오기)
	public List<ClinicContact> clinicList(){ 
		List<ClinicContact> list =null;
		
		try {
			list = clinicContactDao.clinicList(); 	
		}catch(Exception e) {
			logger.error("[ClinicContactService] clinicList Exception",e);
		}

		return list;
		
	}
	
	
	//병원 리스트 조회(category)
	public List <ClinicContact> clinicListCategory(ClinicContact search){
		List<ClinicContact> list = null;
		
		
		if(search != null && search.getGuValue() != null) {
			Integer guValueIndex = search.getGuValue();
			search.setGuName(search.getGuList().get(guValueIndex));
		}
		

		list =clinicContactDao.clinicListCategory(search);
		
		return list;

	}
	
	//총 게시물 수
	public long listCount(ClinicContact search) {
		long count = 0;
		try {
			if(search != null && search.getGuValue() != null) {
				Integer guValueIndex = search.getGuValue();
				search.setGuName(search.getGuList().get(guValueIndex));
			}
			if(search != null && search.getSearchValue() != null) {
			search.setSearchValue(codeChangeName(search.getSearchValue()));  
			}
			if(search != null && search.getGuValue() != null) {
				Integer guValueIndex = search.getGuValue();
				search.setGuName(search.getGuList().get(guValueIndex));
			}

			count = clinicContactDao.listCount(search);
		} catch (Exception e) {
			logger.error("[ClinicContactService] listCount Exception",e);
		}
		
		return count;
	}
	
	//카테고리 코드 명칭으로 변환
	public String codeChangeName(String searchValueCode) {
	
			String searchValue = null;
			switch(searchValueCode) { 
				// 진료과목 검색
				case "1": searchValue = "피부과";
					break;
				case "2": searchValue = "산부인과";
					break;
				case "3": searchValue = "이비인후과";
					break;
				case "4": searchValue = "내과";
					break;
				case "5": searchValue = "안과";
					break;
				case "6": searchValue = "가정의학과";
					break;
				case "7": searchValue = "소아과";
					break;
				case "8": searchValue = "정형외과";
					break;
				case "9": searchValue = "정신건강의학과";
					break;
				case "10": searchValue = "비뇨기과";
					break;
				case "11": searchValue = "치과";
					break;
				case "12": searchValue = "신경외과";
					break;
				// 증상 검색
				case "21": searchValue = "감기/몸살";
					break;	
				case "22": searchValue = "소화불량";
					break;		
				case "23": searchValue = "소아과";
					break;		
				case "24": searchValue = "비염";
					break;		
				case "25": searchValue = "여드름";
					break;		
				case "26": searchValue = "탈모";
					break;		
				case "27": searchValue = "다이어트";
					break;		
				case "28": searchValue = "인공눈물";
					break;		
				case "29": searchValue = "위염";
					break;		
				case "30": searchValue = "장염";
					break;		
				case "31": searchValue = "당뇨";
					break;		
				case "32": searchValue = "고혈압";
					break;		
				case "33": searchValue = "여성질환";
					break;		
				case "34": searchValue = "만성질환";
					break;		
				case "35": searchValue = "복통";
					break;		
				case "36": searchValue = "두통";
					break;	
				case "37": searchValue = "주의력저하";
					break;
				case "38": searchValue = "우울증";
					break;
				case "39": searchValue = "치아";
					break;
				case "40": searchValue = "기력저하";
					break;
				
			}
		
		return searchValue;
	}
	
	
	//영업시간 전체 리스트
	public List<ClinicContact> clinicTimeList(){   
		List<ClinicContact> list =null;
		try {
			list = clinicContactDao.clinicTimeList();
		} catch (Exception e) {
			logger.error("[ClinicContactService] clinicTimeList Exception",e);
		}
		
		return list;
	}
	
	//진료중 구하기
	public List clinicRunningList(){  
		
		 List<ClinicContact> timeListAll = clinicContactDao.clinicTimeList();
	        int size = timeListAll.size();
	        String[][] timeAndInstinumArray = new String[size][10]; // 10으로 수정
	        for (int i = 0; i < size; i++) {
	            timeAndInstinumArray[i][0] = timeListAll.get(i).getClinicInstinum();

	           
	            String[] clinicTimes = timeListAll.get(i).getClinicTime().split(",");

	            // ClinicTime 배열의 요소를 timeAndInstinumArray에 할당합니다.
	            for (int j = 0; j < clinicTimes.length; j++) {
	                timeAndInstinumArray[i][j + 1] = clinicTimes[j];
	            }
	        }

	        // 현재 요일과 시간
	        DayOfWeek currentDayOfWeek = DayOfWeek.from(LocalDateTime.now());
	        LocalTime currentTime = LocalTime.now();

	        // 영업중인 ClinicInstinum 목록
	        List<String> openClinics = new ArrayList<>();
	        int dayIndex = currentDayOfWeek.getValue() - 1;//일요일일때만 dayindex를 6으로 하기

	        for (int i = 0; i < timeAndInstinumArray.length; i++) {
	            String businessHour = timeAndInstinumArray[i][dayIndex + 1];//여기서 리스트에 맞는 인덱스로 들어감
	            if (businessHour != null && !businessHour.equals("휴무") && isBusinessHour(businessHour)) {
	                openClinics.add(timeAndInstinumArray[i][0]);
	            }
	            
	        }
	        
	        return openClinics;
        
	}
	

	
	//해당 날짜 영업중 여부 (공휴일 제외)
	public boolean dayCheck(String selectedDate, String selectedTime, String[] clinicTime) {  
		
        LocalDate date = LocalDate.parse(selectedDate);

      
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        int dayIndex = date.getDayOfWeek().getValue() - 1;
     
        String businessHour  = clinicTime[dayIndex];

		return isBusinessHour(selectedTime,businessHour);
	}
	
	//일별 영업시간 데이터을 현재 시간과 비교
	private boolean isBusinessHour(String businessHour) {
		 if (businessHour.equals("휴무")) {
	            return false;
	        }

	        LocalTime currentTime = LocalTime.now();
	        String[] hours = businessHour.split("-");
	        LocalTime startTime = LocalTime.parse(hours[0]);
	        LocalTime endTime = LocalTime.parse(hours[1]);

	        return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
   }
	
	private boolean isBusinessHour(String selectedTime, String businessHour) {
		
		// 선택된 시간을 LocalTime 객체로 변환
        	LocalTime currentTime = LocalTime.parse(selectedTime);
		 if (businessHour.equals("휴무")) {
	            return false;
	        }
	        String[] hours = businessHour.split("-");
	        LocalTime startTime = LocalTime.parse(hours[0]);
	        LocalTime endTime = LocalTime.parse(hours[1]);
		 
	        
	        return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
		
	}
	
	//병원 상세페이지 조회
	public ClinicContact clinicDetail(String clinicInstinum) {
		ClinicContact clinicContact = null;
		try {
			clinicContact = clinicContactDao.clinicDetail(clinicInstinum);
		} catch (Exception e) {
			logger.error("[ClinicContactService] clinicDetail Exception",e);
		}
		
		return clinicContact;
	}
	
	//대면병원예약
	public int reservationInsert(ReservationContact reservation) {
		int result = 0;
		try {
			result = clinicContactDao.reservationInsert(reservation);

		} catch (Exception e) {
			logger.error("[ClinicContactService] reservationInsert Exception",e);
		}
		
		
		return result;
		
		
	}
	
	//병원 현재시간 기준 영업시간 선택 버튼 리스트
	public List<String> reserveTimebutton(String clinicInstinum) {
		ClinicContact clinicContact = null;
		
			List<String> clinicTimeList = new ArrayList<>(); 
			List<String> nextTimes = new ArrayList<>();
			List<String> availableList = null;
			List<String> reservedTime = null;
			try {
				//해당병원 진료시간
				clinicContact = clinicContactDao.timeOnly(clinicInstinum);
				
				
				//진료시간 리스트
				String[] clinicTime = clinicContact.getClinicTime().split(",");	
				for (String time : clinicTime) {
				    
				}
	
				 // 현재 시간 가져오기
		        LocalTime currentTime = LocalTime.now().plusHours(1);
				
		    	//현재 요일
		        LocalDateTime currentDay = LocalDateTime.now();
				DayOfWeek dayOfWeek = currentDay.getDayOfWeek(); 
				int dayIndex = dayOfWeek.getValue()-1;
				String todayRunningTime = clinicTime[dayIndex]; //9:00-18:00
				
				//현재 날짜 형식
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
		        // 날짜를 문자열로 변환
		        String selectedDate = currentDay.format(formatter);
		        
				if(!todayRunningTime.equals("휴무")) {
					// 현재 시간 이후의 시간대 구하기
			        String[] runningTimes = todayRunningTime.split("-");
			        String start = runningTimes[0]; // 9:00
			        LocalTime startTime = LocalTime.parse(start, DateTimeFormatter.ofPattern("H:mm"));

			        String end = runningTimes[1]; // 18:00
			        LocalTime endTime = LocalTime.parse(end, DateTimeFormatter.ofPattern("H:mm"));
		
			        // 현재 시간이 영업 시간 내에 있는 경우에만 다음 시간대를 계산
			        
			        if (currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(endTime) <= 0) {
			            LocalTime nextTime;
			            if (currentTime.getMinute() < 20) {
			                nextTime = currentTime.withMinute(20);
			            } else if (currentTime.getMinute() < 40) {
			                nextTime = currentTime.withMinute(40);
			            } else {
			                // 시간을 1시간 증가시키고 분을 0으로 설정
			                nextTime = currentTime.plusHours(1).withMinute(0);
			            }
			            // 다음 시간대부터 영업 마감 시간까지 20분 간격으로 시간 계산하여 리스트에 추가
			            while (nextTime.compareTo(endTime) <= 0) {
			                nextTimes.add(nextTime.format(DateTimeFormatter.ofPattern("HH:mm")));
			                nextTime = nextTime.plusMinutes(20);
			                
			            }
	  
			        }
			        if(nextTimes.isEmpty()) {
			            nextTimes.add("예약가능한 시간이 없습니다.");
	
			        }
				}else {//"휴무" 인경우
		            nextTimes.add("휴무입니다. 다른 날짜를 선택하세요.");
	
				}
	
	          //예약된 시간대 리스트 제거
	          
	          reservedTime = reservedTime(clinicInstinum, selectedDate);

	          availableList = excludeTimes(nextTimes, reservedTime);
			} catch (Exception e) {
				logger.error("[ClinicContactService] reserveTimebutton Exception",e);
			}

		return availableList;
	}
	
	//ajax 
	public List<String> reserveTimebutton(String selectedDate, String clinicInstinum, String _isHoliday) {
		ClinicContact clinicContact = null;
		List<String> clinicTimeList = new ArrayList<>(); 
		List<String> nextTimes = new ArrayList<>();
		
		//현재 날짜
		 LocalDate currentDay = LocalDateTime.now().toLocalDate();
		
		//해당병원 진료시간
		clinicContact = clinicContactDao.timeOnly(clinicInstinum);
		
		//진료시간 리스트
		String[] clinicTime = clinicContact.getClinicTime().split(",");	

		 // 현재 시간 가져오기 오늘이면 필요
        LocalTime currentTime = LocalTime.now().plusHours(1);
		
    	//선택한 날짜의 요일
        LocalDate date = LocalDate.parse(selectedDate);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
		int dayIndex = dayOfWeek.getValue()-1;
		String todayRunningTime = clinicTime[dayIndex]; //9:00-18:00
		
		//공휴일 영업시간
		String holidayRunningTime = clinicTime[clinicTime.length-1];
		
		//공휴일 플래그(브라우저에서 받기)
		String isHoliday = "N";
		//선택한 날짜가 공휴일이면 
		isHoliday =_isHoliday;

		
		
		//선택한 날짜가 오늘 
		if(date.isEqual(currentDay)) {
			if(isHoliday.equals("Y")){//오늘이 공휴일이면 holidayRunningTime 으로 현재시간 반영해 남는 시간 표시
				if(!holidayRunningTime.equals("휴무")) {
					// 현재 시간 이후의 시간대 구하기
			        String[] runningTimes = holidayRunningTime.split("-");
			        String start = runningTimes[0]; // 9:00
			        LocalTime startTime = LocalTime.parse(start, DateTimeFormatter.ofPattern("H:mm"));
			        
			        String end = runningTimes[1]; // 18:00
			        LocalTime endTime = LocalTime.parse(end, DateTimeFormatter.ofPattern("H:mm"));
			        
			        // 현재 시간이 영업 시간 내에 있는 경우에만 다음 시간대를 계산
			        if (currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(endTime) <= 0) {
			            LocalTime nextTime;
			            if (currentTime.getMinute() < 20) {
			                nextTime = currentTime.withMinute(20);
			            } else if (currentTime.getMinute() < 40) {
			                nextTime = currentTime.withMinute(40);
			            } else {
			                // 시간을 1시간 증가시키고 분을 0으로 설정
			                nextTime = currentTime.plusHours(1).withMinute(0);
			            }

			            // 다음 시간대부터 영업 마감 시간까지 20분 간격으로 시간 계산하여 리스트에 추가
			            while (nextTime.compareTo(endTime) <= 0) {
			                nextTimes.add(nextTime.format(DateTimeFormatter.ofPattern("HH:mm")));
			                nextTime = nextTime.plusMinutes(20);
			            }
			        }
			        if(nextTimes.isEmpty()) {
			            nextTimes.add("예약가능한 시간이 없습니다.");

			        }
				}else {//"휴무" 인경우
		            nextTimes.add("휴무입니다. 다른 날짜를 선택하세요.");
		
				}
			
			}else {//오늘이 공휴일이 아니면 오늘이 요일 휴무인지 체크
				if(!todayRunningTime.equals("휴무")) {
					// 현재 시간 이후의 시간대 구하기
			        String[] runningTimes = todayRunningTime.split("-");
			        String start = runningTimes[0]; // 9:00
			        LocalTime startTime = LocalTime.parse(start, DateTimeFormatter.ofPattern("H:mm"));
			        
			        String end = runningTimes[1]; // 18:00
			        LocalTime endTime = LocalTime.parse(end, DateTimeFormatter.ofPattern("H:mm"));
			        
			        // 현재 시간이 영업 시간 내에 있는 경우에만 다음 시간대를 계산
			        if (currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(endTime) <= 0) {
			            LocalTime nextTime;
			            if (currentTime.getMinute() < 20) {
			                nextTime = currentTime.withMinute(20);
			            } else if (currentTime.getMinute() < 40) {
			                nextTime = currentTime.withMinute(40);
			            } else {
			                // 시간을 1시간 증가시키고 분을 0으로 설정
			                nextTime = currentTime.plusHours(1).withMinute(0);
			            }

			            // 다음 시간대부터 영업 마감 시간까지 20분 간격으로 시간 계산하여 리스트에 추가
			            while (nextTime.compareTo(endTime) <= 0) {
			                nextTimes.add(nextTime.format(DateTimeFormatter.ofPattern("HH:mm")));
			                nextTime = nextTime.plusMinutes(20);
			            }
			        }
			        if(nextTimes.isEmpty()) {
			            nextTimes.add("예약가능한 시간이 없습니다.");

			        }
				}else {//"휴무" 인경우
		            nextTimes.add("휴무입니다. 다른 날짜를 선택하세요.");
		
				}
			}

		}else {//선택한 날짜가 오늘이 아닌 경우
			
			//이전 날짜를 클릭했다면
			// 현재 날짜 가져오기
			LocalDate currentDate = LocalDate.now();
			// 선택된 날짜 파싱
			LocalDate selectedDate_ = LocalDate.parse(selectedDate);
			// 어제 날짜 가져오기
			LocalDate yesterdayDate = currentDate.minusDays(1);
			
			if (selectedDate_.isBefore(currentDate)) {
				// 예약 가능한 시간대가 없음을 알리는 메시지 
				nextTimes.add("예약가능한 시간이 없습니다.");
			}else { 			
				// 현재 시간 이후의 시간대 구하기
				
				if(isHoliday.equals("Y")) {//공휴일인경우
					if(!holidayRunningTime.equals("휴무")) {
		
				        String[] runningTimes = holidayRunningTime.split("-");
				        String start = runningTimes[0]; // 9:00
				        LocalTime startTime = LocalTime.parse(start, DateTimeFormatter.ofPattern("H:mm"));
				        
				        String end = runningTimes[1]; // 18:00
				        LocalTime endTime = LocalTime.parse(end, DateTimeFormatter.ofPattern("H:mm"));
				        // 현재 시간이 영업 시간 내에 있는 경우에만 다음 시간대를 계산
				        
				        while (startTime.isBefore(endTime)) {
					        nextTimes.add(startTime.format(DateTimeFormatter.ofPattern("HH:mm")));
					        startTime = startTime.plusMinutes(20);
				        }
					}else {
				       nextTimes.add("휴무입니다. 다른 날짜를 선택하세요.");
					}
				}else {//공휴일이 아닌경우
					if(!todayRunningTime.equals("휴무")) {
		
				        String[] runningTimes = todayRunningTime.split("-");
				        String start = runningTimes[0]; // 9:00
				        LocalTime startTime = LocalTime.parse(start, DateTimeFormatter.ofPattern("H:mm"));
				        
				        String end = runningTimes[1]; // 18:00
				        LocalTime endTime = LocalTime.parse(end, DateTimeFormatter.ofPattern("H:mm"));
				        // 현재 시간이 영업 시간 내에 있는 경우에만 다음 시간대를 계산
				        
				        while (startTime.isBefore(endTime)) {
					        nextTimes.add(startTime.format(DateTimeFormatter.ofPattern("HH:mm")));
					        startTime = startTime.plusMinutes(20);
				        }
					}else {
				       nextTimes.add("휴무입니다. 다른 날짜를 선택하세요.");
					}
				}

			}
			
			

		}
		
        
		List<String> reservedTime = null;
		reservedTime = reservedTime(clinicInstinum, selectedDate);
		
        List<String> availableList = excludeTimes(nextTimes, reservedTime);

	return availableList;
	}

	
	//해당 날짜 예약된 시간대 찾기
	public List<String> reservedTime(String clinicInstinum, String selectedDate) {
		List<ReservationContact> reservedTime = null;
		List<String> reservedTimeList = new ArrayList<>();
		
		ReservationContact reservation = new ReservationContact();
		reservation.setClinicInstinum(clinicInstinum);
		reservation.setReservationDate(selectedDate);
		
		reservedTime = clinicContactDao.reservedTime(reservation);

		if (reservedTime != null) {
		    for (ReservationContact contact : reservedTime) {
		        reservedTimeList.add(contact.getReservationTime());
		    }
		}

		return reservedTimeList;
	}
	
	
	//시간대 비교(이미 예약된 시간대 제외한 리스트 반환)
	 public static List<String> excludeTimes(List<String> a, List<String> b) {
	        List<String> c = new ArrayList<>();
	        // 리스트 a의 각 요소를 반복하면서
	        for (String value : a) {
	            // 리스트 b에 포함되지 않는 경우에만 리스트 c에 추가
	            if (!b.contains(value)) {
	                c.add(value);
	            }
	        }
	        return c;
	    }
	 
	 
	 //단순 공휴일 휴무 여부만 판별(휴무:"Y", 영업:"N")
	 public String isHoliday(String clinicInstinum) {
		
		ClinicContact clinicContact = null;
		//해당병원 진료시간
		clinicContact = clinicContactDao.timeOnly(clinicInstinum);
		//진료시간 리스트
		String[] clinicTime = clinicContact.getClinicTime().split(",");	
		
		
		//공휴일
		if(clinicTime!=null) {
			if(clinicTime[clinicTime.length-1] != null) {
				if(clinicTime[clinicTime.length-1].equals("휴무")) {
					return "Y";
				}
			}
		}
		

		 return "N";
	 }
	 
	//대면 예약 확인
	 public ReservationContact contactReservationCheck(ReservationContact ReservationContact) {
		 ReservationContact reservationContact = null;
		 
		 try {
			 reservationContact =clinicContactDao.contactReservationCheck(reservationContact);
		} catch (Exception e) {
			 logger.error("[ClinicContactService] contactReservationCheck Exception",e);

		}
		 return reservationContact;
	 }
		
	 
	
	 //예약확인 리스트
	 public List<ReservationContact> reservationList(String clinicInstinum)
	 {
		 List<ReservationContact> list = null;
		 
		 try
		 {
			 list = clinicContactDao.reservationList(clinicInstinum);
		 }
		 catch(Exception e)
		 {
			 logger.error("[ClinicContactService] reservationList Exception",e);
		 }
		 return list;
	 }
	 
	 
	 //예약확인 의사 상세
	 public ClinicContact clinicListView(String userEmail)
	 {
		 ClinicContact clinic = null;
		 
		 try
		 {
			 clinic = clinicContactDao.clinicListView(userEmail);
		 }
		 catch(Exception e)
		 {
			 logger.error("[ClinicContactService] clinicListView Exception",e);
		 }
		 
		 return clinic;
	 }
	 
	 //예약리스트 예약승인
	 public int reservationApprove(long reservationSeq)
	 {
		 int count = 0;
		 
		 try
		 {
			 count = clinicContactDao.reservationApprove(reservationSeq);
		 }
		 catch(Exception e)
		 {
			 logger.error("[ClinicContactService] reservationApprove Exception",e);
		 }
		 
		 return count;
	 }
	 
	 //예약리스트 예약취소
	 public int reservationCancel(long reservationSeq)
	 {
		 int count = 0;
		 
		 try
		 {
			 count = clinicContactDao.reservationCancel(reservationSeq);
		 }
		 catch(Exception e)
		 {
			 logger.error("[ClinicContactService] reservationCancel Exception",e);
		 }
		 
		 return count;
	 }
	 
	 //예약승인 리스트 토탈카운트
	 public int reservationListTotal(String clinicInstinum)
	 {
		 int count = 0;
		 
		 try
		 {
			 count = clinicContactDao.reservationListTotal(clinicInstinum);
		 }
		 catch(Exception e)
		 {
			 logger.error("[ClinicContactService] reservationListTotal Exception",e);
		 }
		 
		 return count;
	 }
	 
	 //진료 대기 리스트 토탈카운트
	 public int contactListTotal(String clinicInstinum)
	 {
		 int count = 0;
		 
		 try
		 {
			 count = clinicContactDao.contactListTotal(clinicInstinum);
		 }
		 catch(Exception e)
		 {
			 logger.error("[ClinicContactService] contactListTotal Exception",e);
		 }
		 
		 return count;
	 }
	 
	 
	 //이메일로 병원 정보 불러오기
	 public ClinicContact clinicfindByEmail(String userEmail)
	 {
		 ClinicContact clinic = null;
		 
		 try
		 {
			 clinic = clinicContactDao.clinicfindByEmail(userEmail);
		 }
		 catch(Exception e)
		 {
			 logger.error("[ClinicContactService] clinicfindByEmail Exception",e);
		 }
		 
		 return clinic;
	 }
	 
	 
	 ////////////////////////////////////////////////////////////////// 5.20 승준코드
	//testTime
	public ClinicContact testTime(String userEmail)
	{
		return clinicContactDao.testTime(userEmail);
	}
	
	//마이페이지 진료내역 리스트
	public ReservationContact mypageReservationList(String userEmail)
	{
		ReservationContact clinicContact = null;
		
		try
		{
			clinicContact = clinicContactDao.mypageReservationList(userEmail);
		}
		catch(Exception e)
		{
			logger.error("[ClinicContactService] mypageReservationList Exception",e);
		}
		
		return clinicContact;
	}
	
	//예약번호로 예약리스트
	public ReservationContact resrvationClickMapping(long reservationSeq)
	{
		ReservationContact reservation = null;
		
		try
		{
			reservation = clinicContactDao.resrvationClickMapping(reservationSeq);
		}
		catch(Exception e)
		{
			logger.error("[ClinicContactService] ReservationContact Exception",e);
		}
		
		return reservation;
	}
	
	//예약상태 진로입장으로 변경
	public int reservationStatusUpdate(long reservationSeq)
	{
		int count = 0;
		
		try
		{
			count = clinicContactDao.reservationStatusUpdate(reservationSeq);
		}
		catch(Exception e)
		{
			logger.error("[ClinicContactService] reservationStatusUpdate Exception",e);
		}
		
		return count;
	}
	
	//진료완료로 변경
		public int streamEnd(long reservationSeq)
		{
			int count = 0;
			
			try
			{
				count = clinicContactDao.streamEnd(reservationSeq);
			}
			catch(Exception e)
			{
				logger.error("[ClinicContactService] streamEnd Exception",e);
			}
			
			return count;
		}
		
		
	//진료취소
	public int contactCancel(long reservationSeq)
	{
		int count = 0;
		
		try
		{
			count = clinicContactDao.contactCancel(reservationSeq);
		}
		catch(Exception e)
		{
			logger.error("[ClinicContactService] streamEnd Exception",e);
		}
		
		return count;
	}
	
	
	//대면 진료리스트
	public List<ReservationContact> contactLogList2(ReservationContact reservationContact)
	{
		List<ReservationContact> list = null;
		
		try
		{
			list = clinicContactDao.contactLogList2(reservationContact);
		}
		catch(Exception e)
		{
			logger.error("[ClinicContactService] contactLogList Exception",e);
		}
		
		return list;
	}
	
	//대면 진료리스트 토탈
	public int contactLogTotal2(String userEmail)
	{
		int count = 0;
		
		try
		{
			count = clinicContactDao.contactLogTotal2(userEmail);
		}
		catch(Exception e)
		{
			logger.error("[ClinicContactService] contactLogTotal2 Exception",e);
		}
		
		return count;
	}
	
	//////////////////////////////////////5.24 승준코드	//////////////////////////////////////////////////

	 
	 
	 
	 
	 
}



