package za.co.las.stock.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import za.co.las.stock.constants.StockConstants;
import za.co.las.stock.services.StockService;
import za.co.las.stock.services.UtilityService;

public class StockServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UtilityService utilityService = new UtilityService();
	private StockService stockService = new StockService();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String callBack = req.getParameter("callback");
		String serverMethod = req.getParameter("serverMethod");
		String stockId = req.getParameter("stockId");
		String stockCategory = req.getParameter("stockCategory");
		
		PrintWriter out = resp.getWriter();
		String outputMessage = callBack+"(";
		
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_STOCK_CATEGORIES)) {
			outputMessage = outputMessage + utilityService.convertListOfStockCategoriesToJSONString(stockService.getStockCategories());
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_STOCK_IEMS_FOR_STOCK_CATEGORY)) {
			outputMessage = outputMessage + utilityService.convertListOfStockToJSONString(stockService.getStockItemsForStockCategories(stockCategory));
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_STOCK_FOR_STOCK_ID)) {
			outputMessage = outputMessage + (stockService.getStockItemForStockId(Integer.parseInt(stockId)).toJSONString());
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_AVAILABLE_STOCK_FOR_STOCK_ID)) {
			outputMessage = outputMessage + (stockService.getAvailableStockItemForStockId(Integer.parseInt(stockId)).toJSONString());
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.ADD_SERIAL_NUMBER)) {
			String serialNumber = req.getParameter("serialNumber");
			int result = stockService.addSerialNumberToStockId(serialNumber, Integer.parseInt(stockId));
			outputMessage = outputMessage + utilityService.buildResponseMessage(result,"");
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.DELETE_SERIAL_NUMBER)) {
			String serialNumber = req.getParameter("serialNumber");
			int result = stockService.deleteSerialNumberFromStockId(serialNumber, Integer.parseInt(stockId));
			outputMessage = outputMessage + utilityService.buildResponseMessage(result,"");
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.SAVE_STOCK_ITEM)) {
			String modelName = req.getParameter("modelName");
			String pricing = req.getParameter("pricing");
			String stockCode = req.getParameter("stockCode");
			String technicalSpecs = req.getParameter("technicalSpecs");
			String description = req.getParameter("description");
			int result = 0;
			if (stockId == null) {		
				result = stockService.createStockItem(modelName, Double.parseDouble(pricing), stockCategory, stockCode, technicalSpecs, description);
			}
			else {
				result = stockService.updateStockItem(Integer.parseInt(stockId), modelName, Double.parseDouble(pricing), stockCategory, stockCode, technicalSpecs, description);
			}		
			
			if (result == 1)
				outputMessage = outputMessage + utilityService.buildResponseMessage(result,"");
			else
				outputMessage = outputMessage + utilityService.buildResponseMessage(result,"Error saving the user - please check that all fields are completed correctly");
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.DELETE_STOCK_ITEM)) {
			int result = stockService.deleteStockItem(Integer.parseInt(stockId));
			if (result == 1)
				outputMessage = outputMessage + utilityService.buildResponseMessage(result,"");
			else
				outputMessage = outputMessage + utilityService.buildResponseMessage(result,"Error deleting stock item.");
		}
		
		outputMessage = outputMessage + ");";
		System.err.println(outputMessage);
		out.write(outputMessage);
		out.flush();
		out.close();
	}

}
