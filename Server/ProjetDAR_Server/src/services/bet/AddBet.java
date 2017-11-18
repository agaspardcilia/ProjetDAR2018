package services.bet;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import database.exceptions.CannotConnectToDatabaseException;
import database.exceptions.QueryFailedException;
import services.ServicesTools;
import services.errors.ServerErrors;
import services.user.UserUtils;

@WebServlet("/bet/add")
public class AddBet extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 776500783079301638L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject answer = new JSONObject();
		
		
		try {
			int idUser = Integer.parseInt(req.getParameter(ServicesTools.IDUSER_ARG));
			int idEvent = Integer.parseInt(req.getParameter(ServicesTools.IDEVENT_ARG));
			double moneyBet = Double.parseDouble(req.getParameter(ServicesTools.MONEYBET_ARG));
			

			if (!ServicesTools.nullChecker(idUser, idEvent, moneyBet)) {
				answer = Bet.addBet(idUser, idEvent, moneyBet);
			} else {
				answer = ServicesTools.createJSONError(ServerErrors.MISSING_ARGUMENT);
			}
		} catch(NumberFormatException | CannotConnectToDatabaseException | QueryFailedException | SQLException e) {
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