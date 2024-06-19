package com.todoc.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.Supple;
import com.todoc.web.dto.SuppleFile;

@Mapper
public interface SuppleDao 
{
	// 글 모두 조회
    List<Supple> suppleList(Supple supple);
	
	// 글번호로 글 조회
	Supple selectSupple(long suppleSeq);
	
	// 첨부파일 조회
	List<SuppleFile> selectSuppleFile(long suppleSeq);
	
	// 글 입력
	int insertSupple(Supple supple);
	
	// 파일 입력
	int insertSuppleFile(SuppleFile suppleFile);
	
	// 파일 삭제
	int deleteSuppleFile(long suppleSeq);
	
	// 글 수정
	int updateSupple(Supple supple);
	
	// 글 삭제
	int deleteSupple(long suppleSeq);
	
	// 글 갯수 조회
	long countSupple(Supple supple);
}
