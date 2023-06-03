package yaama;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
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
	
	public void addPost(Post p) {
		open();
		String sql_post = "";		//Post에 추가
		String sql_keyword = "";	//PostKeyword에 추가
		
		try{
			pstmt = conn.prepareStatement(sql_post);
			pstmt.setString(1, p.getPost_user().getUser_id());
			pstmt.setString(2,p.getPost_text());
			pstmt.setString(3, p.getPost_date());
			pstmt.setString(4, p.getPost_location());
			pstmt.setBoolean(5, p.isPost_accept());
			pstmt.setString(6, p.getPost_response());
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(sql_keyword);
			pstmt.setLong(1, p.getPost_id());
			
			for(Keyword k : p.getKeywords()) {
				pstmt.setString(2, k.getKeyword());
				pstmt.executeUpdate();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public void deletePost(long pid) {
		open();
		String sql_keyword = "";	// PostKeyword에서 keyword 목록 받아옴
		String sql_post = "";		// post 지움
		KeywordDAO kdao = new KeywordDAO();
		
		try	{
			pstmt = conn.prepareStatement(sql_keyword);
			pstmt.setLong(1, pid);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				kdao.deleteKeyword(rs.getString("kword"));
			}
			
			pstmt = conn.prepareStatement(sql_post);
			pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public Post getPost(long pid) {
		open();
		String sql = "";
		Post p = null;
		UserDAO udao = new UserDAO();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pid);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				p = new Post();
				p.setPost_id(pid);
				p.setPost_text(rs.getString(""));
				p.setPost_date(rs.getString(""));
				p.setPost_location(rs.getString(""));
				p.setPost_accept(rs.getBoolean(""));
				p.setPost_response(rs.getString(""));
				p.setKeywords(null);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return p;
	}
	
	public List<Post> getPostLog(String uid){
		open();
		String sql = "";
		List<Post> posts = new ArrayList();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uid);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				posts.add(this.getPost(rs.getInt("post_id")));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return posts;
	}
}
