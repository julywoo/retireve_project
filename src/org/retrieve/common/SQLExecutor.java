package org.retrieve.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class SQLExecutor {
	
	private static final String JDBC_URL= "jdbc:apache:commons:dbcp:/pool";
	
	static{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("complete");

			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			System.out.println("connection");
		}catch  (ClassNotFoundException e) {
			System.out.println("error");
			e.printStackTrace();
			
		}
	}
	
	protected Connection con;
	protected PreparedStatement pstmt;
	protected PreparedStatement pstmt1;
	protected ResultSet rs;
	protected ResultSet rs1;
	
	public final void execute() throws Exception{
		try{
			
			System.out.println("makeConnection");
			makeConnection();
			System.out.println("doJob");
			doJob();
		}catch(Exception e){
			throw e;
		}finally{
			closeAll();
		}
	}

	private void makeConnection()throws Exception{
		
		con = DriverManager.getConnection(JDBC_URL);		
	}
	
	protected abstract void doJob()throws Exception;
	
	private void closeAll(){

		if(rs != null){try{	rs.close(); }catch(Exception e){ }	}			
		if(pstmt != null){try{ pstmt.close(); }catch(Exception e){ }	}
		if(con != null){try{ con.close(); }catch(Exception e){ }	}		
	}

}
