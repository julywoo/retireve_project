package org.retrieve.controller;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.retrieve.board.BoardDAO;
import org.retrieve.board.BoardVO;
import org.retrieve.controller.WebView;


public class BoardController extends CommandController {
	
	private static final long serialVersionUID = 1L;
    BoardDAO dao = new BoardDAO();	
		

	
	public WebView index(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		return new WebView("/WEB-INF/main.html");
	}
	
	public WebView regist(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		System.out.println("regist");
		BoardVO vo = new BoardVO();
		
		vo.setContent(request.getParameter("content"));
		
		dao.create(vo);
		
		return new WebView("/board?method=list&pageno=1");
	}
//	public WebView view(HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		
//	
//		String content = request.getParameter("findTag");
//		//String title = request.getParameter("title");
//	
//		BoardVO vo = new BoardVO();
//
//		vo = dao.read(content);		
//		request.setAttribute("vo", vo);
//		
//		return new WebView("/WEB-INF/board/tagList.jsp");
//	}
	public WebView tagList(HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		
		List<BoardVO> tagList = dao.tagList(request.getParameter("content"));
		
		request.setAttribute("tagList", tagList);
				
		return new WebView("/WEB-INF/board/tagList.jsp");
	}
	
	public WebView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		
		List<BoardVO> list = dao.list(Integer.parseInt(request.getParameter("pageno")));
		
		request.setAttribute("list", list);
				
		return new WebView("/WEB-INF/board/list.jsp");
	}
//
//	
//	public WebView regist(HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		System.out.println("bbb");
//		if(request.getMethod().equals("GET")){
//			
//			System.out.println("aaaa");
//			return new WebView("/WEB-INF/board/create.jsp");
//		}		
//		
//		BoardVO vo = new BoardVO();
//		vo.setBoardTitle(request.getParameter("boardTitle"));
//		vo.setBoardWriter(request.getParameter("boardWriter"));
//		vo.setBoardContent(request.getParameter("boardContent"));
//
//
//		System.out.println(vo);
//		dao.create(vo);
//		
//		System.out.println("ccc");
//		return new WebView("/WEB-INF/board/list.jsp");
//		
//	}
//	public WebView view(HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		
//	
//		int boardNo = Integer.parseInt(request.getParameter("boardNo"));
//		
//		BoardVO vo = new BoardVO();
//				
//		vo = dao.read(boardNo);
//		System.out.println(vo);
//		
//		request.setAttribute("vo", vo);
//		
//		return new WebView("/WEB-INF/board/view.jsp");
//	}
	

}
