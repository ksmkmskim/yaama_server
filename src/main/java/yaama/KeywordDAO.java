package yaama;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class KeywordDAO {
	Connection conn = null;
	PreparedStatement pstmt;
	
	final String JDBC_DRIVER = "org.h2.Driver";
	final String JDBC_URL = "";
	final String JDBC_USER = "admin";
	final String JDBC_PASSWD = "admin";
	
	public void open() {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWD);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			pstmt.close();
			conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addKeyword(Keyword k) {		
		open();
		String sql_keyword = "";	// keyword가 먼저 있는지 확인해야 함
		String sql_LKeyword = "";
		String sql_RKeyword = "";
		
		try {
			if(true){// keyword가 없는 경우
				pstmt = conn.prepareStatement(sql_keyword);
				pstmt.setString(1, k.getKeyword());
				pstmt.executeUpdate();
				
				pstmt = conn.prepareStatement(sql_LKeyword);
				
				for(String loc : k.getLcnt().keySet()) {
					if(k.getLcnt().get(loc) != 0)
					{
						pstmt.setString(1,loc);
						pstmt.setInt(2, 1);
						pstmt.setString(3, k.getKeyword());
					}
				}
				
				pstmt = conn.prepareStatement(sql_RKeyword);
				
				for(String rel : k.getRcnt().keySet()) {
					pstmt.setString(1,k.getKeyword());
					pstmt.setString(2, rel);
					pstmt.setInt(3, 1);
				}
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public Keyword getKeyword(int pid) {
		
	}
	
	public List<Keyword> getAllKeyword() {
		
	}
}
