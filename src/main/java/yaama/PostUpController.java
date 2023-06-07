package yaama;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

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
		PostDAO pdao = new PostDAO();
		BadWordFiltering bf = new BadWordFiltering();
		KeywordExtractor kext = new KeywordExtractor();
		p.setPost_user(request.getParameter("post_user"));
		p.setPost_text(request.getParameter("post_text"));
		p.setPost_location(request.getParameter("post_location"));	// location 정보가 단순 구만 표시한다는 가정 하에 개발함(대덕구, 중구 등등)
		
		if(!bf.check(p.getPost_text())) {
			KeywordDAO kdao = new KeywordDAO();
			List<String> keywords = kext.extract(p.getPost_text());
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
					kdao.insertKeyword(k);
					
					Map<String, Integer> lcnt = new HashMap<>(){{;
					put("유성구", 0);
					put("대덕구", 0);
					put("동구", 0);
					put("서구", 0);
					put("중구", 0);
					}};
					lcnt.put(p.getPost_location(), 1);
					
					Map<String, Integer> rcnt = new HashMap<>() {{;
					for(String rk : keywords) {
						if(rk != k) {
							put(rk, 1);
						}
					}
					}};
					kword = new Keyword();
					kword.setKeyword(k);
					kword.setLcnt(lcnt);
					kword.setRcnt(rcnt);
				}
				kword.setTotal_count();	// 굳이 필요하진 않음
				kwords.add(kword);
			}
			
			for(Keyword kword : kwords) {
				kdao.upsertKeyword(kword);
			}
			
			p.setKeywords(kwords);
			
		} else {
			p.setPost_accept(false);
			p.setPost_response("부적절한 언어 사용");
			p.setKeywords(new ArrayList<>());	// null or 빈 리스트
		}
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		p.setPost_date(formatter.format(date));
		
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
