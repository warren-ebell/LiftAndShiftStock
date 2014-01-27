package za.co.las.stock.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import za.co.las.stock.constants.StockConstants;
import za.co.las.stock.object.InstallationLocation;
import za.co.las.stock.object.Quotation;
import za.co.las.stock.object.StockImage;
import za.co.las.stock.object.TempAccessory;
import za.co.las.stock.object.User;
import za.co.las.stock.services.CurrencyService;
import za.co.las.stock.services.CustomerService;
import za.co.las.stock.services.DocumentService;
import za.co.las.stock.services.MailService;
import za.co.las.stock.services.QuotationService;
import za.co.las.stock.services.StockService;
import za.co.las.stock.services.UserService;
import za.co.las.stock.services.UtilityService;

public class QuoteServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UtilityService utilityService = new UtilityService();
	private StockService stockService = new StockService();
	private QuotationService quotationService = new QuotationService();
	private UserService userService = new UserService();
	private CustomerService customerService = new CustomerService();
	private DocumentService documentService = new DocumentService();
	private MailService mailService = new MailService();
	private CurrencyService currencyService = new CurrencyService();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String callBack = req.getParameter("callback");
		String serverMethod = req.getParameter("serverMethod");
		String stockId = req.getParameter("stockId");
		String stockManufacturer = req.getParameter("stockManufacturer");
		String stockModel = req.getParameter("stockModel");
		
		PrintWriter out = resp.getWriter();
		String outputMessage = callBack+"(";
		
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_QUOTE_DEFAULTS)) {
			outputMessage = outputMessage + quotationService.getQuoteDefaults().toJSONString();
			outputMessage = outputMessage + ");";
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_ALL_CUSTOMERS)) {
			outputMessage = outputMessage + utilityService.convertListOfCustomersToJSONString(customerService.getAllCustomers());
			outputMessage = outputMessage + ");";
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.ACCEPT_QUOTE_EMAIL)) {
			int quotationId = Integer.parseInt(req.getParameter("quotationId"));
			outputMessage = quotationService.acceptQuoteEMail(quotationId);
		}	
		if (serverMethod.equalsIgnoreCase(StockConstants.ACCEPT_QUOTE)) {
			int quotationId = Integer.parseInt(req.getParameter("quotationId"));
			int result = quotationService.acceptQuote(quotationId);
			if (result == 1)
				outputMessage = outputMessage + "{'result':'1','message':''}";
			else
				outputMessage = outputMessage + "{'result':'0','message':'Quote update failed.'}";
			outputMessage = outputMessage + ");";
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.COMPLETE_QUOTE)) {
			int quotationId = Integer.parseInt(req.getParameter("quotationId"));
			int result = quotationService.completeQuote(quotationId);
			if (result == 1)
				outputMessage = outputMessage + "{'result':'1','message':''}";
			else
				outputMessage = outputMessage + "{'result':'0','message':'Quote update failed.'}";
			outputMessage = outputMessage + ");";
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.DECLINE_QUOTE)) {
			int quotationId = Integer.parseInt(req.getParameter("quotationId"));
			int result = quotationService.declineQuote(quotationId);
			if (result == 1)
				outputMessage = outputMessage + "{'result':'1','message':''}";
			else
				outputMessage = outputMessage + "{'result':'0','message':'Quote update failed.'}";
			outputMessage = outputMessage + ");";
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_STOCK_MANUFACTURERS)) {
			outputMessage = outputMessage + utilityService.convertListOfStockManufacturersToJSONString(stockService.getStockManufacturers());
			outputMessage = outputMessage + ");";
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_STOCK_MODELS_FOR_MANUFACTURER)) {
			outputMessage = outputMessage + utilityService.convertListOfStockModelsToJSONString(stockService.getStockModelsForManufacturer(stockManufacturer));
			outputMessage = outputMessage + ");";
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_STOCK_IEMS_FOR_STOCK_MANUFACTURER_AND_MODEL)) {
			outputMessage = outputMessage + utilityService.convertListOfStockToJSONString(stockService.getStockItemsFromManufacturerAndModel(stockManufacturer, stockModel));
			outputMessage = outputMessage + ");";
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_STOCK_FOR_STOCK_ID)) {
			outputMessage = outputMessage + (stockService.getStockItemForStockId(Integer.parseInt(stockId)).toJSONString());
			outputMessage = outputMessage + ");";
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_MINI_QUOTES)) {
			outputMessage = outputMessage + utilityService.convertListOfMiniQuotesToJSONString(quotationService.getMiniQuotes());
			outputMessage = outputMessage + ");";
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.SAVE_QUOTE)) {
			Quotation quote = new Quotation();
			int usedItem = Integer.parseInt(req.getParameter("usedItem"));
			quote = quotationService.poulateQuoteWithDefaults(quote, usedItem);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			
			int userId = Integer.parseInt(req.getParameter("userId")); 
			String customerName = req.getParameter("name");
			String customerAddress = null, customerAttention = null, customerEmailAddress = null, customerPhoneNumber = null;
			if (customerName.length() > 0) {
				customerAddress = req.getParameter("address");
				customerAttention = req.getParameter("attention");
				customerEmailAddress = req.getParameter("emailAddress");			
				customerPhoneNumber = req.getParameter("phoneNumber");
			}
			
			String serialNumber = req.getParameter("serialNumber");
			String pricingStr = req.getParameter("pricing");
			String accessories = req.getParameter("accessories");
			String notes = req.getParameter("notes");
			String delivery = req.getParameter("delivery");
			String installation = req.getParameter("installation");
			String installationLocation = req.getParameter("installationLocation");
			int companyId = Integer.parseInt(req.getParameter("companyId"));
			String customerId = req.getParameter("customerId");
			
			quote.setNotes(notes);
			quote.setDelivery(delivery);
			quote.setInstallation(installation);
			
			double factor = currencyService.converstionRateForEUR();
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			String rate = df.format(factor);
			double newRate = Double.parseDouble(rate);
			
			double pricing = Double.parseDouble(pricingStr) * newRate;
			
			ArrayList<String> stockItemIds = new ArrayList<String>();
			stockItemIds.add(stockId);
			
			ArrayList<TempAccessory> tempAccList = utilityService.convertAccessoryJSONStringToListWithPricingFactor(accessories, newRate);
			InstallationLocation location = utilityService.convertInstallationLocationJSONStringWithPricingFactor(installationLocation, newRate);
			
			outputMessage = outputMessage + "{'quoteId':'"+quotationService.createQuotation(customerAddress, customerAttention, customerEmailAddress, customerName, customerPhoneNumber, stockItemIds, tempAccList, quote.getNotes(), quote.getDelivery(), quote.getInstallation(), quote.getWarranty(), quote.getVariation(), quote.getValidity(), sdf.format(date), userId, serialNumber, pricing, rate, location, companyId, customerId)+"'}";
			outputMessage = outputMessage + ");";
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.SEND_EMAIL)) {
			int quotationId = Integer.parseInt(req.getParameter("quotationId"));
			Quotation quote = quotationService.getQuotation(quotationId);
			User user = userService.getUserForId(quote.getUserId());
			StockImage stockImage = stockService.getStockImageForStockId(quote.getQuotationLineItems().get(0).getStockId());
			byte[] documentBytes = documentService.getQuoteFromQuoteUserAndStockImage(quote, user, stockImage);
			String bodyText = "<p>Dear "+quote.getCustomer().getAttention()+"</p>"+
				"<p>Please find attached the quotation requested. Click this link to accept the quote (<a href='"+StockConstants.SERVER_URL+"/LiftAndShiftStock/quote?serverMethod=acceptQuoteEmail&quotationId="+quotationId+"'>Accept Quote</a>), or comntact the sales team.</p>"+
				"<p>Please do not reply to the email address this mail comes from, but rather the email address below.</p>"+
				"<br/>"+
				"Kind Regards<br/>"+
				user.getDisplayName()+"<br/>"+
				user.getEmailAddress()+"<br/>"+
				user.getContactNumber()+"<br/>";
			try {
				outputMessage = outputMessage + "{'result':'"+mailService.sendEmailWithQuote(documentBytes, quote.getCustomer().getEmailAddress(), bodyText)+"'}";
			}
			catch (Exception e) {
				e.printStackTrace();
				outputMessage = outputMessage + "{'result':'0','message':'"+e.getMessage()+"'}";
			}
			outputMessage = outputMessage + ");";
		}
		
		System.err.println(outputMessage);
		out.write(outputMessage);
		out.flush();
		out.close();
	}
}
