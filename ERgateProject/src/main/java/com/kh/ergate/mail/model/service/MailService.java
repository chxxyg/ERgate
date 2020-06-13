package com.kh.ergate.mail.model.service;

import java.util.ArrayList;

import com.kh.ergate.mail.model.vo.SearchCondition;
import com.kh.ergate.common.model.vo.PageInfo;
import com.kh.ergate.mail.model.vo.Email;

public interface MailService {

	int selectListCount(String mailTo);
	ArrayList<Email> selectList(PageInfo pi, String mailTo);
	int fselectListCount(String mailFrom);
	ArrayList<Email> fselectList(PageInfo pi, String mailFrom);
	int searchListCount(SearchCondition sc);
	ArrayList<Email> searchList(PageInfo pi, SearchCondition sc);
	
	
	
	
	

}








