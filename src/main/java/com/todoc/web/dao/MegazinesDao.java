package com.todoc.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.Megazines;
import com.todoc.web.dto.MegazinesFile;
import com.todoc.web.dto.MegazinesLike;

@Mapper
public interface MegazinesDao {

	
	//게시물 리스트
	public List<Megazines> MegazinesList(Megazines megazines);
		
	//게시물 수 count
	public long megazinesListCount(Megazines megazines);
	
	//게시물 상세
	public Megazines megazinesDetail(long newsSeq);
	
	//게시물 조회수 증가
	public int megazinesReadCntPlus(long newsSeq);
	
	//좋아요 증가
	public int megazinesLikeCntPlus(MegazinesLike megazinesLike);
	
	//좋아요 클릭 여부 체크
	public long checkLikeCnt(MegazinesLike megazinesLike);
	
	//좋아요 삭제
	public int likeDelete(MegazinesLike megazinesLike);
	
	//좋아요 총 수
	public long totalLikeCnt(long newsSeq);
	
	//좋아요 megazine 증가
	public int likeCntPlus(long newsSeq);
	
	//좋아요 megazine 감소
	public int likeCntMinus(long newsSeq);
	
	//글 등록
	public int megazineInsert(Megazines megazines);
	
	//첨부파일 등록
	public int megazineFileInsert(MegazinesFile megazinesFile);
	
	//첨부파일 조회
	public MegazinesFile megazineFileSelect(long newsSeq);
	
	//게시글 삭제
	public int megazineDelete(long newsSeq);
	
	//첨부파일 삭제
	public int megazineFileDelete (long newsSeq);
	
	//게시글 수정
	public int megazineUpdate (Megazines megazines);
	
	
	
	
}
