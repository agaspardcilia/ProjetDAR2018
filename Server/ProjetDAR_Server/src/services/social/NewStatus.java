package services.social;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import annotations.AuthenticationRequiried;
import services.ServicesTools;
import services.errors.ServerErrors;

@AuthenticationRequiried
@WebServlet("/social/status/new")
public class NewStatus extends HttpServlet {
	private static final long serialVersionUID = 5063305421692382312L;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject answer = new JSONObject();
		
		String key = req.getParameter(ServicesTools.KEY_ARG);
		String content = req.getParameter(SocialUtils.CONTENT_ARG);
		
		if (!ServicesTools.nullChecker(key, content)) {
			answer = SocialUtils.addStatus(key, content);
		} else {
			answer = ServicesTools.createJSONError(ServerErrors.MISSING_ARGUMENT);
		}
		
		ServicesTools.addCORSHeader(resp);
		
		PrintWriter out = resp.getWriter();
		out.write(answer.toString());
		resp.setContentType("text/plain");
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
