package za.co.las.stock.object;

import java.text.DecimalFormat;
import java.util.ArrayList;

import za.co.las.stock.services.UtilityService;

public class Accessory {
	
	private int accessoryId;
	private String accessoryCode;
	private String accessoryModel;
	private String accessoryDescription;
	private String accessoryManufacturer;
	private double pricing;
	private ArrayList<StockLevel> accessoryLevel;
	private int accessoryMarkup;
	private int accessoryShipping;
	
	private UtilityService utilityService = new UtilityService();
	
	public int getAccessoryId() {
		return accessoryId;
	}
	public void setAccessoryId(int accessoryId) {
		this.accessoryId = accessoryId;
	}
	public String getAccessoryCode() {
		return accessoryCode;
	}
	public void setAccessoryCode(String accessoryCode) {
		this.accessoryCode = accessoryCode;
	}
	public String getAccessoryDescription() {
		return accessoryDescription;
	}
	public void setAccessoryDescription(String accessoryDescription) {
		this.accessoryDescription = accessoryDescription;
	}
	public String getAccessoryManufacturer() {
		return accessoryManufacturer;
	}
	public void setAccessoryManufacturer(String accessoryManufacturer) {
		this.accessoryManufacturer = accessoryManufacturer;
	}
	public double getPricing() {
		return pricing;
	}
	public void setPricing(double pricing) {
		this.pricing = pricing;
	}
	public ArrayList<StockLevel> getAccessoryLevel() {
		return accessoryLevel;
	}
	public void setAccessoryLevel(ArrayList<StockLevel> accessoryLevel) {
		this.accessoryLevel = accessoryLevel;
	}
	public String getAccessoryModel() {
		return accessoryModel;
	}
	public void setAccessoryModel(String accessoryModel) {
		this.accessoryModel = accessoryModel;
	}
	public int getAccessoryMarkup() {
		return accessoryMarkup;
	}
	public void setAccessoryMarkup(int accessoryMarkup) {
		this.accessoryMarkup = accessoryMarkup;
	}
	public int getAccessoryShipping() {
		return accessoryShipping;
	}
	public void setAccessoryShipping(int accessoryShipping) {
		this.accessoryShipping = accessoryShipping;
	}
	
	public String toJSONString() {
		DecimalFormat df = new DecimalFormat("0.00");
		double markup = 1.0 + (this.accessoryMarkup/100.0);
		double shipping = 1.0 + (this.accessoryShipping/100.0);
		String sellingPrice = df.format(this.pricing * markup * shipping);
		String returnString = "{"
				+ "'accessoryId':'"+this.accessoryId+"', "
				+ "'accessoryCode':'"+this.accessoryCode+"', "
				+ "'accessoryModel':'"+this.accessoryModel+"', "
				+ "'accessoryDescription':'"+this.accessoryDescription+"', "
				+ "'accessoryManufacturer':'"+this.accessoryManufacturer+"', "
				+ "'accessoryMarkup':'"+this.accessoryMarkup+"', "
				+ "'accessoryShipping':'"+this.accessoryShipping+"', "
				+ "'sellingPrice':'"+sellingPrice+"', "
				+ "'pricing':'"+df.format(this.pricing)+"', ";
		if (this.accessoryLevel != null) {
			returnString = returnString 
				+ "'accessoryLevel':"+utilityService.convertListOfStockLevelToJSONString(this.accessoryLevel)+" ";
		}
		else {
			returnString = returnString 
					+ "'accessoryLevel':[] ";
		}
		return returnString	+ "}";
	}
}
