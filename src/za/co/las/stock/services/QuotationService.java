package za.co.las.stock.services;

import java.util.ArrayList;

import za.co.las.stock.constants.StockConstants;
import za.co.las.stock.dao.QuotationDAO;
import za.co.las.stock.object.Defaults;
import za.co.las.stock.object.InstallationLocation;
import za.co.las.stock.object.MiniQuote;
import za.co.las.stock.object.Quotation;
import za.co.las.stock.object.TempAccessory;

public class QuotationService {
	
	private QuotationDAO quotationDAO = new QuotationDAO();
	private CustomerService customerService = new CustomerService();
	private MailService mailService = new MailService();

	public int createQuotation(String customerAddress, String customerAttention, String customerEmailAddress, String customerName, String customerPhoneNumber, ArrayList<String> stockItemIds, ArrayList<TempAccessory> accessories, String notes, String delivery, String installation, String warranty, String variation, String validity, String date, int userId, String serialNumber, double pricing, String rate, InstallationLocation location, int companyId, String passedCustomerId) {
		
		int customerId;
		if (customerName.length() > 0) {
			customerId = customerService.insertCustomer(customerAddress, customerAttention, customerEmailAddress, customerName, customerPhoneNumber);
		}
		else {
			customerId = Integer.parseInt(passedCustomerId);
		}		
		return quotationDAO.insertQuotation(customerId, stockItemIds, accessories, notes, delivery, installation, warranty, variation, validity, date, userId, serialNumber, pricing, rate, location, companyId);	
	}
	
	public Quotation getQuotation(int quotationId) {
		return quotationDAO.getQuotationForQuotationId(quotationId);
	}
	
	public ArrayList<MiniQuote> getQuotationHistory() {
		return quotationDAO.getquoteHistoryList();
	}
	
	public Quotation poulateQuoteWithDefaults(Quotation quote, int used) {
		return quotationDAO.populateQuoteWithDefaults(quote, used);
	}
	
	public ArrayList<MiniQuote> getMiniQuotes() {
		return quotationDAO.getquoteHistoryList();
	}
	
	public int declineQuote(int quotationId) {
		return quotationDAO.updateQuotationStock(quotationId, 0);
	}
	
	public int acceptQuote(int quotationId) {
		int acceptResult = 0;
		// first need to update the quote
		acceptResult = quotationDAO.updateQuotationStock(quotationId,2);
		// now need to send the notification mail to john
		try {
			Quotation quote = quotationDAO.getQuotationForQuotationId(quotationId);
			String companyName = "Lift and Shift";
			if (quote.getCompanyId() == 2) 
				companyName = "Bowman Cranes";
			String message = "<p>Dear John</p>"+
							 "<p>Please find attached the quotation that has been accepted. Click this link to view the quote (<a href'"+StockConstants.SERVER_URL+"/LiftAndShiftStock/document?quotationId="+quotationId+"'>View Quote</a>).</p>"+
							 "<p>The quote is for "+quote.getCustomer().getName()+" and was quoted from "+companyName+"</p>"+
							 "<p>"+companyName+" Stock System</p>";
			acceptResult = mailService.sendNotificationEmail("johnhb@liftandshift.co.za", message);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return acceptResult;
	}
	
	public int completeQuote(int quotationId) {
		return quotationDAO.updateQuotationStock(quotationId,1);
	}
	
	public String acceptQuoteEMail(int quotationId) {
		int result = quotationDAO.updateQuotationStock(quotationId,2);
		if (result == 1) {
			return StockConstants.QUOTE_ACCEPT_HTML_SUCCESS;
		}
		else {
			return StockConstants.QUOTE_ACCEPT_HTML_FAIL;
		}
	}
	
	public Defaults getQuoteDefaults() {
		return quotationDAO.getDefaults();
	}
}
