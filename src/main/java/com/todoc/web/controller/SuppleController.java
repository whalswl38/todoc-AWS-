package com.todoc.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.todoc.web.dto.Supple;
import com.todoc.web.dto.SuppleFile;
import com.todoc.web.service.SuppleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping
public class SuppleController 
{
	@Autowired
	private SuppleService suppleService;
	
	// 리스트
	@GetMapping("/nutrients-list-page")
	public String nutriListPage(
	        HttpServletRequest request,
	        @RequestParam(required = false, defaultValue = "1") long curPage,
	        @RequestParam(required = false, defaultValue = "10") long pageSize,
	        @RequestParam(required = false, defaultValue = "") String searchValue,
	        Model model
	)
	{
	    Supple supple = new Supple();
	    if (!searchValue.isEmpty()) {
	        supple.setSearchValue(searchValue); // 검색어 설정
	    }

	    long totalCount = suppleService.countSupple(supple);
	    long totalPages = (totalCount + pageSize - 1) / pageSize;

	    long start = (curPage - 1) * pageSize + 1;
	    long end = curPage * pageSize;

	    supple.setStart(start);
	    supple.setEnd(end);

	    List<Supple> list = suppleService.suppleList(supple);

	    // 각 Supple 객체에 파일 정보 추가
	    for (Supple s : list) 
	    {
	        List<SuppleFile> fileList = suppleService.selectSuppleFile(s.getSuppleSeq());
	        s.setSuppleFile(fileList);
	    }

	    model.addAttribute("list", list);
	    model.addAttribute("curPage", curPage);
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("totalCount", totalCount);
	    model.addAttribute("searchValue", searchValue); // 검색어 전달

	    return "nutrients/nutrientsList";
	}

    // 글 상세보기
    @GetMapping("/nutrients-detail-page")
    public String nutriDetailPage(@RequestParam(name = "id") Long id, Model model) 
    {
    	Supple supple = suppleService.selectSupple(id);
    	model.addAttribute("supple", supple);
        return "nutrients/nutrientsDetail";
    }
    
    // 글 작성 페이지
    @GetMapping("/nutrients-write")
    public String write()
    {
    	return "nutrients/nutrientsWrite";
    }
    
    // 글 저장
	@PostMapping("/nutri/save")
	@ResponseBody
	public ResponseEntity<?> saveSupple(@RequestParam("uploadFile") MultipartFile[] multipartFile
			, @RequestParam("suppleName") String suppleName, @RequestParam("suppleTitle") String suppleTitle, @RequestParam("suppleRaw") String suppleRaw, 
			@RequestParam("suppleCaution") String suppleCaution, @RequestParam("suppleFormula") String suppleFormula,
			@RequestParam("suppleCompany") String suppleCompany,@RequestParam("suppleFunction") String suppleFunction,
			@RequestParam("suppleDose") String suppleDose, @RequestParam("suppleLink") String suppleLink, @RequestParam("suppleEfficacy") String suppleEfficacy)
	{
		if(!suppleDose.isEmpty() && !suppleCaution.isEmpty() && !suppleCompany.isEmpty() && !suppleFormula.isEmpty()
				&& !suppleFunction.isEmpty() && !suppleLink.isEmpty() && !suppleName.isEmpty() && !suppleRaw.isEmpty() && !suppleEfficacy.isEmpty())
		{
			Supple supple = new Supple();

			supple.setSuppleDose(suppleDose);
			supple.setSuppleTitle(suppleTitle);
			supple.setSuppleCaution(suppleCaution);
			supple.setSuppleCompany(suppleCompany);
			supple.setSuppleFormula(suppleFormula);
			supple.setSuppleFunction(suppleFunction);
			supple.setSuppleEfficacy(suppleEfficacy);
			supple.setSuppleLink(suppleLink);
			supple.setSuppleName(suppleName);
			supple.setSuppleRaw(suppleRaw);

			int count = suppleService.saveSupple(multipartFile, supple);
			
			return ResponseEntity.ok(count);
		}
		
		return ResponseEntity.ok(404);
	}
	
	// 글 삭제
	@PostMapping("/nutri/delete")
	@ResponseBody
	public ResponseEntity<?> deleteNutri(@RequestParam("suppleSeq") String suppleSeq)
	{
		Supple supple = suppleService.selectSupple(Integer.valueOf(suppleSeq));
		
		if(supple != null)
		{
			if(suppleService.deleteSupple(supple.getSuppleSeq()) > 0)
			{
				return ResponseEntity.ok(1);
			}
			
			return ResponseEntity.ok(500);
		}
		
		return ResponseEntity.ok(404);
	}
	
	// 글 수정 버튼 클릭
	@PostMapping("/nutri/update")
	public String nutriUpdate(HttpServletRequest request, Model model)
	{
		String suppleSeq = request.getParameter("suppleSeq");
		
		if(Integer.valueOf(suppleSeq) > 0)
		{
			Supple supple = suppleService.selectSupple(Integer.valueOf(suppleSeq));
			
			if(supple.getSuppleStatus().equals("Y"))
			{
				model.addAttribute("supple", supple);
				return "nutrients/nutrientsUpdate";
			}
			
			return "500";
		}
				
		return "404";
	}
	
    // 글 수정
    @PostMapping("/nutri/updateProc")
    @ResponseBody
    public ResponseEntity<?> updateSupple(HttpServletRequest request, 
    		@RequestParam("uploadFile") MultipartFile[] multipartFile, 
            @RequestParam("suppleName") String suppleName,
            @RequestParam("suppleTitle") String suppleTitle,
            @RequestParam("suppleRaw") String suppleRaw,
            @RequestParam("suppleCaution") String suppleCaution,
            @RequestParam("suppleFormula") String suppleFormula,
            @RequestParam("suppleCompany") String suppleCompany,
            @RequestParam("suppleFunction") String suppleFunction,
            @RequestParam("suppleDose") String suppleDose,
            @RequestParam("suppleLink") String suppleLink,
            @RequestParam("suppleEfficacy") String suppleEfficacy
    ) 
    {    	
    	long suppleSeq = Integer.valueOf(request.getParameter("suppleSeq"));
        Supple supple = suppleService.selectSupple(suppleSeq);

        if(supple != null)
        {
        	if(supple.getSuppleFile().size() > 0)
        	{
        		if(suppleService.deleteSuppleFile(suppleSeq) <= 0)
        		{    	         
        	        return ResponseEntity.status(500).body("글 수정 중 오류가 발생했습니다.");
        		}	
        	}
        	
	        supple.setSuppleSeq(suppleSeq);
	        supple.setSuppleName(suppleName);
	        supple.setSuppleTitle(suppleTitle);
	        supple.setSuppleRaw(suppleRaw);
	        supple.setSuppleCaution(suppleCaution);
	        supple.setSuppleFormula(suppleFormula);
	        supple.setSuppleCompany(suppleCompany);
	        supple.setSuppleFunction(suppleFunction);
	        supple.setSuppleDose(suppleDose);
	        supple.setSuppleLink(suppleLink);
	        supple.setSuppleEfficacy(suppleEfficacy);
	        
	        if (suppleService.updateSupple(multipartFile, supple) > 0) 
	        {
	            return ResponseEntity.ok(1);
	        }         	
        }
        
        return ResponseEntity.status(404).body("글 수정 중 오류가 발생했습니다.");
    }
}
