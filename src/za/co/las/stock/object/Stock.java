package za.co.las.stock.object;

import java.util.ArrayList;

import za.co.las.stock.services.UtilityService;

public class Stock {
	private int stockId;
	private String stockCode;
	private String stockDescription;
	private String modelName;
	private double pricing;
	private String technicalSpecs;
	private String stockCategory;
	private ArrayList<StockLevel> stockLevel;
	
	private UtilityService utilityService = new UtilityService();
	
	public int getStockId() {
		return stockId;
	}
	public void setStockId(int stockId) {
		this.stockId = stockId;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public double getPricing() {
		return pricing;
	}
	public void setPricing(double pricing) {
		this.pricing = pricing;
	}
	public String getTechnicalSpecs() {
		return technicalSpecs;
	}
	public void setTechnicalSpecs(String technicalSpecs) {
		this.technicalSpecs = technicalSpecs;
	}
	public String getStockCategory() {
		return stockCategory;
	}
	public void setStockCategory(String stockCategory) {
		this.stockCategory = stockCategory;
	}
	public String getStockDescription() {
		return stockDescription;
	}
	public void setStockDescription(String stockDescription) {
		this.stockDescription = stockDescription;
	}
	public ArrayList<StockLevel> getStockLevel() {
		return stockLevel;
	}
	public void setStockLevel(ArrayList<StockLevel> stockLevel) {
		this.stockLevel = stockLevel;
	}
	
	public String toJSONString() {
		String returnString = "{"
				+ "'stockId':'"+this.stockId+"', "
				+ "'stockCode':'"+this.stockCode+"', "
				+ "'stockDescription':'"+this.stockDescription+"', "
				+ "'modelName':'"+this.modelName+"', "
				+ "'pricing':'"+this.pricing+"', "
				+ "'technicalSpecs':'"+this.technicalSpecs+"', "
				+ "'stockCategory':'"+this.stockCategory+"', ";
		if (this.stockLevel != null) {
			returnString = returnString 
				+ "'stockLevel':"+utilityService.convertListOfStockLevelToJSONString(this.stockLevel)+" ";
		}
		else {
			returnString = returnString 
					+ "'stockLevel':[] ";
		}
		return returnString	+ "}";
	}
}
