package services;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import database.DataBaseErrors;
import database.exceptions.CannotConnectToDatabaseException;
import database.exceptions.QueryFailedException;
import services.auth.AuthErrors;
import services.errors.ServerErrors;
import services.errors.ServletError;
import utils.Debug;
import utils.JSONable;

public class ServicesTools {
	//Common args name.
	public final static String KEY_ARG			= "key";
	public final static String IDUSER_ARG 		= "iduser";
	public final static String USERNAME_ARG 	= "username";
	public final static String PASSWORD_ARG 	= "password";
	public final static String EMAIL_ARG 		= "email";
	public final static String SIZE_ARG 		= "size";
	public final static String PAGE_ARG 		= "page";
	public final static String QUERY_ARG 		= "query";
	public final static String ID_ARG 			= "id";
	public final static String USER_ARG 		= "user";
	
	public final static String STATUS_ANSWER = "status";
	public final static String PAYLOAD_ANSWER = "payload";
	public final static String SUCCESS_ANSWER = "success";
	public final static String FAILURE_ANSWER = "failure";
	public final static String CHALLENGE_ANSWER = "challenge";
	
	//
	public final static String IDCITY_ARG = "idcity";
	public final static String DATE_ARG = "date";
	public final static String EVENTTYPE_ARG= "eventtype";
	//
	public final static String IDFRIEND_ARG="idfriend";
	

	/**
	 * Check if there's a null in params.
	 * @param objs
	 * 	Objects to test.
	 * @return
	 * 	True = there is a null. False = no nulls.
	 */
	public static boolean nullChecker(Object... objs) {
		for (Object object : objs) {
			if (object == null)
				return true;
		}

		return false;
	}


	public static JSONObject createJSONError(ServletError error) {
		JSONObject json = new JSONObject();

		json.put(STATUS_ANSWER, FAILURE_ANSWER);

		JSONObject payload = new JSONObject();


		payload.put("errorCode", error.getCode());
		payload.put("errorMessage", error.getMessage());

		json.put(PAYLOAD_ANSWER, payload);

		return json;
	}

	public static JSONObject createJSONFailure(String notice) {
		JSONObject json = new JSONObject();

		json.put(STATUS_ANSWER, FAILURE_ANSWER);

		JSONObject payload = new JSONObject();


		payload.put("notice", notice);

		json.put(PAYLOAD_ANSWER, payload);

		return json;
	}
	
	public static void addToPayload(JSONObject json, String key, JSONable value) {
		if (json.isNull(PAYLOAD_ANSWER))
			json.put(PAYLOAD_ANSWER, new JSONObject());
		
		json.getJSONObject(PAYLOAD_ANSWER).put(key, value.toJSONObject());
	}
	
	public static JSONObject createJSONError(ServletError error, String debugNotice) {
		JSONObject result = createJSONError(error);

		if (Debug.isInDebug())
			result.getJSONObject(PAYLOAD_ANSWER).put("debug", debugNotice);

		return result;
	}

	public static JSONObject createJSONError(ServletError error, Exception e) {
		JSONObject json = createJSONError(error);

		if (Debug.isInDebug()) 
			json.getJSONObject(PAYLOAD_ANSWER).put("debug", e.getMessage());

		return json;
	}

	public static JSONObject createPositiveAnswer() {
		JSONObject ret = new JSONObject();

		ret.put(STATUS_ANSWER, SUCCESS_ANSWER);

		return ret;
	}


	public static String hex(byte[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i]
					& 0xFF) | 0x100).substring(1,3));        
		}
		return sb.toString();
	}

	public static String md5Hex(String message) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return hex (md.digest(message.getBytes("CP1252")));
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}


	public static void addCORSHeader(HttpServletResponse resp) {
		resp.addHeader("Access-Control-Allow-Origin", "*");
	}

	public static JSONObject createDatabaseError(Exception e) {
		JSONObject result;

		if (e instanceof SQLException) {
			Debug.display_stack(e);
			result = createJSONError(DataBaseErrors.UKNOWN_SQL_ERROR);
		} else if (e instanceof CannotConnectToDatabaseException) {
			result = createJSONError(DataBaseErrors.CANNOT_CONNECT_TO_DATABASE);
		} else if (e instanceof QueryFailedException) {
			result = createJSONError(DataBaseErrors.QUERY_FAILED);
		} else {
			Debug.display_stack(e);
			result = createJSONError(DataBaseErrors.UKNOWN_DB_EXCEPTION);
		}

		if (Debug.isInDebug()) {
			result.getJSONObject(PAYLOAD_ANSWER).put("debug", e.getMessage());
		}


		return result;
	}

	public static JSONObject createInvalidKeyError() {
		return createJSONError(ServerErrors.INVALID_KEY);
	}
	
	public static JSONObject createMissingArgumentError() {
		return createJSONError(ServerErrors.MISSING_ARGUMENT);
	}


}















