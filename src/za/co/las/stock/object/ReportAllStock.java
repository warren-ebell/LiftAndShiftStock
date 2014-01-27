package za.co.las.stock.object;

import java.text.DecimalFormat;

public class ReportAllStock extends ReportStock {
	private int stockStatus;
	private double stockPrice;
	
	public int getStockStatus() {
		return stockStatus;
	}
	public void setStockStatus(int stockStatus) {
		this.stockStatus = stockStatus;
	}
	public double getStockPrice() {
		return stockPrice;
	}
	public void setStockPrice(double stockPrice) {
		this.stockPrice = stockPrice;
	}
	
	public String toJSONString() {
		String statusString = "";
		if (this.stockStatus == 0)
			statusString = "Available";
		if (this.stockStatus == 1)
			statusString = "On quote";
		if (this.stockStatus == 2)
			statusString = "Unavailable";
		if (this.stockStatus == 3)
			statusString = "Sold";
		
		DecimalFormat df = new DecimalFormat("0.00");
		String returnString = "{"
				+ "'stockId':'"+super.getStockId()+"', "
				+ "'stockCode':'"+super.getStockCode()+"', "
				+ "'stockManufacturer':'"+super.getStockManufacturer()+"', "
				+ "'stockModel':'"+super.getStockModel()+"', "
				+ "'stockSeries':'"+super.getStockSeries()+"', "
				+ "'serialNumber':'"+super.getSerialNumber()+"', "
				+ "'stockUsed':'"+super.getStockUsed()+"', "
				+ "'stockStatus':'"+statusString+"', "
				+ "'stockPrice':'"+df.format(this.stockPrice)+"'";
		return returnString	+ "}";
	}
}
