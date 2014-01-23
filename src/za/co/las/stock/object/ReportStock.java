package za.co.las.stock.object;

public class ReportStock {
	private int stockId;
	private String stockCode;
	private String stockManufacturer;
	private String stockModel;
	private String stockSeries;
	private int stockUsed;
	private String serialNumber;
	
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
	public String getStockManufacturer() {
		return stockManufacturer;
	}
	public void setStockManufacturer(String stockManufacturer) {
		this.stockManufacturer = stockManufacturer;
	}
	public String getStockModel() {
		return stockModel;
	}
	public void setStockModel(String stockModel) {
		this.stockModel = stockModel;
	}
	public String getStockSeries() {
		return stockSeries;
	}
	public void setStockSeries(String stockSeries) {
		this.stockSeries = stockSeries;
	}
	public int getStockUsed() {
		return stockUsed;
	}
	public void setStockUsed(int stockUsed) {
		this.stockUsed = stockUsed;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	} 
	
	public String toJSONString() {
		String returnString = "{"
				+ "'stockId':'"+this.stockId+"', "
				+ "'stockCode':'"+this.stockCode+"', "
				+ "'stockManufacturer':'"+this.stockManufacturer+"', "
				+ "'stockModel':'"+this.stockModel+"', "
				+ "'stockSeries':'"+this.stockSeries+"', "
				+ "'serialNumber':'"+this.serialNumber+"', "
				+ "'stockUsed':'"+this.stockUsed+"'";
		return returnString	+ "}";
	}

}
