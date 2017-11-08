package services.social;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;

import database.MongoMapper;
import database.exceptions.CannotConnectToDatabaseException;
import database.exceptions.QueryFailedException;
import services.ServicesTools;
import services.auth.Authentication;
import services.datastructs.SearchResult;
import services.social.datastructs.Comment;
import services.social.datastructs.Count;
import services.social.datastructs.IsLiking;
import services.social.datastructs.Status;
import services.user.datastructs.User;

public class SocialUtils {
	public final static String STATUS_COLLECTION = "status";
	public final static String COMMENTS_COLLECTION = "comments";
	public final static String LIKES_COLLECTION = "likes";
	
	public final static String AUTHOR_KEY = "author";
	public final static String CONTENT_KEY = "content";
	public final static String TIMESTAMP_KEY = "timestamp";
	public final static String STATUS_ID_KEY = "status_id";
	
	
	public final static String CONTENT_ARG = "content";
	public final static String COMMENT_ARG = "comment";
	public final static String STATUS_ID_ARG = "statusid";
	public final static String COUNT_ARG = "count";
	
	
	public static JSONObject addStatus(String key, String content) {
		JSONObject result;
		
		try {
			int idUser = Authentication.getIdUserFromKey(key);
		
			Status newStatus = addStatusToDB(idUser, content);
			
			result = ServicesTools.createPositiveAnswer();
			
			ServicesTools.addToPayload(result, "status", newStatus);

		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException | NamingException e) {
			result = ServicesTools.createDatabaseError(e);
		}
		
		return result;
	}
	
	public static JSONObject getUsersLastStatus(int idUser, int size) {
		JSONObject result;
		
		
		try {
		
			SearchResult sr = getUserStatus(idUser, true, System.currentTimeMillis(), 0, size);	
			
			result = ServicesTools.createPositiveAnswer();
			
			ServicesTools.addToPayload(result, "result", sr);
		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException | NamingException e) {
			result = ServicesTools.createDatabaseError(e);
		}
		
		return result;
	}
	
	public static JSONObject getStatusComments(String idStatus, int page, int pageSize) {
		JSONObject result;
		
		try {
		
			SearchResult sr = getStatusComments_intern(idStatus, page, pageSize);
			
			result = ServicesTools.createPositiveAnswer();
			
			ServicesTools.addToPayload(result, "result", sr);
		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException | NamingException e) {
			result = ServicesTools.createDatabaseError(e);
		}
		
		return result;
	}
	
	public static JSONObject addCommentToStatus(String key, String statusId, String content) {
		JSONObject result;
		
		try {
			int idUser = Authentication.getIdUserFromKey(key);
			Comment c = addComment(idUser, statusId, content);
			
			result = ServicesTools.createPositiveAnswer();
			ServicesTools.addToPayload(result, COMMENT_ARG, c);
			
		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException | NamingException e) {
			result = ServicesTools.createDatabaseError(e);
		}
		
		
		return result;
	}
	
	public static JSONObject addLike(String key, String statusId) {
		JSONObject result;
		
		try {
			int idUser = Authentication.getIdUserFromKey(key);
			
			addLike_intern(statusId, idUser);
			
			result = ServicesTools.createPositiveAnswer();
		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException | NamingException e) {
			result = ServicesTools.createDatabaseError(e);
		}
		
		return result;
	}
	
	public static JSONObject removeLike(String key, String statusId) {
		JSONObject result;
		
		try {
			int idUser = Authentication.getIdUserFromKey(key);
			
			removeLike_intern(statusId, idUser);
			
			result = ServicesTools.createPositiveAnswer();
		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException | NamingException e) {
			result = ServicesTools.createDatabaseError(e);
		}
		
		return result;
	}
	
	public static JSONObject getLikeCount(String statusId) {
		JSONObject result;
		
		try {
			int count = getLikeCount_intern(statusId);
			
			result = ServicesTools.createPositiveAnswer();
			
			ServicesTools.addToPayload(result, COUNT_ARG, new Count(statusId, count));
			
		} catch (SQLException | NamingException e) {
			result = ServicesTools.createDatabaseError(e);
		}
		
		return result;
	}
	
	public static JSONObject isUserLiking(int idUser, String statusId) {
		JSONObject result;
		
		try {
			
			boolean isLiking = isLiking_intern(statusId, idUser);
			
			result = ServicesTools.createPositiveAnswer();
			ServicesTools.addToPayload(result, "isliking", new IsLiking(idUser, statusId, isLiking));
		} catch (SQLException | NamingException e) {
			result = ServicesTools.createDatabaseError(e);
		}
		
		return result;
	}
	
	
	private static Status addStatusToDB(int idUser, String content) throws SQLException, NamingException, CannotConnectToDatabaseException, QueryFailedException {
		Document doc = new Document();
		
		long timestamp = System.currentTimeMillis();
		
		doc.append(SocialUtils.AUTHOR_KEY, idUser);
		doc.append(SocialUtils.CONTENT_KEY, content);
		doc.append(SocialUtils.TIMESTAMP_KEY, timestamp);
		
		MongoMapper.executeInsertOne(STATUS_COLLECTION, doc);
		
		FindIterable<Document> qResult;
		Map<String, Object> args = new HashMap<>();

		args.put(SocialUtils.AUTHOR_KEY, idUser);
		args.put(TIMESTAMP_KEY, timestamp);

		qResult = MongoMapper.executeGet(STATUS_COLLECTION, args, 0);

		doc = qResult.first();
		
		return getStatusFromDocument(doc);
	}
	
	
	/**
	 * 
	 * @param after 
	 * 		true = after, false = before
	 * @param timestamp
	 * 		When ?
	 * @return
	 * 		A bunch of status.
	 * @throws NamingException 
	 * @throws SQLException 
	 * @throws QueryFailedException 
	 * @throws CannotConnectToDatabaseException 
	 */
	public static SearchResult getUserStatus(int idUser, boolean after, long timestamp, int page, int pageSize) throws SQLException, NamingException, CannotConnectToDatabaseException, QueryFailedException {
		List<Status> result = new ArrayList<>();
		
		String operator = (after) ? MongoMapper.Operator.LT.toString() : MongoMapper.Operator.GT.toString();
		
		
		
		HashMap<String, Object> args = new HashMap<>();
		args.put(TIMESTAMP_KEY, new BasicDBObject(operator, timestamp));
		args.put(AUTHOR_KEY, idUser);
		
		FindIterable<Document> res = MongoMapper.executeGet(STATUS_COLLECTION, args, page*pageSize);
		
		int i = 0;
		for(Document d : res) {
			if (i >= pageSize) {
				break;
			} else {
				i++;
			}
			
			result.add(getStatusFromDocument(d));
		}
		
		return new SearchResult(page, pageSize, result);
	}

