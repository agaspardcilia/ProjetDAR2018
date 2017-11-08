package database;

import java.sql.SQLException;
import java.util.Map;
import java.util.function.Consumer;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


/**
 * @author marin
 * MongoMapper.java
 * MongoDB Connection.
 */
public class MongoMapper {
	public final static String DOC_ID = "_id";
	
	public final static String DATABASE = "platine";

	private static MongoDatabase db;

	public static MongoDatabase getMongoDBConnection() throws SQLException, NamingException {
		InitialContext cxt = new InitialContext();
		MongoClient m = (MongoClient) cxt.lookup("java:/comp/env/mongodb/Mongo" );
		
		if (db == null)
			db = m.getDatabase(DATABASE);
		
		return db;
	}
	
	
	
	public static void executeInsertOne(String collection, Document doc) throws SQLException, NamingException{
		MongoDatabase database = getMongoDBConnection();
		MongoCollection<Document> collect = database.getCollection(collection);
		collect.insertOne(doc);
	}
	
	public static void executeDeleteOne(String collection, Document doc) throws SQLException, NamingException{
		MongoDatabase database = getMongoDBConnection();
		MongoCollection<Document> collect = database.getCollection(collection);
		collect.deleteOne(doc);
	}
	
	/**
	 * Execute a MongoDB query.
	 * @param collection
	 * 	Collection to use.
	 * @param whereArgs
	 * 	Arguments.
	 * @param startIndex
	 * 	Number of result to skip.
	 * @return
	 * 	Result.
	 * @throws SQLException
	 * XXX TEST : partial, not fully tested but for now it works. I guess.
	 * @throws NamingException 
	 */
	public static FindIterable<Document> executeGet(String collection, Map<String, Object> whereArgs, int startIndex) throws SQLException, NamingException {
		MongoCollection<Document> database = getMongoDBConnection().getCollection(collection);
		BasicDBObject whereQuery = new BasicDBObject();
		FindIterable<Document> result;
		
		whereQuery.putAll(whereArgs);
		
		result = database.find(whereQuery);
		
		result.skip(startIndex);
		
		return result;
	}
	
	public static FindIterable<Document> executeGetWSort(String collection, Map<String, Object> whereArgs, Map<String, Object> sort, int startIndex) throws SQLException, NamingException {
		MongoCollection<Document> database = getMongoDBConnection().getCollection(collection);
		BasicDBObject whereQuery = new BasicDBObject();
		BasicDBObject sortQuery = new BasicDBObject();
		FindIterable<Document> result;
		
		whereQuery.putAll(whereArgs);
		sortQuery.putAll(sort);
		
		result = database.find(whereQuery).sort(sortQuery);
		
		result.skip(startIndex);
		
		return result;
	}
	
	public static MongoCollection<Document> getCollection(String collection) throws SQLException, NamingException {
		return getMongoDBConnection().getCollection(collection);
	}
	
	public static int count(FindIterable<Document> findQuery) {
		int result = 0;
		for (Document d : findQuery) {
			result++;
		}
		
		return result;
	}
	
	public enum Operator {
		GT("gt"), LT("lt");
		
		private String str;
		
		private Operator(String str) {
			this.str = str;
		}
		
		@Override
		public String toString() {
			return "$" + str;
		}
	}
}

