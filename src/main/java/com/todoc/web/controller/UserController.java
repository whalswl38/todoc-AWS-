package com.todoc.web.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.bouncycastle.asn1.ocsp.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.todoc.web.dto.Clinic;
import com.todoc.web.dto.Pharmacy;
import com.todoc.web.dto.User;
import com.todoc.web.security.dto.SignUpDto;
import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserController 
{	
	@Autowired
	private UserService userService;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtAuthorizationFilter jwtFilter;
	
    // 아이디 찾기 페이지
    @GetMapping("/login/findId")
    public String findIdPage()
    {
    	return "login/findId";
    }
    
    // 비밀번호 찾기 페이지
    @GetMapping("/login/findPwd")
    public String findPwdPage()
    {
    	return "login/findPwd";
    }
    
	// 로그인 페이지
    @GetMapping("/login-page")
    public String loginPage( ) 
    {
        return "login/login";
    }
    
    // 병원, 약국 회원가입 페이지
    @GetMapping("/medical-register-page")
    public String medicalRegisterPage(Model model) 
    {
    	model.addAttribute("signUpDto", new SignUpDto());
    	
        return "login/medicalRegister";
    }
    
    // 병원, 약국 회원 회원가입 기능
    @Transactional
	@PostMapping("/medicalSign")
	public String medicalSign(MultipartHttpServletRequest request, @Valid SignUpDto signUpDto, BindingResult bindingResult, Model model,@ModelAttribute MultipartFile clinicFile,@ModelAttribute MultipartFile stampFile) throws IOException
	{
    	if(bindingResult.hasErrors())
		{
			model.addAttribute("signUpDto", signUpDto);
			return "login/medicalRegister";
		}
		
		Clinic clinic  = new Clinic();
		Pharmacy pharmacy = new Pharmacy();
		String type = "";
		
		if(signUpDto.getUserType().equals("병의원"))
		{
			try
			{
				clinic.setClinicInstinum(signUpDto.getInstitutionNum());
				clinic.setClinicRegnum(signUpDto.getRegNum());
				clinic.setClinicPhone(signUpDto.getUserPhone());
				clinic.setClinicName(signUpDto.getInstitutionName());
				clinic.setClinicSubject(signUpDto.getSubject());
				clinic.setClinicSymptom(signUpDto.getSymptop());
				clinic.setUserEmail(signUpDto.getUserEmail());
				clinic.setUserPwd(passwordEncoder.encode(signUpDto.getUserPwd()));
				clinic.setClinicDay(signUpDto.getDayOn());
				clinic.setClinicTime(signUpDto.getDayTime());
				clinic.setClinicDayoff(signUpDto.getDayOff());
				clinic.setClinicZipcode(signUpDto.getZipcode());
				clinic.setClinicAddr(signUpDto.getAddr());

				if(signUpDto.getContactType().equals("대면") || signUpDto.getContactType().equals("C"))
				{
					type = "C";
				}
				else if(signUpDto.getContactType().equals("비대면") || signUpDto.getContactType().equals("U"))
				{
					type = "U";
				}


				clinic.setUserEmail(signUpDto.getUserEmail());
				clinic.setClinicContactFlag(type);
				clinic.setClinicDetail(signUpDto.getDetail());
				clinic.setClinicDoctor(signUpDto.getUserName());
				clinic.setClinicFax(signUpDto.getFaxNum());
				clinic.setClinicCareer(signUpDto.getCareer());
				clinic.setClinicBreak(signUpDto.getBreakTime());
				clinic.setClinicNight(signUpDto.getNight());
				clinic.setClinicWeekend(signUpDto.getWeekend());

					if(userService.insertClinic(clinic) > 0)
					{
						// 첨부파일 전부 없을 때
						if(clinicFile.isEmpty() && stampFile.isEmpty())
						{
							return "login/login";
						}// 인감 파일만 존재할 때
						else if(stampFile.isEmpty() && !clinicFile.isEmpty())
						{
							if(userService.insertClinicFile(clinicFile, clinic) > 0)
							{
								return "login/login";
							}
						}// 의사 얼굴 파일만 존재할 때
						else if(clinicFile.isEmpty() && !stampFile.isEmpty())
						{
							if(userService.insertStampFile(stampFile, clinic) > 0 )
							{	
								return "login/login";
							}
						}
						else
						{
							if(userService.insertClinicFile(clinicFile, clinic) > 0)
							{
								if(userService.insertStampFile(stampFile, clinic) > 0)
								{	
									return "login/login";
								}
							}
						}
					}
				model.addAttribute("signUpDto", signUpDto);
				return "login/medicalRegister";
			}
			catch(Exception e)
			{
				log.error("sign Exception");
				model.addAttribute("errorMessage", e.getMessage());
				
				return "login/medicalRegister";
			}
		}
		else
		{
			try
			{
				pharmacy.setUserEmail(signUpDto.getUserEmail());
				pharmacy.setPharmacyInstinum(signUpDto.getInstitutionNum());
				pharmacy.setPharmacyRegnum(signUpDto.getRegNum());
				pharmacy.setPharmacyPhone(signUpDto.getInstitutionNum());
				pharmacy.setPharmacyName(signUpDto.getInstitutionName());
				pharmacy.setPharmacistName(signUpDto.getUserName());
				pharmacy.setUserPwd(passwordEncoder.encode(signUpDto.getUserPwd()));
				pharmacy.setPharmacyDayoff(signUpDto.getDayOff());
				pharmacy.setPharmacyTime(signUpDto.getDayTime());
				pharmacy.setPharmacyZipcode(signUpDto.getZipcode());
				pharmacy.setPharmacyAddr(signUpDto.getAddr());
				pharmacy.setPharmacyFax(signUpDto.getFaxNum());
				pharmacy.setPharmacyCareer(signUpDto.getCareer());
				
					if(userService.insertPharmacy(pharmacy) > 0)
					{
						return "login/login";
					}
				
				model.addAttribute("signUpDto", signUpDto);
				return "login/medicalRegister";
			}
			catch(Exception e)
			{
				log.error("sign Exception : " + e);
				model.addAttribute("errorMessage", e.getMessage());
				
				return "login/medicalRegister";
			}
		}
	}
    
    // 일반 회원 회원가입 페이지
    @GetMapping("/register-page")
    public String registerPage(Model model) 
    {
        model.addAttribute("user", new User());
        
    	return "login/register";
    }
    
    // 일반 회원 회원가입 기능
	@PostMapping("/sign")
	public String sign(@Valid User user, BindingResult bindingResult, Model model)
	{
		if(bindingResult.hasErrors())
		{
			model.addAttribute("user", user);
			return "login/register";
		}
		
		User signUser = new User();
		
		try
		{
			signUser.setUserEmail(user.getUserEmail());
			signUser.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
			signUser.setUserName(user.getUserName());
			signUser.setUserPhone(user.getUserPhone());
			signUser.setUserIdentity(user.getUserIdentity());
			
				if(userService.userInsert(signUser) > 0)
				{
					return "redirect:/login-page";
				}
			
			model.addAttribute("user", user);
			return "login/register";
		}
		catch(Exception e)
		{
			log.error("sign Exception");
			model.addAttribute("errorMessage", e.getMessage());
			
			return "login/register";
		}
	}	
	
	@GetMapping("/login/resetPwd")
	public String resetPwd()
	{
		return "login/resetPwd";
	}
	
	//https설정
//	@GetMapping("/healthcheck")
//	public String healthcheck()
//	{
//		return "OK";
//	}	
	
	// 제휴
	@GetMapping("/service")
	public String service()
	{
		return "login/service";
	}
	
	// 소셜로그인 성공 후 처리
	@GetMapping("/login/oauthRegister")
	public String oauthRegister()
	{
		return "login/oauthRegister";
	}
	
	// 소셜로그인 후 정보 추가 입력
	@ResponseBody
	@PostMapping("/register/oauth")
	public ResponseEntity<?> registerOauth(HttpServletRequest request, @RequestParam("userName") String userName, @RequestParam("userPhone") String userPhone, @RequestParam("userIdentity") String userIdentity)
	{
		String token = jwtFilter.extractJwtFromCookie(request);
		log.info(token);
    	String userEmail = jwtFilter.getUsernameFromToken(token);

    	User user = null;
    	
    	if(userEmail.contains("id")) user = userService.findByEmail(userEmail.split(",")[1].substring(7, userEmail.split(",")[1].length()));
    	else if(userEmail.contains("@")) user = userService.findByEmail(userEmail);
    	else user = userService.findByPwd(userEmail);

		if(user != null)
    	{
    		user.setUserName(userName);
    		user.setUserPhone(userPhone);
    		user.setUserIdentity(userIdentity);
    		
    		if(userService.oauthUpdate(user) > 0)
    		{
    			return ResponseEntity.ok(2);
    		}
    	}

    	return ResponseEntity.ok(1);
	}
}
