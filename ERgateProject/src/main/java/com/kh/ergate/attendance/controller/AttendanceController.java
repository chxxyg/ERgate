package com.kh.ergate.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.kh.ergate.attendance.model.service.AttendanceService;

@Controller
public class AttendanceController {

	@Autowired
	private AttendanceService atService;
	
	
}
