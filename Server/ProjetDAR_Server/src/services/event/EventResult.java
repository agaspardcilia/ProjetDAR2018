package services.event;

import java.util.List;

import services.datastructs.SearchResult;
import utils.JSONable;

/**
 * 
 * @author cb_mac
 *
 */
public class EventResult extends SearchResult{

	public EventResult(int page, int pageSize, List<? extends JSONable> results) {
		super(page, pageSize, results);
	}
}
