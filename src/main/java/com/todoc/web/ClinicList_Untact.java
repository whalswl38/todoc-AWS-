package com.todoc.web;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClinicList_Untact {
    public ClinicList_Untact(boolean b, String string, String string2, boolean c, String string3, double d, String string4,
			Object object, Object object2, String string5, String string6) {
		// TODO Auto-generated constructor stub
	}
	private boolean hasAd;
    private String name;
    private String keyword;
    private boolean isOperationTime;
    private String endTime;
    private double distance;
    private String address;
    private Integer visitReviewCnt;
    private Integer blogReviewCnt;
    private String profileImg;
    private String clinicId;
}
