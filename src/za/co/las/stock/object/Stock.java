package za.co.las.stock.object;

import java.text.DecimalFormat;
import java.util.ArrayList;

import za.co.las.stock.services.UtilityService;

public class Stock {
	private int stockId;
	private String stockCode;
	private String stockDescription;
	private double pricing;
	private String technicalSpecs;
	private String stockManufacturer;
	private String stockSeries;
	private String stockModel;
	private ArrayList<StockLevel> stockLevel;
	private ArrayList<InstallationLocation> installLocations;
	private int stockUsed;
	
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
	public String getStockManufacturer() {
		return stockManufacturer;
	}
	public void setStockManufacturer(String stockManufacturer) {
		this.stockManufacturer = stockManufacturer;
	}
	public String getStockSeries() {
		return stockSeries;
	}
	public void setStockSeries(String stockSeries) {
		this.stockSeries = stockSeries;
	}
	public String getStockModel() {
		return stockModel;
	}
	public void setStockModel(String stockModel) {
		this.stockModel = stockModel;
	}
	public UtilityService getUtilityService() {
		return utilityService;
	}
	public void setUtilityService(UtilityService utilityService) {
		this.utilityService = utilityService;
	}
	public ArrayList<InstallationLocation> getInstallLocations() {
		return installLocations;
	}
	public void setInstallLocations(ArrayList<InstallationLocation> installLocations) {
		this.installLocations = installLocations;
	}
	public int getStockUsed() {
		return stockUsed;
	}
	public void setStockUsed(int stockUsed) {
		this.stockUsed = stockUsed;
	}
	
	public String toJSONString() {
		DecimalFormat df = new DecimalFormat("0.00");
		String returnString = "{"
				+ "'stockId':'"+this.stockId+"', "
				+ "'stockCode':'"+this.stockCode+"', "
				+ "'stockDescription':'"+this.stockDescription+"', "
				+ "'stockManufacturer':'"+this.stockManufacturer+"', "
				+ "'stockModel':'"+this.stockModel+"', "
				+ "'stockSeries':'"+this.stockSeries+"', "
				+ "'pricing':'"+df.format(this.pricing)+"', "
				+ "'stockUsed':'"+this.stockUsed+"', "
				+ "'technicalSpecs':'"+this.technicalSpecs+"', ";
		if (this.installLocations != null) {
			returnString = returnString 
					+ "'installLocation':"+utilityService.convertListOfInstallLocationsToJSONString(this.installLocations)+", ";
		}
		else {
			returnString = returnString 
					+ "'installLocation':[], ";
		}
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
