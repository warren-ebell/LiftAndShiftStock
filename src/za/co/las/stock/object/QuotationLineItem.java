package za.co.las.stock.object;

public class QuotationLineItem {
	private int stockId;
	private int quotationId;
	private int optionalExtraId;
	
	public int getStockId() {
		return stockId;
	}
	public void setStockId(int stockId) {
		this.stockId = stockId;
	}
	public int getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(int quotationId) {
		this.quotationId = quotationId;
	}
	public int getOptionalExtraId() {
		return optionalExtraId;
	}
	public void setOptionalExtraId(int optionalExtraId) {
		this.optionalExtraId = optionalExtraId;
	}
}
