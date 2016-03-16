package org.retrieve.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.retrieve.controller.WebView;

public abstract class BasicServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		WebView result = null;

		try{
			
			result = doWork(request,response);
			
						if(result.isRedirect()){
					response.sendRedirect(result.getNextPage());
				
			}else{
				RequestDispatcher dispatcher = request.getRequestDispatcher(result.getNextPage());
				dispatcher.forward(request, response);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected abstract WebView doWork(HttpServletRequest request,
			HttpServletResponse response) throws Exception;

}
