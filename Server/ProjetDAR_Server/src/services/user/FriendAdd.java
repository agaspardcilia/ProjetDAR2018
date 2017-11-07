package services.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.ServicesTools;
import services.errors.ServerErrors;

public class FriendAdd extends HttpServlet{

	private static final long serialVersionUID = 134567854345L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		JSONObject answer = new JSONObject();
		
		try {
			int iduser = Integer.parseInt(req.getParameter(ServicesTools.IDUSER_ARG));
			int idfriend = Integer.parseInt(req.getParameter(ServicesTools.IDFRIEND_ARG));
			int page = Integer.parseInt(req.getParameter(ServicesTools.PAGE_ARG));
			int pageSize = Integer.parseInt(req.getParameter(ServicesTools.SIZE_ARG));
			if (page < 0 || pageSize <= 0) {
				answer = ServicesTools.createJSONError(ServerErrors.BAD_ARGUMENT);
			} else {
				if (!ServicesTools.nullChecker(iduser,idfriend)) {	
					answer = FriendManagement.addFriendJSON(iduser, idfriend);
				} else {
					answer = ServicesTools.createJSONError(ServerErrors.MISSING_ARGUMENT);
				}
			}
		} catch (NumberFormatException e) {
			answer = ServicesTools.createJSONError(ServerErrors.BAD_ARGUMENT);
		}
		PrintWriter out = resp.getWriter();
		out.write(answer.toString());
		resp.setContentType("text/plain");
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

	
}
