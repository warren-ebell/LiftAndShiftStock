package za.co.las.stock.object;

public class Customer {
	private int customerId;
	private String name;
	private String address;
	private String emailAddress;
	private String phoneNumber;
	private String attention;
	
	public Customer(String name, String address,
			String emailAddress, String phoneNumber, String attention) {
		super();
		this.name = name;
		this.address = address;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.attention = attention;
	}
	
	public Customer() {}
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAttention() {
		return attention;
	}
	public void setAttention(String attention) {
		this.attention = attention;
	}
	
	public String toJSONString() {
		return "{"
				+ "'customerId':'"+this.customerId+"', "
				+ "'name':'"+this.name+"', "
				+ "'address':'"+this.address+"', "
				+ "'emailAddress':'"+this.emailAddress+"', "
				+ "'phoneNumber':'"+this.phoneNumber+"', "
				+ "'attention':'"+this.attention+"'"
			+ "}";
	}
}
