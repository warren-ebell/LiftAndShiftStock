package za.co.las.stock.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import za.co.las.stock.object.Quotation;
import za.co.las.stock.object.StockImage;
import za.co.las.stock.object.User;
import za.co.las.stock.services.DocumentService;
import za.co.las.stock.services.QuotationService;
import za.co.las.stock.services.StockService;
import za.co.las.stock.services.UserService;

public class DocumentServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private QuotationService quotationService = new QuotationService();
	private DocumentService documentService = new DocumentService();
	private StockService stockService = new StockService();
	private UserService userService = new UserService();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int quotationId = Integer.parseInt(req.getParameter("quotationId"));
		Quotation quote = quotationService.getQuotation(quotationId);
		User user = userService.getUserForId(quote.getUserId());
		//StockImage stockImage = stockService.getStockImageForStockId(quote.getQuotationLineItems().get(0).getStockId());
		byte[] documentBytes = documentService.getQuoteFromQuoteUserAndStockImage(quote, user);
		resp.setContentType("application/pdf");
		resp.addHeader("Content-Disposition", "inline; filename=\"Quotation_"+quote.getQuotationId()+"_"+System.currentTimeMillis()+".pdf\"");
		resp.setHeader("Content-Length", ""+documentBytes.length);
		OutputStream os = resp.getOutputStream();
		os.write(documentBytes);
        os.flush();
	}
}
