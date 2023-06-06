package yaama;

import java.util.HashMap;
import java.util.Map;

public class Keyword {
	private String keyword;
	private int total_count;
	private Map<String, Integer> lcnt;	// location : count 딕셔너리
	private Map<String, Integer> rcnt;	// rkeyword : count 딕셔너리
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count() {		// lcnt 총합이 저장되게 수정
		int total = 0;
		for(int val : lcnt.values()) {
			total += val;
		}
		this.total_count = total;
	}
	public Map<String, Integer> getLcnt() {
		return lcnt;
	}
	public void setLcnt(Map<String, Integer> lcnt) {
		this.lcnt = lcnt;
	}
	public Map<String, Integer> getRcnt() {
		return rcnt;
	}
	public void setRcnt(Map<String, Integer> rcnt) {
		this.rcnt = rcnt;
	}
}
