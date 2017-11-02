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
import services.user.datastructs.SearchResult;
import services.user.datastructs.User;

/**
 * 
 * @author cb_mac
 *
 */
@AuthenticationRequiried
@WebServlet("/user/friends")
public class FriendsList extends HttpServlet {
	private static final long serialVersionUID = 134567898376543L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject answer = new JSONObject();
		
		String iduser = req.getParameter(ServicesTools.IDUSER_ARG);
		String page  =req.getParameter(ServicesTools.PAGE_ARG);
		String pageSize  =req.getParameter(ServicesTools.SIZE_ARG);
		
		List<User> friendsList=new ArrayList<>() ;
		try {
		friendsList = FriendManagement.listFriends(Integer.parseInt(iduser), 
					Integer.parseInt(page), Integer.parseInt(pageSize));
		} catch (NumberFormatException | CannotConnectToDatabaseException | QueryFailedException | SQLException e) {
			e.printStackTrace();
		}
		
		//here we build json answer !
		SearchResult  sr = new SearchResult(Integer.parseInt(page), Integer.parseInt(pageSize), friendsList);
		answer=sr.toJSONObject();
		// reponse en json 
		
		ServicesTools.addCORSHeader(resp);
		
		PrintWriter out = resp.getWriter();
		out.write(answer.toString());
		resp.setContentType("text/plain");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}
	

}
