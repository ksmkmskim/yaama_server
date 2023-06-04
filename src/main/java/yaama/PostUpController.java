package yaama;

import java.io.IOException;
import java.util.List;

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
		p.setPost_location(request.getParameter("post_location"));
		
		if(!bf.check(p.getPost_text())) {
			KeywordDAO kdao = new KeywordDAO();
			p.setPost_accept(true);
			List<String> keywords = ke.extract(p.getPost_text());
			
			for(String k : keywords) {
				Keyword kword = kdao.getKeyword(k);
				if(kword != null) {
					kword.getLcnt();
					kword.getRcnt();
				} else {
					kword = new Keyword();
					kword.setKeyword(k);
					kword.setLcnt(null);
					kword.setRcnt(null);
				}
			}
		} else {
			p.setPost_accept(false);
			p.setPost_response("부적절한 언어 사용");
			p.setKeywords(null);	// null or 빈 리스트
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
