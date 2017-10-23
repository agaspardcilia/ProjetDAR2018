package services.test;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import annotations.AuthenticationRequiried;
import utils.managementpepet.ManagementPepet;

//@AuthenticationRequiried
@WebServlet("/test")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = -514345712229720561L;

	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ManagementPepet.removePepet(4, 170);
		String answer = "{\"Hello\" : \"world!\"}";
		PrintWriter out = resp.getWriter();
		out.write(answer.toString());
		resp.setContentType("text/plain");
	}
	
}

