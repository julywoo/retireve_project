<%@page import="org.retrieve.board.BoardVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%@ page import="org.retrieve.*"%>
<%@ page import="java.util.*"%>

<%
	request.setCharacterEncoding("UTF-8");

	List<BoardVO> list=(List<BoardVO>)request.getAttribute("list");
	BoardVO vo1 =(BoardVO)request.getAttribute("vo");
	
 %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">

<!-- 부가적인 테마 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">

<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</head>
<style>
.userIcon{height: 50px; width: 50px
}
</style>
<body>
<div><img src="/retrieve/play/img.png" width="1300px"></div>
<div style="width: 800px;padding-left: 20%;padding-top: 5px;" >
<div >
<div style="float: left"><img alt="" src="/retrieve/play/1.png" style="width: 50px;height: 50px"></div>
<div style="padding-left: 10%" > <span><a href="#">코미디 빅리그</a></span><br><span>12월 8일 오전 9:00 ·</span></div>
</div>
<video src="/retrieve/play/video.mp4" controls autoplay loop muted preload="auto" width="100%" height="300px"></video>
<br>
<span>좋아요</span> <span id="reply">댓글달기</span> <span id="find">태그찾기</span>
<hr>
<div >
<form method="post" action="/retrieve/board?method=regist"  id="replySection" style="display: none;">
				<span><img class="userIcon" src="/retrieve/play/man.png"></span>
				<span><input type="text" style="width: 60%;height: 40px" class="" placeholder="댓글을 입력하세요" name="content"/></span>
				<input type="submit"class="btn btn-default" value="입력">
				
				<hr></form>
				
				
				<form method="post" action="/retrieve/board?method=tagList" id="findSection" style="display: none;">
				<img class="userIcon" src="/retrieve/play/man.png">
				<input type="text" style="width: 60%;height: 40px" class="" name="content" placeholder="검색 항목을 입력하세요 "/>
				<input type="submit"class="btn btn-default" value="검색">
				<hr>
				</form>
				</div>
				
<table width='100%' class="table table-striped">
					

					<%
						for (int i = 0; i < list.size(); i++) {
							BoardVO vo = list.get(i);
					%>
					<tr>
						<% if(i%2==0){ %>
						<td width="10%"><img class="userIcon" src="/retrieve/play/man.png"></td>
						<%} else{%>
						<td width="10%"><img class="userIcon" src="/retrieve/play/woman.png"></td>
						<%} %>
						<td width="20%"><%=vo.getWriter() %></td>
						<td width="50%"><%=vo.getContent() %></td>
						<td>좋아요 <%=vo.getHit() %></td>
					</tr>
					<%
						}
					%>
				
				</table>
				<br>
				<a href="#">댓글 더보기</a>
</div>		
</body>
<script src="js/jquery.js"></script>
<script type="text/javascript">
$("#reply").click(function name() {
	$("#replySection").css("display","block");
	$("#findSection").css("display","none");
});
$("#find").click(function name() {
	$("#findSection").css("display","block");
	$("#replySection").css("display","none");
});
	

</script>
</html>