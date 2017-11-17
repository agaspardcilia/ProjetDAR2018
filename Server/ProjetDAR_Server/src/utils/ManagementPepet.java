package utils;

import java.sql.ResultSet;

import org.json.JSONObject;

import database.DBMapper;
import database.DBMapper.QueryType;
import services.ServicesTools;
import services.auth.Authentication;

public class ManagementPepet {
	private final static String QUERY_PEPET = "SELECT users.pepet FROM users WHERE idusers = ?";
	private final static String QUERY_PEPET_ADD = "INSERT INTO users ('pepet') VALUES ('?') WHERE idusers = ?";


	public static JSONObject addPepet(String key, double amout){
		JSONObject answer;
		double pepet;
		try{
			if(Authentication.validateAndRefreshKey(key)){
				ResultSet rs = DBMapper.executeQuery(QUERY_PEPET, QueryType.SELECT,'%', key);
				if(rs.next()){
					pepet = rs.getInt("pepet");
					DBMapper.executeQuery(QUERY_PEPET_ADD, QueryType.UPDATE, pepet+amout, key);
					answer = ServicesTools.createPositiveAnswer();
				}
				
				else
					answer = ServicesTools.createInvalidKeyError();
			}
			else
				answer = ServicesTools.createInvalidKeyError();	
		}
		catch (Exception e) { //TODO
			answer = ServicesTools.createDatabaseError(e);
		}
		return answer;

	}
	
	public static JSONObject removePepet(String key, int amout, String username){
		JSONObject answer;
		int pepet;
		try{
			if(Authentication.validateAndRefreshKey(key)){
				ResultSet rs = DBMapper.executeQuery(QUERY_PEPET, QueryType.SELECT,'%', username);
				if(rs.next()){
					pepet = rs.getInt("pepet");
					//TODO : appel ) à la dataBase pour retirer des sous
					answer = ServicesTools.createInvalidKeyError();

				}
				
				else
					answer = ServicesTools.createInvalidKeyError();
			}
			else
				answer = ServicesTools.createInvalidKeyError();	
		}
		catch (Exception e) { //TODO
			answer = ServicesTools.createDatabaseError(e);
		}
		return answer;

	}
}