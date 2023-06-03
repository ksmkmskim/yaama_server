package yaama;

import java.util.HashMap;

public class Keyword {
	String keyword;
	HashMap<String, Integer> lcnt = new HashMap();	// location : count 딕셔너리
	HashMap<String, Integer> rcnt = new HashMap();	// rkeyword : count 딕셔너리
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public HashMap<String, Integer> getLcnt() {
		return lcnt;
	}
	public void setLcnt(HashMap<String, Integer> lcnt) {
		this.lcnt = lcnt;
	}
	public HashMap<String, Integer> getRcnt() {
		return rcnt;
	}
	public void setRcnt(HashMap<String, Integer> rcnt) {
		this.rcnt = rcnt;
	}
}
