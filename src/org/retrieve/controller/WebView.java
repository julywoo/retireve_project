package org.retrieve.controller;

public class WebView {

	private String nextPage;
	private boolean redirect;
	private String errorPage;
	
	public WebView(String nextPage){
		this(nextPage,false);
	}
	
	public WebView(String nextPage, boolean redirect){
		this(nextPage, false, "/WEB-INF/error.jsp");
	}

	public WebView(String nextPage, boolean redirect, String errorPage){
		
		this.nextPage = nextPage;
		this.redirect = redirect;
		this.errorPage = errorPage;
	}

	public String getNextPage() {
		return nextPage;
	}

	public boolean isRedirect() {
		return redirect;
	}

	public String getErrorPage() {
		return errorPage;
	}

	@Override
	public String toString() {
		return "WebView [nextPage=" + nextPage + ", redirect=" + redirect
				+ ", errorPage=" + errorPage + "]";
	}
	
	
}
