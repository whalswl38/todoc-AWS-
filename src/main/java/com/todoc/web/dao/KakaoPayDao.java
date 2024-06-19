package com.todoc.web.dao;

import org.apache.ibatis.annotations.Mapper;

import com.todoc.web.dto.PayLog;

@Mapper
public interface KakaoPayDao {

	int insertPayLog(PayLog payLog);

}
