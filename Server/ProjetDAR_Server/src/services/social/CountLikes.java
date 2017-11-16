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

@WebServlet("/social/like/count")
public class CountLikes extends HttpServlet {
	private static final long serialVersionUID = 4190981992129407673L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject answer = new JSONObject();
		
		String statusID = req.getParameter(SocialUtils.STATUS_ID_ARG);
		
		if (!ServicesTools.nullChecker(statusID)) {
			answer = SocialUtils.getLikeCount(statusID);
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

