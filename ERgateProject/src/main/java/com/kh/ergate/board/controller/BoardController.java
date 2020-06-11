package com.kh.ergate.board.controller;

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
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kh.ergate.board.model.service.BoardService;
import com.kh.ergate.board.model.vo.Board;
import com.kh.ergate.board.model.vo.BoardAttachment;
import com.kh.ergate.board.model.vo.ReReply;
import com.kh.ergate.board.model.vo.Reply;
import com.kh.ergate.board.model.vo.SearchCondition;
import com.kh.ergate.common.model.vo.PageInfo;
import com.kh.ergate.common.template.Pagination;

@Controller
public class BoardController {

	@Autowired
	private BoardService bodService;

	@RequestMapping("list.bo")
	public String selectList(int currentPage, Model model, @RequestParam(value="deleteFlag", required=false, defaultValue="0") int flag) {

		int listCount = bodService.selectListCount();
		
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 5, 10);
		
		ArrayList<Board> list = bodService.selectList(pi);
		if(flag == 1) {
			model.addAttribute("msg", "게시글 삭제 성공");	
		}
		model.addAttribute("pi", pi);
		model.addAttribute("list", list);

		return "board/boardList";
	}
	
	@RequestMapping("search.bo")
	public String searchBoardList(String condition, String keyword, int currentPage, Model model) {
		
		SearchCondition sc = new SearchCondition();
		
		switch(condition) {
		case "boardTitle" : sc.setBoardTitle(keyword);  break;
		case "boardContent" : sc.setBoardContent(keyword); break;
		case "boardWriter" : sc.setBoardWriter(keyword); break;
		}
		
		
		
		int searchListCount = bodService.searchListCount(sc);
		
		PageInfo pi = Pagination.getPageInfo(searchListCount, currentPage, 5, 10);
		
		ArrayList<Board> slist = bodService.searchList(pi,sc);
		
		model.addAttribute("condition", condition);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pi", pi);
		model.addAttribute("list", slist);
		model.addAttribute("sc", 1);	// 검색된 값인지 일반 게시글 리스트인지 구별하기 위한 값 반환 (sc가 1이면 검색결과, sc라는 키값이 없으면 일반 글목록)
		return "board/boardList";
	}
	
	@RequestMapping("detail.bo") 
	public ModelAndView selectBoard(int bno, ModelAndView mv, int currentPage) {
	
		int result = bodService.increaseCount(bno);
	
		if(result > 0) {
			Board b = bodService.selectBoard(bno); 
			mv.addObject("b", b);
			mv.addObject("currentPage", currentPage);
			mv.setViewName("board/boardDetail");
		}else { // 게시글 상세조회 실패
			mv.addObject("msg", "게시글 상세조회 실패!"); 
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value="detailFile.bo", produces="application/json; charset=utf-8")
	public String fileList(int refBoardNo) {
		
		ArrayList<BoardAttachment> list = bodService.fileList(refBoardNo);
		return new Gson().toJson(list);
	}
	
	@ResponseBody
	@RequestMapping(value="rlist.bo", produces="application/json; charset=utf-8")
	public String replyList(int refBno) {
		
		ArrayList<Reply> list = bodService.replyList(refBno);
		return new GsonBuilder().setDateFormat("yyyy년 MM월 dd일 HH:mm:ss").create().toJson(list);
	}
	
	@ResponseBody
	@RequestMapping(value="relist.bo", produces="application/json; charset=utf-8")
	public String rereplyList(int refRno) {
		
		ArrayList<ReReply> list = bodService.rereplyList(refRno);
		return new GsonBuilder().setDateFormat("yyyy년 MM월 dd일 HH:mm:ss").create().toJson(list);
	}
	@ResponseBody
	@RequestMapping(value="beforeAfter.bo", produces="application/json; charset=utf-8")
	public String beforAfter(int refBoardNo) {
		
		ArrayList<Board> list = new ArrayList<>();
		list.add(bodService.beforeB(refBoardNo));
		list.add(bodService.afterB(refBoardNo));
		return new GsonBuilder().setDateFormat("yyyy/MM/dd").create().toJson(list);
	}
	
	@RequestMapping("enrollForm.bo")
	public String enrollForm() {
		return "board/boardEnrollForm";
	}
	
	@ResponseBody
	@RequestMapping(value="insert.bo", produces="application/json; charset=utf-8")
	public int insertBoard(MultipartHttpServletRequest form, @RequestParam(name="files", required=false) MultipartFile[] files) {
		//System.out.println(files.length);
		String title[] = form.getParameterValues("boardTitle");
		String content[] = form.getParameterValues("boardContent");
		String writer[] = form.getParameterValues("boardWriter");
		String empId[] = form.getParameterValues("empId");
		//System.out.println("제목값은? : " + title[0]); 
		//System.out.println("내용값은? : " + content[0]);
		//System.out.println("이름은? : " + content[0]);
		Board insertB = new Board();
		insertB.setBoardTitle(title[0]);
		insertB.setBoardContent(content[0]);
		insertB.setBoardWriter(writer[0]);
		insertB.setEmpId(empId[0]);
		
		int result = 0;
		result = bodService.insertBoard(insertB);
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
				BoardAttachment bt = new BoardAttachment();
				bt.setChangeName(changeName);
				bt.setOriginName(files[i].getOriginalFilename());
				result += bodService.insertBoardAttachment(bt);
			}
		}else {
			result = bodService.updateBoardFlag();
		}
		
		
		return result;
	}
	
	public String saveFile(MultipartFile file, HttpServletRequest request, int flag) {
		
		String resources = request.getSession().getServletContext().getRealPath("resources");
		String savePath = resources + "\\uploadFiles\\board\\";
		System.out.println("생성시 : " + resources);
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
		String savePath = resources + "\\uploadFiles\\board\\";
		System.out.println("삭제시: " + resources);
		File deleteFile = new File(savePath + fileName);
		deleteFile.delete();
	}
	@ResponseBody
	@RequestMapping(value="insertReply.bo", produces="application/json; charset=utf-8")
	public int insertReply(Reply repl) {
		int result = bodService.insertReply(repl);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="insertReReply.bo", produces="application/json; charset=utf-8")
	public String insertReReply(ReReply replForm) {
		int refRno = replForm.getRefRno();
	
		int result = bodService.insertReReply(replForm);
		ReReply rrepl = bodService.rereplyOne(refRno);
		
		return new GsonBuilder().setDateFormat("yyyy. MM. dd HH:mm:ss").create().toJson(rrepl);
	}
	
	@ResponseBody
	@RequestMapping(value="updateReply.bo", produces="application/json; charset=utf-8")
	public int updateReply(Reply repl) {
		int result = bodService.updateReply(repl);
		return result;
	}
	@ResponseBody
	@RequestMapping(value="updateReReply.bo", produces="application/json; charset=utf-8")
	public int updateReReply(ReReply repl) {
		int result = bodService.updateReReply(repl);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="deleteReply.bo", produces="application/json; charset=utf-8")
	public int deleteReply(int replyNo) {
		int result = bodService.deleteReply(replyNo);
		return result;
	}
	@ResponseBody
	@RequestMapping(value="deleteReReply.bo", produces="application/json; charset=utf-8")
	public int deleteReReply(int replyNo) {
		int result = bodService.deleteReReply(replyNo);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="checkHaveReply.bo", produces="application/json; charset=utf-8")
	public int checkHaveReply(int replyNo) {
		int result = bodService.checkHaveReply(replyNo);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="replyForceDelete.bo", produces="application/json; charset=utf-8")
	public int replyForceDelete(int replyNo) {
		int result = bodService.replyForceDelete(replyNo);
		return result;
	}
	
	@RequestMapping("update.bo")
	public ModelAndView updateBoard(int bno, int currentPage, ModelAndView mv) {

		Board b = bodService.selectBoard(bno);
		ArrayList<BoardAttachment> bt = bodService.fileList(bno);
			mv.addObject("b", b);
			mv.addObject("currentPage", currentPage);
			mv.addObject("btList", bt);
			mv.setViewName("board/boardEnrollForm");
		return mv;
	}
	
	@RequestMapping("realUpdate.bo")
	public ModelAndView realUpdateBoard(int bno, int currentPage, ModelAndView mv) {

		Board b = bodService.selectBoard(bno); 
			mv.addObject("b", b);
			mv.addObject("currentPage", currentPage);
			mv.setViewName("board/boardEnrollForm");
		return mv;
	}
	
	@RequestMapping("delete.bo")
	public String deleteBoard(int bno, Model model, HttpServletRequest request) {
		
		int result = bodService.deleteBoard(bno);
		if(result > 0) {
			ArrayList<BoardAttachment> list = new ArrayList<>();
			list = bodService.fileList(bno);
			String[] fileName = new String[list.size()];
			if(list.size() > 0) {
				for(int i=0; i<list.size(); i++) {
					fileName[i] = list.get(i).getChangeName();
					//System.out.println(fileName[i]);
					deleteFile(fileName[i], request);
				}
				
			}
			
			return "redirect:list.bo?currentPage=1&deleteFlag=1";
		}else {
			model.addAttribute("msg", "게시글 삭제 실패");
			return "common/errorPage";
		}

	}
	/*
	 * @RequestMapping("delete.bo") public String deleteBoard(int bno, String
	 * fileName, HttpServletRequest request, Model model) {
	 * 
	 * int result = bService.deleteBoard(bno);
	 * 
	 * if(result > 0) { // 게시글 삭제 성공 --> 기존의 첨부파일이 있었을 경우 서버에 삭제
	 * 
	 * // 기존의 첨부파일이 있었을 경우(fileName에 빈문자열이 아닐꺼임)만 서버에 업로드된 파일 삭제
	 * if(!fileName.equals("")) { deleteFile(fileName, request); }
	 * 
	 * return "redirect:list.bo?currentPage=1";
	 * 
	 * }else { // 게시글 삭제 실패
	 * 
	 * model.addAttribute("msg", "게시글 삭제 실패!!"); return "common/errorPage";
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping("updateForm.bo") public String updateForm(int bno, Model
	 * model) {
	 * 
	 * model.addAttribute("b", bService.selectBoard(bno)); return
	 * "board/boardUpdateForm";
	 * 
	 * }
	 * 
	 * @RequestMapping("update.bo") public String updateBoard(Board b,
	 * HttpServletRequest request, Model model,
	 * 
	 * @RequestParam(name="reUploadFile", required=false) MultipartFile file) {
	 * 
	 * // 새로 넘어온 첨부파일이 있을 경우 --> 서버에 업로드 해야됨
	 * if(!file.getOriginalFilename().equals("")) {
	 * 
	 * // 기존의 첨부파일의 있었을 경우 --> 업로드 된 파일 지워야 됨 if(b.getChangeName() != null) { // 새로
	 * 넘어온 첨부파일도 있고 기존의 첨부파일도 있었을 경우 deleteFile(b.getChangeName(), request); }
	 * 
	 * String changeName = saveFile(file, request);
	 * 
	 * b.setChangeName(changeName); b.setOriginName(file.getOriginalFilename());
	 * 
	 * }
	 * 
	 * 
	 * b = 게시글번호, 게시글제목, 게시글내용
	 * 
	 * 1. 기존의 첨부파일 X, 새로 첨부된 파일 X --> originName : null, changeName : null
	 * 
	 * 2. 기존의 첨부파일 X, 새로 첨부된 파일 O --> 서버에 업로드 처리 후 --> originName : 새로첨부된파일원본명,
	 * changeName : 새로첨부된파일수정명
	 * 
	 * 3. 기존의 첨부파일 O, 새로 첨부된 파일 X --> originName : 기존첨부파일원본명, changeName : 기존첨부파일수정명
	 * 
	 * 4. 기존의 첨부파일 O, 새로 첨부된 파일 O --> 기존의 첨부파일 삭제 후 --> 새로첨부된 파일 서버에 업로드 후 -->
	 * originName : 새로첨부된파일원본명, changeName : 새로첨부된파일수정명
	 * 
	 * 
	 * int result = bService.updateBoard(b);
	 * 
	 * if(result > 0) {
	 * 
	 * return "redirect:detail.bo?bno=" + b.getBoardNo();
	 * 
	 * }else { model.addAttribute("msg", "게시글 수정 실패!!"); return "common/errorPage";
	 * }
	 * 
	 * }
	 * 
	 * 
	 * 
	 * // 전달받은 파일명을 가지고 서버로 부터 삭제하는 메소드 public void deleteFile(String fileName,
	 * HttpServletRequest request) { String resources =
	 * request.getSession().getServletContext().getRealPath("resources"); String
	 * savePath = resources + "\\uploadFiles\\";
	 * 
	 * File deleteFile = new File(savePath + fileName); deleteFile.delete(); }
	 * 
	 * 
	 * // 공유해서 쓸수 있게끔 따로 정의 해놓은 메소드 // 전달받은 파일을 서버에 업로드 시킨 후 수정명 리턴하는 메소드 public
	 * String saveFile(MultipartFile file, HttpServletRequest request) {
	 * 
	 * // 파일을 업로드 시킬 폴더 경로 (String savePath) String resources =
	 * request.getSession().getServletContext().getRealPath("resources"); String
	 * savePath = resources + "\\uploadFiles\\";
	 * 
	 * // 원본명 (aaa.jpg) String originName = file.getOriginalFilename();
	 * 
	 * // 수정명 (20200522202011.jpg) // 년월일시분초 (String currentTime) String currentTime
	 * = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); //
	 * "20200522202011"
	 * 
	 * // 확장자 (String ext) String ext =
	 * originName.substring(originName.lastIndexOf(".")); // ".jpg"
	 * 
	 * String changeName = currentTime + ext;
	 * 
	 * 
	 * try { file.transferTo(new File(savePath + changeName)); } catch
	 * (IllegalStateException | IOException e) { e.printStackTrace(); }
	 * 
	 * return changeName;
	 * 
	 * }
	 */

}
