package services.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import annotations.AuthenticationRequiried;
import database.exceptions.CannotConnectToDatabaseException;
import database.exceptions.QueryFailedException;
import services.ServicesTools;
import services.datastructs.SearchResult;
import services.errors.ServerErrors;
import services.user.datastructs.User;

/**
 * 
 * @author cb_mac
 *
 */
@WebServlet("/user/friends/list")
public class FriendsList extends HttpServlet {
	private static final long serialVersionUID = 134567898376543L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject answer = new JSONObject();
		
		try {
			int iduser = Integer.parseInt(req.getParameter(ServicesTools.IDUSER_ARG));
			int page = Integer.parseInt(req.getParameter(ServicesTools.PAGE_ARG));
			int pageSize = Integer.parseInt(req.getParameter(ServicesTools.SIZE_ARG));
			
			if (page < 0 || pageSize <= 0) {
				answer = ServicesTools.createJSONError(ServerErrors.BAD_ARGUMENT);
			} else {
				if (!ServicesTools.nullChecker(iduser, page, pageSize)) {	
					answer = FriendManagement.getListFriendsJSON(iduser, page, pageSize);
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
		this.doGet(req,resp);
	}
	

}
