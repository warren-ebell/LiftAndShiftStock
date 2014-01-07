package za.co.las.stock.object;

public class User {
	private int userId;
	private String userName;
	private String userPassword;
	private String displayName;
	private String emailAddress;
	private String contactNumber;
	private int admin;
	private int enabled;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public int getAdmin() {
		return admin;
	}
	public void setAdmin(int admin) {
		this.admin = admin;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}	
	
	public String toJSONString() {
		return "{"
				+ "'userId':'"+this.userId+"',"
				+ "'userName':'"+this.userName+"', "
				+ "'userPassword':'"+this.userPassword+"', "
				+ "'displayName':'"+this.displayName+"', "
				+ "'emailAddress':'"+this.emailAddress+"', "
				+ "'contactNumber':'"+this.contactNumber+"', "
				+ "'admin':'"+this.admin+"', "
				+ "'enabled':'"+this.enabled+"'"
			+ "}";
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
}
