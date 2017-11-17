package services.bet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.naming.NamingException;

import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.client.FindIterable;

import database.DBMapper;
import database.MongoMapper;
import database.exceptions.CannotConnectToDatabaseException;
import database.exceptions.QueryFailedException;
import database.DBMapper.QueryType;
import services.ServicesTools;
import services.bet.datastruct.BetStruct;

public class Bet {
	public final static String BETS_COLLECTION = "bets";
	public final static String USER_KEY = "user";
	public final static String EVENT_KEY = "event";
	public final static String BET_KEY = "bet";

	public final static String DATE_VALUE = "date";
	public final static String ODD_VALUE = "odd";
	public final static String MONEYBET_VALUE = "moneybet";

	private final static String GET_EVENT = "SELECT * FROM events WHERE idEvent = ?;";



	public static JSONObject addBet(int idUser, int idEvent,int moneyBet){
		JSONObject answer;
		Document doc = new Document();				
		doc.append(Bet.USER_KEY, idUser);
		doc.append(Bet.EVENT_KEY, idEvent);
		ResultSet res = null;
		double odd = 1;
		Date date = new Date();
		try {
			res = DBMapper.executeQuery(GET_EVENT, QueryType.SELECT, idEvent);
			res.next();
			odd = res.getDouble("odd");
			date = new Date(res.getLong("date"));
			doc.append(Bet.ODD_VALUE, odd); // requete pour recuperer cote
			doc.append(Bet.MONEYBET_VALUE, moneyBet);
			doc.append(Bet.DATE_VALUE, date); // idem
			MongoMapper.executeInsertOne(BETS_COLLECTION, doc);
			answer = ServicesTools.createPositiveAnswer();
		} catch (SQLException | CannotConnectToDatabaseException | QueryFailedException |NamingException e1) {
			answer = ServicesTools.createDatabaseError(e1);
		}

		return answer;
	}

	public static JSONObject printBet(int idBet){
		BetStruct print;
		JSONObject answer;
		Document args = new Document ();
		FindIterable<Document> qResult;
		args.append(BET_KEY, idBet);
		try {
			qResult = MongoMapper.executeGet(BETS_COLLECTION, args, 0);
			Document first = qResult.first();
			print = new BetStruct(idBet, (Integer)first.get(Bet.USER_KEY), 
					(Integer)first.get(Bet.EVENT_KEY), 
					(Integer)first.get(Bet.ODD_VALUE), 
					(Integer)first.get(Bet.MONEYBET_VALUE),
					(Date)first.get(Bet.DATE_VALUE)
					);
			answer = ServicesTools.createPositiveAnswer();
			ServicesTools.addToPayload(answer, "result", print);

		} catch (SQLException | NamingException e) {
			answer = ServicesTools.createDatabaseError(e);
		}

		return answer;
	}


}
