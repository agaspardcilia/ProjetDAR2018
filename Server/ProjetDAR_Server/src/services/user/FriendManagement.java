package services.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import database.DBMapper;
import database.DBMapper.QueryType;
import database.exceptions.CannotConnectToDatabaseException;
import database.exceptions.QueryFailedException;
import services.ServicesTools;
import services.auth.Authentication;
import services.datastructs.SearchResult;
import services.user.datastructs.User;

/**
 * 
 * @author cb_mac
 *
 */
public class FriendManagement {

	//Friend list query
	private final static String QUERY_ADD_FRIEND = "INSERT INTO friends (idfriends,idusers) VALUES (?, ?);";
	private final static String QUERY_LIST_FRIEND = "SELECT u.username FROM users u ,friends f WHERE f.idfriends=u.idusers "
			+ "AND u.idusers=? ORDER BY u.idusers LIMIT ? OFFSET ?;";
	private final static String QUERY_REMOVE_FRIEND = "DELETE FROM friends WHERE idfriends=? OR idusers=?;";

	public static Boolean addFriend(int id , int idfriend) throws 
	CannotConnectToDatabaseException, QueryFailedException, SQLException {

		if(!Authentication.doesHeExists(id) || Authentication.doesHeExists(idfriend))
			return false;
		DBMapper.executeQuery(QUERY_ADD_FRIEND, QueryType.INSERT, id,idfriend);
		return true;
	}


	public static JSONObject addFriendJSON(int iduser, int idfriend) {
		JSONObject answer = new JSONObject();
		try {
			Boolean rep = addFriend(iduser, idfriend);
			if(rep) 
				answer = ServicesTools.createPositiveAnswer();

		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException e) {
			answer = ServicesTools.createDatabaseError(e);
		}
		return answer;

	}
	
	public static JSONObject deleteFriendJSON(int iduser, int idfriend) {
		JSONObject answer = new JSONObject();
		try {
			Boolean rep = deleteFriend(iduser, idfriend);
			if(rep) 
				answer = ServicesTools.createPositiveAnswer();

		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException e) {
			answer = ServicesTools.createDatabaseError(e);
		}
		return answer;

	}

	public static Boolean deleteFriend(int iduser, int idfriend) throws 
	CannotConnectToDatabaseException, QueryFailedException, SQLException {

		if(!Authentication.doesHeExists(iduser) || Authentication.doesHeExists(idfriend))
			return false;
		DBMapper.executeQuery(QUERY_REMOVE_FRIEND, QueryType.DELETE,iduser,idfriend);
		return true ;
	}

	public static List<User> getListFriends(int iduser,int page , int pageSize) throws
	CannotConnectToDatabaseException, QueryFailedException, SQLException{

		List<User> friends= new ArrayList<>();
		if(!Authentication.doesHeExists(iduser))
			return friends;

		ResultSet rs = DBMapper.executeQuery(QUERY_LIST_FRIEND, QueryType.SELECT, iduser,pageSize, pageSize*page);
		while (rs.next()) {
			friends.add(new User(rs.getInt("idusers"), rs.getString("username"), rs.getString("email")));
		}
		return friends ;
	}

	public static JSONObject getListFriendsJSON(int iduser, int page, int pageSize) {
		JSONObject answer= new JSONObject();
		try {
			List<User> friends = getListFriends(iduser, page, pageSize);
			SearchResult sr = new SearchResult(page, pageSize, friends);

			answer = ServicesTools.createPositiveAnswer();
			ServicesTools.addToPayload(answer, "searchResult", sr);
		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException e) {
			answer = ServicesTools.createDatabaseError(e);
		}
		return answer;
	}

}
