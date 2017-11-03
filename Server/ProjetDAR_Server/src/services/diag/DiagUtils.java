package services.diag;

import java.sql.SQLException;

import javax.naming.NamingException;

import org.json.JSONObject;

import database.DBMapper;
import database.MongoMapper;
import services.ServicesTools;
import services.diag.datastruct.ExceptionReport;
import utils.CannotLoadConfigException;

public class DiagUtils {

	/**
	 * Checks if the servlet can connect to database.
	 */
	public static JSONObject checkDatabase() {
		JSONObject answer;

		try {
			DBMapper.getMySQLConnection();
			answer = ServicesTools.createPositiveAnswer();
		} catch (SQLException | NamingException e) {
			answer = ServicesTools.createJSONFailure("Cannot connect to database");

			ServicesTools.addToPayload(answer, "report", new ExceptionReport(e));
		}

		return answer;
	}

	public static JSONObject checkNoSQL() {
		JSONObject answer;

		try {
			MongoMapper.getMongoDBConnection();
			answer = ServicesTools.createPositiveAnswer();
		} catch (SQLException | NamingException e) {
			answer = ServicesTools.createJSONFailure("Cannot connect to NoSQL database");

			ServicesTools.addToPayload(answer, "report", new ExceptionReport(e));
		}

		return answer;
	}

}
