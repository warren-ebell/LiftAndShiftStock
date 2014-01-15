package za.co.las.stock.object;

import java.text.DecimalFormat;

public class InstallationLocation {
	private String location;
	private double price;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String toJSONString() {
		DecimalFormat df = new DecimalFormat("0.00");
		return "{"
				+ "'location':'"+this.location+"', "
				+ "'price':'"+df.format(this.price)+"' "
			+ "}";
	}
}
