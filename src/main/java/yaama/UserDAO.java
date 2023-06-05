package yaama;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
	Connection conn = null;
	PreparedStatement pstmt;
	
	final String JDBC_DRIVER = "org.h2.Driver";
	final String JDBC_URL = "jdbc:h2:tcp://localhost/C:\\Users\\Administrator\\Desktop\\4-1\\capstone\\database\\yaama";
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
	
	public void addUser(User u) {
		open();
		String sql = "insert into User_table(uid, upw, uname, uaddr, utel, uimg) values(?, ?, ?, ?, ?, ?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, u.getUser_id());
			pstmt.setString(2, u.getUser_pw());
			pstmt.setString(3, u.getUser_name());
			pstmt.setString(4, u.getUser_addr());
			pstmt.setString(5, u.getUser_tel());
			pstmt.setString(6, u.getUser_profile_img().replace("\\", "/"));
			
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public void deleteUser(String uid) {
		open();
		String sql = "delete * from User_table where uid=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uid);
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public User getUser(String uid) {
		open();
		String sql_user = "select * from User_table where uid=?";
		User u = null;
		PostDAO pdao = new PostDAO();
		
		try {
			pstmt = conn.prepareStatement(sql_user);
			pstmt.setString(1, uid);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				u = new User();
				u.setUser_id(uid);
				u.setUser_pw(rs.getString("upw"));
				u.setUser_name(rs.getString("uname"));
				u.setUser_addr(rs.getString("uaddr"));
				u.setUser_tel(rs.getString("utel"));
				u.setUser_profile_img(rs.getString("uimg"));
				u.setPosts(pdao.getPostLog(uid));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return u;
	}
}
