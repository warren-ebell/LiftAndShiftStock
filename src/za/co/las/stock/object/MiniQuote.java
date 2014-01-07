package za.co.las.stock.object;

public class MiniQuote {
	private int quotationId;
	private String customerName;
	private String quotationDate;
	
	public int getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(int quotationId) {
		this.quotationId = quotationId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getQuotationDate() {
		return quotationDate;
	}
	public void setQuotationDate(String quotationDate) {
		this.quotationDate = quotationDate;
	}
	
	public String toJSONString() {
		String dateString = this.quotationDate.substring(0, 4)+"/"+this.quotationDate.substring(4, 6)+"/"+this.quotationDate.substring(6, 8)+"";
		return "{"
				+ "'quotationId':'"+this.quotationId+"', "
				+ "'customerName':'"+this.customerName+"', "
				+ "'quotationDate':'"+dateString+"' "
			+ "}";
	}
}
