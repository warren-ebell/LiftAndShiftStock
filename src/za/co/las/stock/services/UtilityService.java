package za.co.las.stock.services;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import za.co.las.stock.object.Accessory;
import za.co.las.stock.object.InstallationLocation;
import za.co.las.stock.object.MiniQuote;
import za.co.las.stock.object.OptionalExtra;
import za.co.las.stock.object.Stock;
import za.co.las.stock.object.StockLevel;
import za.co.las.stock.object.TempAccessory;
import za.co.las.stock.object.User;

public class UtilityService {
	
	public ArrayList<OptionalExtra> convertOptionalExtraJSONStringToListWithPricingFactor(String jsonString, double factor) {
		ArrayList<OptionalExtra> optionalExtras = new ArrayList<OptionalExtra>();
		JSONParser parser = new JSONParser();
		
		try {
		
			JSONArray array = (JSONArray)parser.parse(jsonString);
			for (int i = 0; i < array.size(); i++) {
				JSONObject jsonObj = (JSONObject)array.get(i);
				System.out.println("description=" + jsonObj.get("description"));
				System.out.println("pricing=" + jsonObj.get("pricing"));
				
				double pricing = ((Long)jsonObj.get("pricing")) * factor;
				OptionalExtra oe = new OptionalExtra();
				oe.setDescription((String)jsonObj.get("description"));
				oe.setPricing(pricing);
				
				optionalExtras.add(oe);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return optionalExtras;
	}
	
	public InstallationLocation convertInstallationLocationJSONStringWithPricingFactor(String jsonString, double factor) {
		InstallationLocation location = new InstallationLocation();
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(jsonString);
			JSONObject jsonObj = (JSONObject)obj;
			String loc = (String)jsonObj.get("location");
			String price = (String)jsonObj.get("price");
			
			location.setLocation(loc);
			
			double pricing = Double.parseDouble(price) * factor;
			location.setPrice(pricing);
			
			return location;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}
	
	public ArrayList<TempAccessory> convertAccessoryJSONStringToListWithPricingFactor(String jsonString, double factor) {
		ArrayList<TempAccessory> accessoryList = new ArrayList<TempAccessory>();
		JSONParser parser = new JSONParser();
		
		try {
		
			JSONArray array = (JSONArray)parser.parse(jsonString);
			for (int i = 0; i < array.size(); i++) {
				JSONObject jsonObj = (JSONObject)array.get(i);
				System.out.println("serial=" + jsonObj.get("serial"));
				System.out.println("code=" + jsonObj.get("code"));
				System.out.println("price=" + jsonObj.get("price"));
				System.out.println("accessoryId=" + jsonObj.get("accessoryId"));
				
				double pricing = (Double.parseDouble((String)jsonObj.get("price"))) * factor;
				
				TempAccessory tempAccessory = new TempAccessory();
				tempAccessory.setAccessoryId(Integer.parseInt((String)jsonObj.get("accessoryId")));
				tempAccessory.setCode((String)jsonObj.get("code"));
				tempAccessory.setPrice(pricing);
				tempAccessory.setSerial((String)jsonObj.get("serial"));
				
				accessoryList.add(tempAccessory);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return accessoryList;
	}
	
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
	
	public String convertListOfAccessoryToJSONString(ArrayList<Accessory> accessoryItemList) {
		String stockJSONString = "[";
		for (Accessory a:accessoryItemList) {
			if (stockJSONString.length() == 1) {
				stockJSONString += a.toJSONString();
			}
			else {
				stockJSONString += "," + a.toJSONString();
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
	
	public String convertListOfInstallLocationsToJSONString(ArrayList<InstallationLocation> installLocationList) {
		String stockLevelJSONString = "[";
		for (InstallationLocation i:installLocationList) {
			if (stockLevelJSONString.length() == 1) {
				stockLevelJSONString += i.toJSONString();
			}
			else {
				stockLevelJSONString += "," + i.toJSONString();
			}
		}		
		stockLevelJSONString += "]";
		return stockLevelJSONString;
	}
	
	public String convertListOfStockManufacturersToJSONString(ArrayList<String> stockManufacturerList) {
		String stockJSONString = "[";
		for (String s:stockManufacturerList) {
			if (stockJSONString.length() == 1) {
				stockJSONString += "{'manufacturer':'"+s+"'}";
			}
			else {
				stockJSONString += "," + "{'manufacturer':'"+s+"'}";
			}
		}		
		stockJSONString += "]";
		return stockJSONString;
	}
	
	public String convertListOfStockModelsToJSONString(ArrayList<String> stockModelList) {
		String stockJSONString = "[";
		for (String s:stockModelList) {
			if (stockJSONString.length() == 1) {
				stockJSONString += "{'model':'"+s+"'}";
			}
			else {
				stockJSONString += "," + "{'model':'"+s+"'}";
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
