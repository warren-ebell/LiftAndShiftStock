package za.co.las.stock.object;

public class StockLevel {
	private String serialNumber;
	private int status;
	
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String toJSONString() {
		String statusString = "";
		if (this.status == 0)
			statusString = "Available";
		if (this.status == 1)
			statusString = "On quote";
		if (this.status == 2)
			statusString = "Unavailable";
		return "{"
				+ "'serialNumber':'"+this.serialNumber+"', "
				+ "'status':'"+statusString+"' "
			+ "}";
	}
}
