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

	public int createQuotation(String customerAddress, String customerAttention, String customerEmailAddress, String customerName, String customerPhoneNumber, ArrayList<String> stockItemIds, ArrayList<TempAccessory> accessories, String notes, String delivery, String installation, String warranty, String variation, String validity, String date, int userId, String serialNumber, double pricing, String rate, InstallationLocation location) {
		int customerId = customerService.insertCustomer(customerAddress, customerAttention, customerEmailAddress, customerName, customerPhoneNumber);
		
		return quotationDAO.insertQuotation(customerId, stockItemIds, accessories, notes, delivery, installation, warranty, variation, validity, date, userId, serialNumber, pricing, rate, location);	
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
		return quotationDAO.updateQuotationStock(quotationId,2);
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
