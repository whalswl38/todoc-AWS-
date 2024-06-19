package com.todoc.web.dto;

import lombok.Data;

@Data
public class SuppleFile 
{
	private long suppleSeq;
	private long fileSeq;
	private String fileOrgName;
	private String fileName;
	private String fileExt;
	private long fileSize;
	private String fileRegdate;
}
