package za.co.las.stock.object;

public class StockImage {
	private int stockId;
	private byte[] stockImage;
	
	public int getStockId() {
		return stockId;
	}
	public void setStockId(int stockId) {
		this.stockId = stockId;
	}
	public byte[] getStockImage() {
		return stockImage;
	}
	public void setStockImage(byte[] stockImage) {
		this.stockImage = stockImage;
	}	
}
