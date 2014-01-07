package za.co.las.stock.object;

public class OptionalExtra {
	private int optionalExtraId;
	private String description;
	private double pricing;
	
	public int getOptionalExtraId() {
		return optionalExtraId;
	}
	public void setOptionalExtraId(int optionalExtraId) {
		this.optionalExtraId = optionalExtraId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPricing() {
		return pricing;
	}
	public void setPricing(double pricing) {
		this.pricing = pricing;
	}
	
	public String toJSONString() {
		return "{"
				+ "'optionalExtraId':'"+this.optionalExtraId+"', "
				+ "'description':'"+this.description+"', "
				+ "'pricing':'"+this.pricing+"' "
			+ "}";
	}
	
}
