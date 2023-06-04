package yaama;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Servlet implementation class PostDeleteController
 */
@WebServlet("/PostDelete")
public class PostDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostDeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/x-json; charset=UTF-8");
		PostDAO pdao = new PostDAO();
		KeywordDAO kdao = new KeywordDAO();
		
		Post p = pdao.getPost(Integer.parseInt(request.getParameter("post_id")));
		
		List<Keyword> kwords = kdao.getPostKeyword(Integer.parseInt(request.getParameter("post_id")));
		for(Keyword k : kwords) {
			k.getLcnt().put(p.getPost_location(), k.getLcnt().get(p.getPost_location())-1);
			k.setTotal_count();
			if(k.getTotal_count() == 0) {
				kdao.deleteKeyword(k.getKeyword());
			} else {
				for(Keyword rk : kwords) {
					if(rk.getKeyword() != k.getKeyword()) {
						k.getRcnt().put(rk.getKeyword(), k.getRcnt().get(rk.getKeyword())-1);
					}
				}
			}
		}
		
		pdao.deletePost(p.getPost_id());
		response.getWriter().write("다음 게시글 삭제완료.\n" + p.getPost_text());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
