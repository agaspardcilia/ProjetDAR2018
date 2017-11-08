package services.social;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.ServicesTools;
import services.errors.ServerErrors;

@WebServlet("/social/status/comments")
public class GetStatusComments extends HttpServlet {
	private static final long serialVersionUID = 7345769660359653936L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject answer = new JSONObject();
		
		try {
			String idStatus = req.getParameter(SocialUtils.STATUS_ID_ARG);
			int page = Integer.parseInt(req.getParameter(ServicesTools.PAGE_ARG));
			int size = Integer.parseInt(req.getParameter(ServicesTools.SIZE_ARG));
			if (!ServicesTools.nullChecker(idStatus, size, page)) {
				answer = SocialUtils.getStatusComments(idStatus, page, size);
			} else {
				answer = ServicesTools.createJSONError(ServerErrors.MISSING_ARGUMENT);
			}
		} catch(NumberFormatException e) {
			answer = ServicesTools.createJSONError(ServerErrors.BAD_ARGUMENT);
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
