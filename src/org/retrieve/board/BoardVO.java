package org.retrieve.board;

import java.sql.Time;

public class BoardVO {
	
	private int boardNo;
	private String writer;
	private String Content;
	private String writerImg;
	private int hit;
	private Time date;
	private String stopResult;
	private String stemmerList;
	
	
	public String getStemmerList() {
		return stemmerList;
	}
	public void setStemmerList(String stemmerList) {
		this.stemmerList = stemmerList;
	}
	public String getStopResult() {
		return stopResult;
	}
	public void setStopResult(String stopResult) {
		this.stopResult = stopResult;
	}
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getWriterImg() {
		return writerImg;
	}
	public void setWriterImg(String writerImg) {
		this.writerImg = writerImg;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public Time getDate() {
		return date;
	}
	public void setDate(Time date) {
		this.date = date;
	}

	
	@Override
	public String toString() {
		return "BoardVO [boardNo=" + boardNo + ", writer=" + writer
				+ ", Content=" + Content + ", writerImg=" + writerImg
				+ ", hit=" + hit + ", date=" + date + ", stopResult="
				+ stopResult + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + boardNo;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoardVO other = (BoardVO) obj;
		if (boardNo != other.boardNo)
			return false;
		return true;
	}
	
	

}
