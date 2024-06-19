package com.todoc.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.todoc.web.dao.MegazinesDao;
import com.todoc.web.dto.Megazines;
import com.todoc.web.dto.MegazinesFile;
import com.todoc.web.dto.MegazinesLike;
import com.todoc.web.util.FileUtil;

@Service
public class MegazinesService {
	private static Logger logger = LoggerFactory.getLogger(MegazinesService.class);

	@Autowired
	private MegazinesDao megazinesDao;
	
	//매거진 이미지 파일 경로
	@Value("${megazines.upload.dir}")
	private String megazinesFileUploadDir;

	
	//게시물 리스트
	public List<Megazines> MegazinesList(Megazines megazines){
		List<Megazines> list = null;
		MegazinesFile megazinesFile = null;
		try {
			list =megazinesDao.MegazinesList(megazines);
			
			for(int i= 0; i < list.size(); i++) {
				megazinesFile = megazinesDao.megazineFileSelect(list.get(i).getNewsSeq());
				list.get(i).setMegazinesFile(megazinesFile);
			}
			
		
		} catch (Exception e) {
			logger.error("[MegazinesService] MegazinesList Exception", e);
		}
		return list;
	}
		
	//게시물 수 count
	public long megazinesListCount(Megazines megazines) {
		long count = 0;
		try {
			count =megazinesDao.megazinesListCount(megazines);
		} catch (Exception e) {
			logger.error("[MegazinesService] megazinesListCount Exception", e);
		}
		
		
		return count;
	}
	
	//게시물 상세 조회 (조회수 증가 O)
	public Megazines megazinesDetail(long newsSeq) {
		Megazines megazines = null;
		try {
			megazines = megazinesDao.megazinesDetail(newsSeq);
			//조회수 증가
			if(megazines != null) {  
				megazinesDao.megazinesReadCntPlus(newsSeq);
			}
			//첨부파일 조회
			MegazinesFile megazinesFile  =megazinesDao.megazineFileSelect(newsSeq);
			if(megazinesFile != null) {
				megazines.setMegazinesFile(megazinesFile);
			}

		} catch (Exception e) {
			logger.error("[MegazinesService] megazinesDetail Exception", e);
		}
		return megazines;
	}
	
	
	
	//좋아요 클릭
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int megazinesLike(MegazinesLike megazinesLike) throws Exception {
		int result = 0;

			if(megazinesLike != null) {
				if(megazinesDao.checkLikeCnt(megazinesLike) >= 1) {
					if(megazinesDao.likeDelete(megazinesLike) > 0) {
						result = megazinesDao.likeCntMinus(megazinesLike.getNewsSeq());
					}
				}else {
					if(megazinesDao.megazinesLikeCntPlus(megazinesLike) > 0) {
						result = megazinesDao.likeCntPlus(megazinesLike.getNewsSeq());
					}
				}
			}

		return result;
	}
	
	//좋아요 클릭한 유저인지 여부
	public long checkLikeCnt(MegazinesLike megazinesLike) {
		long result = 0;
		
		try {
			result = megazinesDao.checkLikeCnt(megazinesLike);
		} catch (Exception e) {
			logger.error("[MegazinesService] checkLikeCnt Exception", e);

		}

		return result;
	}
	
	
		//게시물 등록 트렌젝션(하나라도 오류가 나면 롤백시키는것) 처리 해야함. 일단 게시물은 hiBoard테이블인서트후 첨부파일 이쓰염ㄴ file테이블도 인서트 해야함. 후에 tranjaction 어노테이션 처리할 예정
		@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
		public int megazinesInsert(Megazines megazines) throws Exception{  
			
			int count = 0;
			
		
				count=megazinesDao.megazineInsert(megazines);
				
				if(count > 0 && megazines.getMegazinesFile() != null ) {  
					MegazinesFile megazinesFile = megazines.getMegazinesFile();
					megazinesFile.setNewsSeq(megazines.getNewsSeq());
					megazinesFile.setFileSeq((short)1);
	
					megazinesDao.megazineFileInsert(megazinesFile);

				}
			
			return count;

		}


	
	
	//첨부파일 조회
	public MegazinesFile megazineFileSelect(long newsSeq) {
		MegazinesFile megazinesFile = null;
		
		try {
			megazinesFile=megazinesDao.megazineFileSelect(newsSeq);
		} catch (Exception e) {
			logger.error("[MegazinesService] megazineFileSelect Exception", e);
		}
		
		
		
		return megazinesFile;
	}
	
	

	

	
	//매거진 수정물 조회(조회수 증가 포함 X)
	public Megazines megazinesSelect(long newsSeq) {
		Megazines megazines = null;
		try {
			megazines = megazinesDao.megazinesDetail(newsSeq);
			
			//첨부파일 조회
			MegazinesFile megazinesFile  =megazinesDao.megazineFileSelect(newsSeq);
			if(megazinesFile != null) {
				megazines.setMegazinesFile(megazinesFile);
			}

		} catch (Exception e) {
			logger.error("[MegazinesService] megazinesDetail Exception", e);
		}
		return megazines;
	}
	
	
	
	
	//매거진 글 삭제 + 첨부파일 삭제
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int megazineDelete(long newsSeq) throws Exception {
		int result = 0;
		Megazines megazines = megazinesSelect(newsSeq);
		if(megazines != null) {
			
			result = megazinesDao.megazineDelete(newsSeq);
			if(result > 0) {
				MegazinesFile megazinesFile = megazines.getMegazinesFile();
				
				if(megazinesFile != null) {
					if(megazinesDao.megazineFileDelete(newsSeq) > 0) {
						FileUtil.deleteFile(megazinesFileUploadDir + FileUtil.getFileSeparator() + megazinesFile.getFileName());
					}
				}
			}
		}
		
		
		return result;
	}
	
	//게시물 수정(첨부파일 삭제 후 인서트)
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int megazineUpdate(Megazines megazines) throws Exception{
		int count = 0;
		
		count =megazinesDao.megazineUpdate(megazines);
		if(count > 0 && megazines.getMegazinesFile()!= null) {
			MegazinesFile delmegazinesFile = megazinesDao.megazineFileSelect(megazines.getNewsSeq());
			if(delmegazinesFile != null) {
				FileUtil.deleteFile(megazinesFileUploadDir + FileUtil.getFileSeparator() + delmegazinesFile.getFileName());
				
				int a = megazinesDao.megazineFileDelete(megazines.getNewsSeq());

			}
			
			MegazinesFile megazinesFile = megazines.getMegazinesFile();

			megazinesFile.setNewsSeq(megazines.getNewsSeq());
			megazinesFile.setFileSeq((short)1);
			
			int b = megazinesDao.megazineFileInsert(megazinesFile);

		}
		
		return count;
	}
	
		


	
}
