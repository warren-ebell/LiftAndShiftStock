package za.co.las.stock.object;

public class TempAccessory {
	private String serial;
	private String code;
	private String currency;
	private double price;
	private int accessoryId;
	
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCurrency() {
		return this.currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getAccessoryId() {
		return accessoryId;
	}
	public void setAccessoryId(int accessoryId) {
		this.accessoryId = accessoryId;
	}

}
