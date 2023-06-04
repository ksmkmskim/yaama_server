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
	
	public void upsertKeyword(Keyword k) {
		open();
		String sql_keyword = "insert ignore into Keyword_table(kword) values(?)";
		String sql_loc = "insert into KeywordLoc_table(kloc, kword, lcnt) values(?, ?, ?) on duplicate key update lcnt=?";
		String sql_rel = "insert into KeywordRel_table(kword, rkword, rcnt) values(?, ?, ?) on duplicate key update rcnt=?";
		
		try {
			pstmt = conn.prepareStatement(sql_keyword);
			pstmt.setString(1, k.getKeyword());
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(sql_loc);
			pstmt.setString(2, k.getKeyword());
			for(String loc : k.getLcnt().keySet()) {
				pstmt.setString(1, loc);
				pstmt.setInt(3, k.getLcnt().get(loc));
				pstmt.setInt(4, k.getLcnt().get(loc));
				pstmt.executeUpdate();
			}
			
			pstmt = conn.prepareStatement(sql_rel);
			pstmt.setString(1, k.getKeyword());
			for(String rel : k.getRcnt().keySet()) {
				pstmt.setString(2, rel);
				pstmt.setInt(3, k.getLcnt().get(rel));
				pstmt.setInt(4, k.getLcnt().get(rel));
				pstmt.executeUpdate();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public void deleteKeyword(String kword) {
		open();
		String sql = "delete * from Keyword_table where kword=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, kword);
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public Keyword getKeyword(String kword) {
		open();
		String sql = "select * from Keyword_table where kword=?";
		Keyword k = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, kword);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				k = new Keyword();
				k.setKeyword(kword);
				k.setLcnt(this.getKeywordLoc(kword));
				k.setRcnt(this.getKeywordRel(kword));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return k;
	}
	
	private HashMap<String, Integer> getKeywordLoc(String kword){
		open();
		String sql = "select * from KeywordLoc_table where kword=?";
		HashMap<String, Integer> lcnt = new HashMap<>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, kword);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String loc = rs.getString("kloc");
				int cnt = rs.getInt("lcnt");
				lcnt.put(loc, cnt);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return lcnt;
	}
	
	private HashMap<String, Integer> getKeywordRel(String kword){
		open();
		String sql = "select * from KeywordRel_table where kword=?";
		HashMap<String, Integer> rcnt = new HashMap<>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, kword);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String rel = rs.getString("rkword");
				int cnt = rs.getInt("rcnt");
				rcnt.put(rel, cnt);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return rcnt;
	}
	
	public List<Keyword> getPostKeyword(long pid) {
		open();
		String sql = "select * from PostKeyword_table where pid=?";
		List<Keyword> pKeywords = new ArrayList<>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pid);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				pKeywords.add(this.getKeyword(rs.getString("kword")));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return pKeywords;
	}
	
	public List<Keyword> getAllKeyword() {
		open();
		String sql = "select * from PostKeyword_table";
		List<Keyword> pKeywords = new ArrayList<>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				pKeywords.add(this.getKeyword(rs.getString("kword")));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return pKeywords;
	}
}
