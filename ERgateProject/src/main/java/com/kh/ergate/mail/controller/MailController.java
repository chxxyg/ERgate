package com.kh.ergate.mail.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kh.ergate.mail.model.vo.SearchCondition;
import com.kh.ergate.mail.model.vo.mainCount;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kh.ergate.board.model.vo.Board;
import com.kh.ergate.board.model.vo.BoardAttachment;
import com.kh.ergate.common.model.vo.PageInfo;
import com.kh.ergate.common.template.Pagination;
import com.kh.ergate.mail.model.service.MailService;
import com.kh.ergate.mail.model.vo.Email;
import com.kh.ergate.mail.model.vo.MailAttachment;

@Controller
public class MailController {

	@Autowired
	private MailService milService;

	@RequestMapping("list.mil")
	public String selectList(int currentPage, String mailOwn, Model model) {
		int listCount = milService.selectListCount(mailOwn);
		
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 5, 10);
		
		ArrayList<Email> list = milService.selectList(pi, mailOwn);
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		for(int i=0; i<list.size(); i++) {
			list.get(i).setMailDateStr(format1.format(list.get(i).getMailDate()));
		}
		
		model.addAttribute("pi", pi);
		model.addAttribute("list", list);

		return "mail/mailInbox";
	}
	@RequestMapping("flist.mil")
	public String selectToList(int currentPage, String mailOwn, Model model) {

		int listCount = milService.fselectListCount(mailOwn);
		
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 5, 10);
		
		ArrayList<Email> list = milService.fselectList(pi, mailOwn);
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		for(int i=0; i<list.size(); i++) {
			list.get(i).setMailDateStr(format1.format(list.get(i).getMailDate()));
		}
		model.addAttribute("pi", pi);
		model.addAttribute("list", list);

		return "mail/mailSentbox";
	}
	@RequestMapping("ilist.mil")
	public String selectImportToList(int currentPage, String mailOwn, Model model) {

		int listCount = milService.iselectListCount(mailOwn);
		
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 5, 10);
		
		ArrayList<Email> list = milService.iselectList(pi, mailOwn);
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		for(int i=0; i<list.size(); i++) {
			list.get(i).setMailDateStr(format1.format(list.get(i).getMailDate()));
		}
		model.addAttribute("pi", pi);
		model.addAttribute("list", list);

		return "mail/mailImportantbox";
	}
	
	
	@RequestMapping("search.mil")
	public String searchEmailList(String condition, String keyword, String mailOwn, int currentPage, Model model) {
		
		SearchCondition sc = new SearchCondition();
		sc.setMailTo(mailOwn);
		
		switch(condition) {
		case "mailTitle" : sc.setMailTitle(keyword);  break;
		case "mailContent" : sc.setMailContent(keyword); break;
		case "mailnameFrom" : sc.setMailnameFrom(keyword); break;
		}
		
		
		int searchListCount = milService.searchListCount(sc);
		
		PageInfo pi = Pagination.getPageInfo(searchListCount, currentPage, 5, 10);
		
		ArrayList<Email> slist = milService.searchList(pi,sc);
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		for(int i=0; i<slist.size(); i++) {
			slist.get(i).setMailDateStr(format1.format(slist.get(i).getMailDate()));
		}
		model.addAttribute("condition", condition);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pi", pi);
		model.addAttribute("list", slist);
		model.addAttribute("sc", 1);	// 검색된 값인지 일반 게시글 리스트인지 구별하기 위한 값 반환 (sc가 1이면 검색결과, sc라는 키값이 없으면 일반 글목록)
		return "mail/mailInbox";
	}
	
	@RequestMapping("fsearch.mil")
	public String fsearchEmailList(String condition, String keyword, String mailOwn, int currentPage, Model model) {
		
		SearchCondition sc = new SearchCondition();
		sc.setMailFrom(mailOwn);
		
		switch(condition) {
		case "mailTitle" : sc.setMailTitle(keyword);  break;
		case "mailContent" : sc.setMailContent(keyword); break;
		case "mailnameTo" : sc.setMailnameTo(keyword); break;
		}
		
		
		
		int searchListCount = milService.searchListCount(sc);
		
		PageInfo pi = Pagination.getPageInfo(searchListCount, currentPage, 5, 10);
		
		ArrayList<Email> slist = milService.searchList(pi,sc);
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		for(int i=0; i<slist.size(); i++) {
			slist.get(i).setMailDateStr(format1.format(slist.get(i).getMailDate()));
		}
		
		model.addAttribute("condition", condition);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pi", pi);
		model.addAttribute("list", slist);
		model.addAttribute("sc", 1);	// 검색된 값인지 일반 게시글 리스트인지 구별하기 위한 값 반환 (sc가 1이면 검색결과, sc라는 키값이 없으면 일반 글목록)
		return "mail/mailSentbox";
	}
	
	@RequestMapping("isearch.mil")
	public String isearchEmailList(String condition, String keyword, String mailOwn, int currentPage, Model model) {
		
		SearchCondition sc = new SearchCondition();
		sc.setMailOwn(mailOwn);
		
		switch(condition) {
		case "mailTitle" : sc.setMailTitle(keyword);  break;
		case "mailContent" : sc.setMailContent(keyword); break;
		case "mailnameTo" : sc.setMailnameTo(keyword); break;
		case "mailnameFrom" : sc.setMailnameFrom(keyword); break;
		}
		
		
		
		int searchListCount = milService.isearchListCount(sc);
		
		PageInfo pi = Pagination.getPageInfo(searchListCount, currentPage, 5, 10);
		
		ArrayList<Email> slist = milService.isearchList(pi,sc);
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		for(int i=0; i<slist.size(); i++) {
			slist.get(i).setMailDateStr(format1.format(slist.get(i).getMailDate()));
		}
		
		model.addAttribute("condition", condition);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pi", pi);
		model.addAttribute("list", slist);
		model.addAttribute("sc", 1);	// 검색된 값인지 일반 게시글 리스트인지 구별하기 위한 값 반환 (sc가 1이면 검색결과, sc라는 키값이 없으면 일반 글목록)
		return "mail/mailImportantbox";
	}
	
	
	@RequestMapping("detail.mil")
	public String detailMail(int mailNo, int currentPage, String mailOwn, Model model, String pt) {

		int readResult = milService.readFlagUpdate(mailNo);
		Email e = milService.selectMail(mailNo);
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		
		e.setMailDateStr(format1.format(e.getMailDate()));
		
		model.addAttribute("e", e);
		model.addAttribute("pt", pt);
		return "mail/mailDetail";
	}
	
	@ResponseBody
	@RequestMapping(value="detailFile.mil", produces="application/json; charset=utf-8")
	public String fileList(int mailNo) {
		ArrayList<MailAttachment> list = milService.fileList(mailNo);
		return new Gson().toJson(list);
	}
	
	
	@RequestMapping("enrollForm.mil")
	public String enrollForm(@RequestParam(required=false)String empId, Model model) {
		empId = empId + "@ergate.com";
		model.addAttribute("trans", empId);
		return "mail/mailSendForm";
	}
	
	
	@ResponseBody
	@RequestMapping(value="insert.mil", produces="application/json; charset=utf-8")
	public int insertBoard(MultipartHttpServletRequest form, @RequestParam(name="files", required=false) MultipartFile[] files) {
		
		String title[] = form.getParameterValues("mailTitle");
		String content[] = form.getParameterValues("mailContent");
		String tto[] = form.getParameterValues("mailTo");
		String toname[] = form.getParameterValues("mailnameTo");
		String from[] = form.getParameterValues("empId");
		String fromname[] = form.getParameterValues("mailnameFrom");
		String with[] = form.getParameterValues("mailWith");
		String withname[] = form.getParameterValues("mailnameWith");
		String [] to = { "1", "2" };
		to[0] = tto[0].substring(0,tto[0].indexOf('@'));
		
		Email insertE = new Email();
		insertE.setMailTitle(title[0]);
		insertE.setMailContent(content[0]);
		insertE.setMailTo(to[0]);
		insertE.setMailFrom(from[0]);
		insertE.setMailnameTo(toname[0]);
		insertE.setMailnameWith(withname[0]);
		insertE.setMailWith(with[0]);
		if(toname[0].length() == 0) {
			insertE.setMailnameTo(milService.loadMailnameTo(to[0]));
		}
		
		insertE.setMailnameFrom(fromname[0]);
		
		
		int result = 0;
		result = milService.insertMail(insertE);
		String resources = form.getSession().getServletContext().getRealPath("resources");
		String filePath = resources + "\\uploadFiles\\mail\\";
		if(files.length > 0) {
			int flag = 0;
			int setFlag = 0;
			for(int i=0; i<files.length; i++) {
				setFlag = (int)(Math.random()*99) + 10;
				if(setFlag != flag) {
					flag = setFlag;
				}else {
					setFlag += 100;
					flag = setFlag;
				} // 혹시나 Math.random이 같은 값이 나올경우를 대비해서~
				String changeName = saveFile(files[i], form, flag);
				MailAttachment mt = new MailAttachment();
				mt.setChangeName(changeName);
				mt.setOriginName(files[i].getOriginalFilename());
				mt.setMailFileSize(String.valueOf(files[i].getSize()));
				mt.setFilePath(filePath);
				result += milService.insertMailAttachment(mt);
			}
		}else {
			Email currMail = milService.nowMailNo();
			int currNo = currMail.getMailNo();
			result = milService.updateMailFlag(currNo);
		}
		
		
		return result;
	}
	
	public String saveFile(MultipartFile file, HttpServletRequest request, int flag) {
		
		String resources = request.getSession().getServletContext().getRealPath("resources");
		String savePath = resources + "\\uploadFiles\\mail\\";
		// 원본명 (aaa.jpg)
		String originName = file.getOriginalFilename();
		
		// 수정명 (20200522202011.jpg)
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		// 반복시의 구별값
		currentTime += flag;
		
		// 확장자 (String ext)
		String ext = originName.substring(originName.lastIndexOf(".")); // ".jpg"
		
		String changeName = currentTime + ext;
		
				
		try {
			file.transferTo(new File(savePath + changeName));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return changeName;
	}
	
	public void deleteFile(String fileName, HttpServletRequest request) {
		String resources = request.getSession().getServletContext().getRealPath("resources");
		String savePath = resources + "\\uploadFiles\\mail\\";
		File deleteFile = new File(savePath + fileName);
		deleteFile.delete();
	}
	
	  //결재라인등록팝업 요청용
	  @RequestMapping("openSigner.mil")
	  public String openSigner(String documentNo,HttpSession session,Model model) {

		  model.addAttribute("documentNo",documentNo);
		  return "mail/mailAddToList";
	  }
	  
	@RequestMapping("delete.mil")
	public String deleteMail(int mailNo[], String mailOwn, Model model) {
		
		int result = 0;
		for(int i=0; i<mailNo.length; i++) {
			result = milService.deleteMail(mailNo[i]);
		}
		
		return "redirect:list.mil?currentPage=1&mailOwn=" + mailOwn;
	}  
	
	@ResponseBody
	@RequestMapping("importFlagUpdate.mil")
	public int importFlagUpdate(int mailNo) {
		
		int result = 0;
		Email e = milService.selectMail(mailNo);
		if(e.getMailImportFlag().equals("Y")) {
			
			result = milService.unImportFlagUpdate(mailNo);
		}else if(e.getMailImportFlag().equals("N")) {
			result = milService.importFlagUpdate(mailNo);
		}
			
		return result;
	}
	@RequestMapping("replyMail.mil")
	public String replyMail(int mailNo, Model model) {
		Email e = milService.selectMail(mailNo);
		model.addAttribute("m", e);
		model.addAttribute("f", 0);
		return "mail/mailSentDetail";
	}
	@RequestMapping("transMail.mil")
	public String transMail(int mailNo, Model model) {
		Email e = milService.selectMail(mailNo);
		model.addAttribute("m", e);
		model.addAttribute("f", 1);
		return "mail/mailSentDetail";
	}
	
	@ResponseBody
	@RequestMapping("mailCount.mil")
	public mainCount mailCount(String mailOwn) {
		
		mainCount mc = new mainCount();
		mc.setNotReadMail(milService.notReadMail(mailOwn));
		mc.setFromMail(milService.fromMail(mailOwn));
		mc.setImportMail(milService.importMail(mailOwn));
			
		return mc;
	}
	
	@ResponseBody
	@RequestMapping("miniFromMailList.mil")
	public ArrayList<Email> miniFromMailList(String mailOwn) {
		
		ArrayList<Email> list = milService.miniFromMailList(mailOwn);
		return list;
	}
	@ResponseBody
	@RequestMapping("miniToMailList.mil")
	public ArrayList<Email> miniToMailList(String mailOwn) {
		
		ArrayList<Email> list = milService.miniToMailList(mailOwn);
		return list;
	}
	@ResponseBody
	@RequestMapping("miniImportMailList.mil")
	public ArrayList<Email> miniImportMailList(String mailOwn) {
		ArrayList<Email> list = milService.miniImportMailList(mailOwn);
		return list;
	}
	
	
}
