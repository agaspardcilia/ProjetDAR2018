package services.bet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.ServicesTools;
import services.errors.ServerErrors;

@WebServlet("/social/bet/add")
public class AddBet extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 776500783079301638L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject answer = new JSONObject();
		
		
		try {
			int idUser = Integer.parseInt(req.getParameter(ServicesTools.USER_ARG));
			int idEvent = Integer.parseInt(req.getParameter(ServicesTools.IDEVENT_ARG));
			int odd = Integer.parseInt(req.getParameter(ServicesTools.ODD_ARG));
			int moneyBet = Integer.parseInt(req.getParameter(ServicesTools.MONEYBET_ARG));
			Date date = new Date(req.getParameter(ServicesTools.DATE_ARG));
			

			if (!ServicesTools.nullChecker(idUser, idEvent, odd, moneyBet, date)) {
				answer = Bet.addBet(idUser, idEvent, odd, moneyBet, date);
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
