package services.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.ServicesTools;
import services.errors.ServerErrors;
import services.user.FriendManagement;

@WebServlet("/event/last")
public class GetLastEvents extends HttpServlet{

	private static final long serialVersionUID = 14885478L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject answer = new JSONObject();

		try {
			int idcity= Integer.parseInt(req.getParameter(ServicesTools.IDCITY_ARG));

			if (!ServicesTools.nullChecker(idcity)) {	
				answer = EventUtils.getLastEvents(idcity);
			} else {
				answer = ServicesTools.createJSONError(ServerErrors.MISSING_ARGUMENT);
			}
		} catch (NumberFormatException e) {
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
