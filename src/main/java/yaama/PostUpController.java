package yaama;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import yaama.badwords.BadWordFiltering;
/**
 * Servlet implementation class PostUpController
 */
@WebServlet("/PostUp")
public class PostUpController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostUpController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/x-json; charset=UTF-8");
		ObjectMapper om = new ObjectMapper();
		Post p = new Post();
		UserDAO udao = new UserDAO();
		PostDAO pdao = new PostDAO();
		KeywordExtractor ke = new KeywordExtractor();
		BadWordFiltering bf = new BadWordFiltering();
		
		p.setPost_id(Integer.parseInt(request.getParameter("post_id")));
		p.setPost_user(udao.getUser(request.getParameter("post_user")));
		p.setPost_text(request.getParameter("post_text"));
		p.setPost_date(request.getParameter("post_date"));
		p.setPost_location(request.getParameter("post_location"));	// location 정보가 단순 구만 표시한다는 가정 하에 개발함(대덕구, 중구 등등)
		
		if(!bf.check(p.getPost_text())) {
			KeywordDAO kdao = new KeywordDAO();
			List<String> keywords = ke.extract(p.getPost_text());
			List<Keyword> kwords = new ArrayList<>();
			
			p.setPost_accept(true);
			p.setPost_response(String.join(", ", keywords));
			
			for(String k : keywords) {
				Keyword kword = kdao.getKeyword(k);
				if(kword != null) {
					kword.getLcnt().put(p.getPost_location(), kword.getLcnt().get(p.getPost_location()) + 1);
					for(String rk : keywords) {
						if(rk != k) {
							kword.getRcnt().put(rk, kword.getRcnt().get(rk) != null ? kword.getRcnt().get(rk) + 1 : 1);
						}
					}
				} else {
					HashMap<String, Integer> lcnt = new HashMap();
					lcnt.put("유성구", 0);
					lcnt.put("대덕구", 0);
					lcnt.put("동구", 0);
					lcnt.put("서구", 0);
					lcnt.put("중구", 0);
					lcnt.put(p.getPost_location(), 1);
					
					HashMap<String, Integer> rcnt = new HashMap();
					for(String rk : keywords) {
						if(rk != k) {
							rcnt.put(rk, 1);
						}
					}
					
					kword = new Keyword();
					kword.setKeyword(k);
					kword.setLcnt(lcnt);
					kword.setRcnt(rcnt);
				}
				kword.setTotal_count();	// 굳이 필요하진 않음
				kwords.add(kword);
				kdao.upsertKeyword(kword);
			}
			
			p.setKeywords(kwords);
			
		} else {
			p.setPost_accept(false);
			p.setPost_response("부적절한 언어 사용");
			p.setKeywords(null);	// null or 빈 리스트
		}
		String result = om.writeValueAsString(p);
		response.getWriter().write(result);
		
		pdao.addPost(p);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
