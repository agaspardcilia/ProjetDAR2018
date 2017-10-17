package utils.managementpepet;

import java.sql.ResultSet;

import org.json.JSONObject;

import database.DBMapper;
import database.DBMapper.QueryType;
import services.ServicesTools;
import services.auth.Authentication;
import utils.managementpepet.error.ExceptionPepetValue;

public class ManagementPepet {
	private final static String QUERY_PEPET = "SELECT users.pepet FROM users WHERE idusers = ?";
	private final static String QUERY_PEPET_ADD = "INSERT INTO users ('pepet') VALUES ('?') WHERE idusers = ?";


	public static void addPepet(int idUsers, int amount){
		int pepet;
		try{
			ResultSet rs = DBMapper.executeQuery(QUERY_PEPET, QueryType.SELECT,'%', idUsers);
			if(rs.next()){
				pepet = rs.getInt("pepet");
				DBMapper.executeQuery(QUERY_PEPET_ADD, QueryType.UPDATE, pepet+amount, idUsers);
			}

			else
				throw new ExceptionPepetValue("pepet value is not in dataBase");
		}
		catch (Exception e) { //TODO
			e.getMessage();
		}

	}

	public static void removePepet(int idUsers,  int amount){
		int pepet;
		try{
			ResultSet rs = DBMapper.executeQuery(QUERY_PEPET, QueryType.SELECT,'%', idUsers);
			if(rs.next()){
				pepet = rs.getInt("pepet");
				if(pepet >= amount){
					DBMapper.executeQuery(QUERY_PEPET_ADD, QueryType.UPDATE, pepet-amount, idUsers);
				}
				else
					throw new ExceptionPepetValue("rs < amount");

			}

			else
				throw new ExceptionPepetValue("pepet value is not in dataBase");
		}
		catch (Exception e) {
			e.getMessage();
		}

	}
}
