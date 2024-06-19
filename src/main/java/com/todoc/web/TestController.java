package com.todoc.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.todoc.web.controller.ClinicContactController;
import com.todoc.web.dto.Megazines;
import com.todoc.web.service.MegazinesService;


@Controller
@RequestMapping
public class TestController {
	
	private static final Logger log = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private MegazinesService megazinesService;
	
    @GetMapping("/main-page")
    public String test2(Model model) {
    	
    	//건강매거진 메인페이지 리스트
    	
    	Megazines search = new Megazines();
    	search.setStartRow(1);
		search.setEndRow(2);
		List<Megazines> list = null;
		list = megazinesService.MegazinesList(search);
		
		
		model.addAttribute("list", list);
    	
    	
    	return "main/main";
    }

}
