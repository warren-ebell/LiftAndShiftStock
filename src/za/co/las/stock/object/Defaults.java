package za.co.las.stock.object;

public class Defaults {
	private String notes;
	private String delivery;
	private String installation;
	private String warranty;
	private String variation;
	private String validity;
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getDelivery() {
		return delivery;
	}
	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}
	public String getInstallation() {
		return installation;
	}
	public void setInstallation(String installation) {
		this.installation = installation;
	}
	public String getWarranty() {
		return warranty;
	}
	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}
	public String getVariation() {
		return variation;
	}
	public void setVariation(String variation) {
		this.variation = variation;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	
	public String toJSONString() {
		return "{"
				+ "'notes':'"+this.notes+"', "
				+ "'delivery':'"+this.delivery+"', "
				+ "'installation':'"+this.installation+"', "
				+ "'warranty':'"+this.warranty+"', "
				+ "'variation':'"+this.variation+"', "
				+ "'validity':'"+this.validity+"' "
			+ "}";
	}
}
