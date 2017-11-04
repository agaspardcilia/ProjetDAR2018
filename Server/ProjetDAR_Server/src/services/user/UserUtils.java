package services.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import database.DBMapper;
import database.DBMapper.QueryType;
import database.exceptions.CannotConnectToDatabaseException;
import database.exceptions.QueryFailedException;
import services.ServicesTools;
import services.auth.Authentication;
import services.datastructs.SearchResult;
import services.user.datastructs.User;

public class UserUtils {
	private final static String QUERY_SEARCH_USER = "SELECT * FROM users WHERE username LIKE ? ORDER BY idusers LIMIT ? OFFSET ?;";
	
	/**
	 * Looks for someone in user database.
	 * @param key Authentication key.
	 * @param query Search query (= username you're looking for).
	 * @param page Page number (>= 0).
	 * @param pageSize How many results per page (> 0).
	 */
	public static JSONObject search(String query, int page, int pageSize) {
		JSONObject answer;

		try {
			List<User> queryResult = getUserListFromQuery(query, page, pageSize);
			SearchResult sr = new SearchResult(page, pageSize, queryResult);

			answer = ServicesTools.createPositiveAnswer();
			ServicesTools.addToPayload(answer, "searchResult", sr);
		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException e) {
			answer = ServicesTools.createDatabaseError(e);
		}

		return answer;
	}

	public static JSONObject getUserByID(int idUser) {
		JSONObject result;
		
		try {
			
			User u = Authentication.getUserFromId(idUser);
			result = ServicesTools.createPositiveAnswer();
			
			ServicesTools.addToPayload(result, ServicesTools.USER_ARG, u);
			
		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException e) {
			result = ServicesTools.createDatabaseError(e);
		}
		
		return result;
	}

	/**
	 * Search
	 */
	private static List<User> getUserListFromQuery(String query, int page, int pageSize) throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		List<User> result = new ArrayList<>();

		ResultSet rs = DBMapper.executeQuery(QUERY_SEARCH_USER, QueryType.SELECT, '%' + query + '%', pageSize, getOffset(page, pageSize));

		while (rs.next()) {
			result.add(new User(rs.getInt("idusers"), rs.getString("username"), rs.getString("email")));
		}

		return result;
	}
	
	
	
	private static int getOffset(int pageNumber, int pageSize) {
		return pageNumber * pageSize;
	}
}
