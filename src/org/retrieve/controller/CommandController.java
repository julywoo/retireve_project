package org.retrieve.controller;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import org.retrieve.member.MemberVO;
import org.retrieve.controller.WebView;
import org.retrieve.controller.BasicServlet;

public abstract class CommandController extends BasicServlet{
	
	public CommandController() {
		super();
	}

	@Override
	protected WebView doWork(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		//MemberVO vo = (MemberVO)session.getAttribute("vo");
		
		request.setCharacterEncoding("UTF-8");
		
		String method = request.getParameter("method");
		WebView result = null;
		
		
		Class clz = this.getClass();
		
		Method targetMethod = clz.getDeclaredMethod(method, HttpServletRequest.class, HttpServletResponse.class);
	
//		if((vo == null) && (!method.equals("login"))){
//			response.sendRedirect("/flower/reject.jsp");
//		}
//		
		result = (WebView)targetMethod.invoke(this, request, response);
				
		return result;
	}


}