	private static Comment addComment(int idAuthor, String statusId, String content) throws SQLException, NamingException, CannotConnectToDatabaseException, QueryFailedException {
		
		long timestamp = System.currentTimeMillis();
		
		
		Document doc = new Document();
		doc.append(AUTHOR_KEY, idAuthor);
		doc.append(STATUS_ID_KEY, statusId);
		doc.append(CONTENT_KEY, content);
		doc.append(TIMESTAMP_KEY, timestamp);
		
		MongoMapper.executeInsertOne(COMMENTS_COLLECTION, doc);
		
		FindIterable<Document> qResult;
		Map<String, Object> args = new HashMap<>();

		args.put(SocialUtils.AUTHOR_KEY, idAuthor);
		args.put(TIMESTAMP_KEY, timestamp);

		qResult = MongoMapper.executeGet(COMMENTS_COLLECTION, args, 0);

		doc = qResult.first();
		
		return getCommentFromDocument(doc);
	}
	
	private static void addLike_intern(String idStatus, int idUser) throws SQLException, NamingException {
		long timestamp = System.currentTimeMillis();
		
		Document doc = new Document();
		doc.append(STATUS_ID_KEY, idStatus);
		doc.append(AUTHOR_KEY, idUser);
		doc.append(TIMESTAMP_KEY, timestamp);
		
		MongoMapper.executeInsertOne(LIKES_COLLECTION, doc);
	}
	
	private static int getLikeCount_intern(String idStatus) throws SQLException, NamingException {
		Map<String, Object> args = new HashMap<>();

		args.put(STATUS_ID_KEY, idStatus);

		FindIterable<Document> res = MongoMapper.executeGet(LIKES_COLLECTION, args, 0);
		
		return MongoMapper.count(res);
	}
	
	private static void removeLike_intern(String idStatus, int idUser) throws SQLException, NamingException {
		Document toRemove = new Document();
		toRemove.append(STATUS_ID_KEY, idStatus);
		toRemove.append(AUTHOR_KEY, idUser);
		
		MongoMapper.executeDeleteOne(LIKES_COLLECTION, toRemove);
	}
	
	private static boolean isLiking_intern(String idStatus, int idUser) throws SQLException, NamingException {
		Map<String, Object> args = new HashMap<>();

		args.put(STATUS_ID_KEY, idStatus);
		args.put(AUTHOR_KEY, idUser);
		
		FindIterable<Document> res = MongoMapper.executeGet(LIKES_COLLECTION, args, 0);
		
		return MongoMapper.count(res) != 0;
	}
	
	private static SearchResult getStatusComments_intern(String statusId, int page, int pageSize) throws SQLException, NamingException, CannotConnectToDatabaseException, QueryFailedException {
		
		ArrayList<Comment> comments = new ArrayList<>();
		
		HashMap<String, Object> args = new HashMap<>();
		args.put(STATUS_ID_KEY, statusId);
		
		FindIterable<Document> res = MongoMapper.executeGet(COMMENTS_COLLECTION, args, page*pageSize);
		
		
		int i = 0;
		for(Document d : res) {
			if (i >= pageSize) {
				break;
			} else {
				i++;
			}
			comments.add(getCommentFromDocument(d));
		}
		
		return new SearchResult(page, pageSize, comments);
	}
	
	private static Status getStatusFromDocument(Document doc) throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		String id;
		User author;
		String content;
		long timestamp;
		
		id = doc.getObjectId(MongoMapper.DOC_ID).toHexString();
		author = Authentication.getUserFromId(doc.getInteger(AUTHOR_KEY));
		content = doc.getString(CONTENT_KEY);
		timestamp = doc.getLong(TIMESTAMP_KEY);
		
		return new Status(id, author, content, timestamp);
	}
	
	private static Comment getCommentFromDocument(Document doc) throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		String id;
		User author;
		String content;
		long timestamp;
		String statusId;
		
		id = doc.getObjectId(MongoMapper.DOC_ID).toHexString();
		author = Authentication.getUserFromId(doc.getInteger(AUTHOR_KEY));
		content = doc.getString(CONTENT_KEY);
		timestamp = doc.getLong(TIMESTAMP_KEY);
		statusId = doc.getString(STATUS_ID_KEY);
		
		
		return new Comment(id, author, timestamp, content, statusId);
	}
	
}
