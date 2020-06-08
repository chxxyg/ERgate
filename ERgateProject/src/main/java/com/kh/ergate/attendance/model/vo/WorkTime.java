package com.kh.ergate.attendance.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WorkTime {

	private int workTimeCode;
	private String arrive;
	private String leave;
	private String halfdayOff;
	
}
