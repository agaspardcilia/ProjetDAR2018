package services.bank;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import database.DBMapper;
import database.DBMapper.QueryType;
import database.exceptions.CannotConnectToDatabaseException;
import database.exceptions.QueryFailedException;
import services.ServicesTools;
import services.auth.Authentication;
import services.bank.datastrucs.Account;
import services.user.datastructs.User;

public class BankUtils {
	public final static double DEFAULT_ACCOUNT_BALANCE = 1000d;
	
	private final static String QUERY_GET_ACCOUNT = "SELECT * FROM accounts WHERE iduser = ?;";
	private final static String QUERY_CHANGE_BALANCE = "UPDATE accounts SET balance = balance+? WHERE iduser = ?;";
	private final static String QUERY_NEW_ACCOUNT = "INSERT INTO accounts VALUES (DEFAULT, ?, ?)";
	
	
	private final static String BALANCE_ATTR = "balance";
	
	
	
	public static JSONObject getAccountBalance(String key) {
		JSONObject result;
		
		try {
			int userId = Authentication.getIdUserFromKey(key);
			Account account = getAccountFromUserId(userId);
			result = ServicesTools.createPositiveAnswer();
			
			ServicesTools.addToPayload(result, "account", account);
			
		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException e) {
			result = ServicesTools.createDatabaseError(e);
		}
		
		
		return result;
	}
	
	public static void createNewAccount(int idUser, double amount) throws CannotConnectToDatabaseException, QueryFailedException {
		DBMapper.executeQuery(QUERY_NEW_ACCOUNT, QueryType.INSERT, idUser, amount);
	}

	
	public static Account getAccountFromUserId(int userId) throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		ResultSet rs = DBMapper.executeQuery(QUERY_GET_ACCOUNT, QueryType.SELECT, userId);
		
		if (!rs.next())
			return null;
		
		User owner = Authentication.getUserFromId(userId);
		
		return new Account(owner, rs.getDouble(BALANCE_ATTR));
	}
	
	public static void changeAccountBalance(int userId, double amount) throws CannotConnectToDatabaseException, QueryFailedException {
		DBMapper.executeQuery(QUERY_CHANGE_BALANCE, QueryType.UPDATE, amount, userId);
	}
}
