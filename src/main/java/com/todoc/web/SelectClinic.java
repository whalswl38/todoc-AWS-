package com.todoc.web;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectClinic {

	private String doctor;
    private String name;
    private Double avgRate;
    private int reviewCnt;
    private boolean possibleTodayReservation;
    private String todayTime;
    private String clinicItem;
    private boolean videoDiagnosis;
    private String profileImg;

}
