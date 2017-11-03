package services.diag;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet("/diag/nosql")
public class CheckNoSQLDatabase extends HttpServlet{
	private static final long serialVersionUID = -8990107765777433099L;
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject answer = new JSONObject();

		answer = DiagUtils.checkNoSQL();

		PrintWriter out = resp.getWriter();
		out.write(answer.toString());
		resp.setContentType("text/plain");

	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
}
