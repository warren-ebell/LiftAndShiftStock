package za.co.las.stock.constants;

public class StockConstants {

	public static final String DO_LOGIN = "doLogin";
	
	public static final String GET_ALL_USERS = "getAllUsers";
	public static final String GET_USER = "getUserForUserId";
	public static final String SAVE_USER = "saveUser";
	public static final String DELETE_USER = "deleteUser";
	
	public static final String GET_STOCK_CATEGORIES = "getStockCategories";
	public static final String GET_STOCK_FOR_STOCK_ID = "getStockForStockId";
	public static final String GET_AVAILABLE_STOCK_FOR_STOCK_ID = "getAvailableStockForStockId";
	public static final String GET_STOCK_IEMS_FOR_STOCK_CATEGORY = "getStockItemsForStockCategory";
	public static final String SAVE_STOCK_ITEM = "saveStockItem";
	public static final String DELETE_STOCK_ITEM = "deleteStockItem";

	public static final String SAVE_QUOTE = "saveQuote";
	public static final String GET_MINI_QUOTES = "getMiniQuotes";
	public static final String SEND_EMAIL = "sendEmail";
	public static final String ACCEPT_QUOTE = "acceptQuote";
	public static final String COMPLETE_QUOTE = "completeQuote";
	public static final String ACCEPT_QUOTE_EMAIL = "acceptQuoteEmail";
	public static final String RETURN_STOCK = "returnStock";
	public static final String DECLINE_QUOTE = "declineQuote";
	public static final String GET_QUOTE_DEFAULTS = "getQuoteDefaults";

	public static final String ADD_SERIAL_NUMBER = "addSerialNumber";
	public static final String DELETE_SERIAL_NUMBER = "deleteSerialNumber";

	public static final String QUOTE_ACCEPT_HTML_SUCCESS = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>Lift and Shift Quote System</title></head><body>	<h4>Quote system Response</h4>    <div>    	<p>That you for submitting your acceptance of the quotation - a sales person will be in contact shortly to make final arrangmeents.</p>    </div></body></html>";
	public static final String QUOTE_ACCEPT_HTML_FAIL = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>Lift and Shift Quote System</title></head><body>	<h4>Quote system Response</h4>    <div>    	<p>That you for submitting your acceptance of the quotation, but there was an error processing this request - please contact the sales team.</p>    </div></body></html>";

	//public static final String SERVER_URL = "http://localhost:8080";
	public static final String SERVER_URL = "http://197.221.7.50:8080";
	
}
