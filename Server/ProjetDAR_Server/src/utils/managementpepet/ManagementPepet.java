package utils.managementpepet;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.DBMapper;
import database.DBMapper.QueryType;
import database.exceptions.CannotConnectToDatabaseException;
import database.exceptions.QueryFailedException;
import utils.managementpepet.error.ExceptionPepetValue;

public class ManagementPepet {
	private final static String QUERY_PEPET = "SELECT account.balance FROM account WHERE iduser = ?";
	private final static String QUERY_PEPET_ADD = "UPDATE account SET balance = ? WHERE iduser = ?";
	private final static String QUERY_CHECK_PEPET = "SELECT * FROM account WHERE iduser = ?" ;
	private final static String QUERY_CREATE_ACCOUNT = "INSERT INTO account VALUES (DEFAULT, ?, ?)";

	//create account
	//checker si count créer
	public static boolean haveAccount(int idUsers){
		ResultSet rs;
		try {
			rs = DBMapper.executeQuery(QUERY_CHECK_PEPET, QueryType.SELECT, idUsers);
			return rs.next(); 
		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	public static void createPepet(int idUsers){
		try {
			DBMapper.executeQuery(QUERY_CREATE_ACCOUNT, QueryType.INSERT, idUsers, 100);
		} catch (CannotConnectToDatabaseException | QueryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void addPepet(int idUsers, int amount){
		int pepet;
		try{
			if(!haveAccount(idUsers)){
				createPepet(idUsers);
			}
			ResultSet rs = DBMapper.executeQuery(QUERY_PEPET, QueryType.SELECT, idUsers);
			if(rs.next()){
				pepet = rs.getInt("balance");
				DBMapper.executeQuery(QUERY_PEPET_ADD, QueryType.UPDATE, pepet+amount, idUsers);
			}

			else
				throw new ExceptionPepetValue("pepet value is not in dataBase");			

		}
		catch (Exception e) { 
			e.getMessage();
		}
	}

	public static void removePepet(int idUsers,  int amount){
		int pepet;
		try{
			ResultSet rs = DBMapper.executeQuery(QUERY_PEPET, QueryType.SELECT, idUsers);
			if(rs.next()){
				pepet = rs.getInt("balance");
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
			//TODO somthing
			e.printStackTrace(); 
		}

	}
	public static void main(String[] args) {
		addPepet(4, 100);
	}
}
