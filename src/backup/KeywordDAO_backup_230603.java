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
		String sql_key = "";
		String sql_loc = "";	// KeywordLoc 테이블에 kloc kword lcnt 추가
		
		try {
			pstmt = conn.prepareStatement(sql_key);
			pstmt.setString(1, k.getKeyword());
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(sql_loc);
			pstmt.setString(2, k.getKeyword());
			for(String loc : k.getLcnt().keySet()) {
				pstmt.setString(1, loc);
				pstmt.setInt(3, k.getLcnt().get(loc));
				pstmt.executeUpdate();
			}
			
			this.addRkword(k);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	private void addRkword(Keyword k) {
		open();
		String sql = "";	//insert
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, k.getKeyword());
			for(String rel : k.getRcnt().keySet()) {
				pstmt.setString(2, rel);
				pstmt.setInt(3, k.getRcnt().get(rel));
				pstmt.executeUpdate();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	private void updateRkword(Keyword k) {
		open();
		String sql = "";	// update
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, k.getKeyword());
			for(String rel : k.getRcnt().keySet()) {
				pstmt.setString(2, rel);
				pstmt.setInt(3, k.getRcnt().get(rel));
				pstmt.executeUpdate();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public void updateKeyword(Keyword k) {
		open();
		String sql_loc = "";
		String sql_rel_chk = "";	// rel 존재 확인
		
		try {
			pstmt = conn.prepareStatement(sql_loc);
			pstmt.setString(2, k.getKeyword());
			for(String loc : k.getLcnt().keySet()) {
				pstmt.setString(1, loc);
				pstmt.setInt(3, k.getLcnt().get(loc));
				pstmt.executeUpdate();
			}
			
			pstmt = conn.prepareStatement(sql_rel_chk);
			pstmt.setString(1, k.getKeyword());
			for(String rel : k.getRcnt().keySet()) {
				pstmt.setString(2, rel);
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()) {
					this.updateRkword(k);
				} else {
					this.addRkword(k);
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public void deleteKeyword(Keyword k) {
		open();
		
		try {
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public Keyword getKeyword(String kid) {
		open();
		String sql = "";
		Keyword k = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, kid);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				k = new Keyword();
				k.setKeyword(kid);
				k.setLcnt();
				k.setRcnt();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return k;
	}
	
	public Keyword getPostKeyword(long pid) {
		open();
		String sql = "";
		Keyword k = null;
		try {
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return k;
	}
	
	public List<Keyword> getAllKeyword() {
		open();
		
		try {
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
}
