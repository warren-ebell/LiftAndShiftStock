package za.co.las.stock.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import za.co.las.stock.object.Stock;
import za.co.las.stock.object.User;
import za.co.las.stock.services.CurrencyService;
import za.co.las.stock.services.QuotationService;
import za.co.las.stock.services.StockService;
import za.co.las.stock.services.UserService;

public class TestServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();
	private StockService stockService = new StockService();
	private QuotationService quoteService = new QuotationService();
	private CurrencyService currencyService = new CurrencyService();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*System.err.println("User Service Test run...");
		//1. insert a user...
		int userId = userService.insertUser(0, "warren", "password", "Warren Ebell", "warren@mail.com","0114630929", 1);
		//2. update user...
		int result = userService.updateUser(userId, 1, "warren_ebell", "password_again","Warren Ebell 2", "warren@new.mail.com","0116789012",1);
		//3. retrieve all users...
		ArrayList<User> userList = userService.getAllUsers();
		//4. validateUser - positive
		User user = userService.validateUser("warren_ebell", "password_again");
		//5. Validate User - negetive
		User user2 = userService.validateUser("warren", "password");
		System.err.println("User Service Test run complete...");
		System.err.println("Stock Service Test run...");
		//1. insert a stock item...
		int stockId = stockService.createStockItem("modelName", 10.00, "stockCategory", "stockCode", "technicalSpecs", "description");
		//2. update stock item
		int stockResult = stockService.updateStockItem(stockId, "modelName2", 120.00, "stockCategory2", "stockCode2", "technicalSpecs2", "description");
		//3. retrieve stock categories...
		ArrayList<String> categoryList = stockService.getStockCategories();
		//4. retieve stock for category...
		ArrayList<Stock> stockList = stockService.getStockItemsForStockCategories("stockCategory2");
		//5. delete stock item
		//int deleteResult = stockService.deleteStockItem(stockId);
		System.err.println("Stock Service Test run complete...");
		System.err.println("Quote Service Test run...");
		ArrayList<String> stock = new ArrayList<String>();
		stock.add("1");
		
		ArrayList<String> optional = null;
		
		int quoteId = quoteService.createQuotation("1 Main Road somewhere", "Warren Ebell", "warren.ebell@gmail.com", "Warren Inc", "0114658907", stock, optional, "notes", "delivery", "installation", "warranty", "variation", "validity", "20130102",1,"", "");
		System.err.println("Quote Service Test run complete...");*/
		
		double currency = currencyService.converstionRateForEUR();
		System.err.println(currency);
	}

}
