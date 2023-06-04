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
		String sql_post = "insert into Post_table(uid, ptext, pdate, ploc, pacpt, pres) values(?, ?, ?, ?, ?, ?)";		//Post에 추가
		String sql_keyword = "insert into PostKeyword_table(pid, kword) values(LAST_INSERT_ID(), ?)";	//PostKeyword에 추가
		
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
			for(Keyword k : p.getKeywords()) {
				pstmt.setString(1, k.getKeyword());
				pstmt.executeUpdate();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public void deletePost(Post p) {
		open();
		String sql = "delete * from Post_table where pid=?";		// post 지움
		
		try	{
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, p.getPost_id());
			pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public Post getPost(long pid) {
		open();
		String sql = "select * from Post_table where pid=?";
		Post p = null;
		UserDAO udao = new UserDAO();
		KeywordDAO kdao = new KeywordDAO();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pid);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				p = new Post();
				p.setPost_id(pid);
				p.setPost_user(udao.getUser(rs.getString("uid")));
				p.setPost_text(rs.getString("ptext"));
				p.setPost_date(rs.getString("pdate"));
				p.setPost_location(rs.getString("ploc"));
				p.setPost_accept(rs.getBoolean("pacpt"));
				p.setPost_response(rs.getString("pres"));
				p.setKeywords(kdao.getPostKeyword(pid));
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
		String sql = "select * from Post_table where uid=?";
		List<Post> posts = new ArrayList<>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uid);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				posts.add(this.getPost(rs.getLong("pid")));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return posts;
	}
}
