package za.co.las.stock.services;

import java.util.ArrayList;

import za.co.las.stock.object.MiniQuote;
import za.co.las.stock.object.OptionalExtra;
import za.co.las.stock.object.Stock;
import za.co.las.stock.object.StockLevel;
import za.co.las.stock.object.User;

public class UtilityService {
	
	public String convertListOfStockToJSONString(ArrayList<Stock> stockItemList) {
		String stockJSONString = "[";
		for (Stock s:stockItemList) {
			if (stockJSONString.length() == 1) {
				stockJSONString += s.toJSONString();
			}
			else {
				stockJSONString += "," + s.toJSONString();
			}
		}		
		stockJSONString += "]";
		return stockJSONString;
	}
	
	public String convertListOfStockLevelToJSONString(ArrayList<StockLevel> stockLevelItemList) {
		String stockLevelJSONString = "[";
		for (StockLevel s:stockLevelItemList) {
			if (stockLevelJSONString.length() == 1) {
				stockLevelJSONString += s.toJSONString();
			}
			else {
				stockLevelJSONString += "," + s.toJSONString();
			}
		}		
		stockLevelJSONString += "]";
		return stockLevelJSONString;
	}
	
	public String convertListOfStockCategoriesToJSONString(ArrayList<String> stockItemList) {
		String stockJSONString = "[";
		for (String s:stockItemList) {
			if (stockJSONString.length() == 1) {
				stockJSONString += "{'category':'"+s+"'}";
			}
			else {
				stockJSONString += "," + "{'category':'"+s+"'}";
			}
		}		
		stockJSONString += "]";
		return stockJSONString;
	}
	
	public String convertListOfOptionalExtrasToJSONString(ArrayList<OptionalExtra> optionalExtraItemList) {
		String oeJSONString = "[";
		for (OptionalExtra oe:optionalExtraItemList) {
			if (oeJSONString.length() == 1) {
				oeJSONString += oe.toJSONString();
			}
			else {
				oeJSONString += "," + oe.toJSONString();
			}
		}		
		oeJSONString += "]";
		return oeJSONString;
	}
	
	public String convertListOfUsersToJSONString(ArrayList<User> userList) {
		String userJSONString = "[";
		for (User u:userList) {
			if (userJSONString.length() == 1) {
				userJSONString += u.toJSONString();
			}
			else {
				userJSONString += "," + u.toJSONString();
			}
		}		
		userJSONString += "]";
		return userJSONString;
	}
	
	public String convertListOfMiniQuotesToJSONString(ArrayList<MiniQuote> quoteList) {
		String quoteJSONString = "[";
		for (MiniQuote mini:quoteList) {
			if (quoteJSONString.length() == 1) {
				quoteJSONString += mini.toJSONString();
			}
			else {
				quoteJSONString += "," + mini.toJSONString();
			}
		}		
		quoteJSONString += "]";
		return quoteJSONString;
	}
	
	public String buildResponseMessage(int result, String message) {
		return "{'result':'"+result+"', 'message':'"+message+"'}";
	}

}
