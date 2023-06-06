package yaama;

import java.util.ArrayList;
import java.util.List;

public class Post {
	private long post_id;
	private String post_user;
	private String post_text;
	private String post_date;
	private String post_location;
	private boolean post_accept;
	private String post_response;
	private List<Keyword> keywords;
	
	public long getPost_id() {
		return post_id;
	}
	public void setPost_id(long post_id) {
		this.post_id = post_id;
	}
	public String getPost_user() {
		return post_user;
	}
	public void setPost_user(String post_user) {
		this.post_user = post_user;
	}
	public String getPost_text() {
		return post_text;
	}
	public void setPost_text(String post_text) {
		this.post_text = post_text;
	}
	public String getPost_date() {
		return post_date;
	}
	public void setPost_date(String post_date) {
		this.post_date = post_date;
	}
	public String getPost_location() {
		return post_location;
	}
	public void setPost_location(String post_location) {
		this.post_location = post_location;
	}
	public boolean isPost_accept() {
		return post_accept;
	}
	public void setPost_accept(boolean post_accept) {
		this.post_accept = post_accept;
	}
	public String getPost_response() {
		return post_response;
	}
	public void setPost_response(String post_response) {
		this.post_response = post_response;
	}
	public List<Keyword> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}
}
