package yaama;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class SignUpController
 */
@WebServlet("/SignUp")
public class SignUpController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public SignUpController() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/x-json; charset=UTF-8");
		User u = new User();
		UserDAO udao = new UserDAO();
		
		u.setUser_id(request.getParameter("user_id"));
		u.setUser_pw(request.getParameter("user_pw"));
		u.setUser_name(request.getParameter("user_name"));
		u.setUser_addr(request.getParameter("user_addr"));
		u.setUser_tel(request.getParameter("user_tel"));
		u.setUser_profile_img(null);		//프로필 사진 업로드 구현 보류
		
		udao.addUser(u);
		
		if(udao.getUser(u.getUser_id()) == null) {
			response.getWriter().write("회원가입 실패");
		} else {
			response.getWriter().write("회원가입 성공");
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
