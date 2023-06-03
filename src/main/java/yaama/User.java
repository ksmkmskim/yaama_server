package yaama;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String user_id;
	private String user_pw;
	private String user_name;
	private String user_addr;
	private String user_tel;
	private String user_profile_img;
	private List<Post> posts;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_pw() {
		return user_pw;
	}
	public void setUser_pw(String user_pw) {
		this.user_pw = user_pw;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_addr() {
		return user_addr;
	}
	public void setUser_addr(String user_addr) {
		this.user_addr = user_addr;
	}
	public String getUser_tel() {
		return user_tel;
	}
	public void setUser_tel(String user_tel) {
		this.user_tel = user_tel;
	}
	public String getUser_profile_img() {
		return user_profile_img;
	}
	public void setUser_profile_img(String user_profile_img) {
		this.user_profile_img = user_profile_img;
	}
	public List<Post> getPosts() {
		return posts;
	}
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
}
