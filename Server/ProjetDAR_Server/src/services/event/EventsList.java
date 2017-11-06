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
/**
 * 
 * @author cb_mac
 *
 */
@WebServlet("/event/list")
public class EventsList extends HttpServlet{

	private static final long serialVersionUID = 14885478L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject answer = new JSONObject();
		
		try {
			int idcity= Integer.parseInt(req.getParameter(ServicesTools.IDCITY_ARG));
			Date date =  new Date(Long.parseLong(req.getParameter(ServicesTools.DATE_ARG)));
			int eventtype= Integer.parseInt(req.getParameter(ServicesTools.EVENTTYPE_ARG));
			
			int page = Integer.parseInt(req.getParameter(ServicesTools.PAGE_ARG));
			int pageSize = Integer.parseInt(req.getParameter(ServicesTools.SIZE_ARG));
			
			if (page < 0 || pageSize <= 0) {
				answer = ServicesTools.createJSONError(ServerErrors.BAD_ARGUMENT);
			} else {
				if (!ServicesTools.nullChecker(idcity,date,eventtype, page, pageSize)) {	
					answer = EventUtils.getEventsListJSON(idcity, date, eventtype, page, pageSize);
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
		this.doGet(req, resp);
	}
	
	
}
