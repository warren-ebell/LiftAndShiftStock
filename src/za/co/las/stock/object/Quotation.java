package za.co.las.stock.object;

import java.util.ArrayList;

public class Quotation {
	private int quotationId;
	private int userId;
	private String quotationDate;
	private Customer customer;
	private String status;
	private String rate;
	private String notes;
	private String delivery;
	private String installation;
	private String warranty;
	private String variation;
	private String validity;
	private InstallationLocation installLocation;
	private ArrayList<Stock> quotationLineItems;
	private ArrayList<Accessory> accessoryItems;
	private int usedItem;
	
	public int getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(int quotationId) {
		this.quotationId = quotationId;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public ArrayList<Stock> getQuotationLineItems() {
		return quotationLineItems;
	}
	public void setQuotationLineItems(
			ArrayList<Stock> quotationLineItems) {
		this.quotationLineItems = quotationLineItems;
	}
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
	public String getQuotationDate() {
		return quotationDate;
	}
	public void setQuotationDate(String quotationDate) {
		this.quotationDate = quotationDate;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public ArrayList<Accessory> getAccessoryItems() {
		return accessoryItems;
	}
	public void setAccessoryItems(ArrayList<Accessory> accessoryItems) {
		this.accessoryItems = accessoryItems;
	}
	public InstallationLocation getInstallLocation() {
		return installLocation;
	}
	public void setInstallLocation(InstallationLocation installLocation) {
		this.installLocation = installLocation;
	}
	public int getUsedItem() {
		return usedItem;
	}
	public void setUsedItem(int usedItem) {
		this.usedItem = usedItem;
	}
}
