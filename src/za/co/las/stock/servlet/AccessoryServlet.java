package za.co.las.stock.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import za.co.las.stock.constants.StockConstants;
import za.co.las.stock.services.AccessoryService;
import za.co.las.stock.services.UtilityService;

public class AccessoryServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UtilityService utilityService = new UtilityService();
	private AccessoryService accessoryService = new AccessoryService();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String callBack = req.getParameter("callback");
		String serverMethod = req.getParameter("serverMethod");
		String accessoryId = req.getParameter("accessoryId");
		String accessoryManufacturer = req.getParameter("accessoryManufacturer");
		
		PrintWriter out = resp.getWriter();
		String outputMessage = callBack+"(";
		
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_ACCESSORY_MANUFACTURERS)) {
			outputMessage = outputMessage + utilityService.convertListOfStockManufacturersToJSONString(accessoryService.getAccessoryManufacturers());
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_ACCESSORY_IEMS_FOR_ACCESSORY_MANUFACTURER)) {
			outputMessage = outputMessage + utilityService.convertListOfAccessoryToJSONString(accessoryService.getAccessoryItemsFromManufacturer(accessoryManufacturer));
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_ACCESSORY_FOR_ACCESSORY_ID)) {
			outputMessage = outputMessage + (accessoryService.getAccessoryItemForAccessoryId(Integer.parseInt(accessoryId)).toJSONString());
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_AVAILABLE_ACCESSORY_FOR_ACCESSORY_ID)) {
			outputMessage = outputMessage + (accessoryService.getAvailableStockItemForStockId(Integer.parseInt(accessoryId)).toJSONString());
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.ADD_SERIAL_NUMBER)) {
			String serialNumber = req.getParameter("serialNumber");
			int result = accessoryService.addSerialNumberToAccessoryId(serialNumber, Integer.parseInt(accessoryId));
			outputMessage = outputMessage + utilityService.buildResponseMessage(result,"");
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.DELETE_SERIAL_NUMBER)) {
			String serialNumber = req.getParameter("serialNumber");
			int result = accessoryService.deleteSerialNumberFromAccessoryId(serialNumber, Integer.parseInt(accessoryId));
			outputMessage = outputMessage + utilityService.buildResponseMessage(result,"");
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.SAVE_ACCESSORY_ITEM)) {
			
			String pricing = req.getParameter("pricing");
			String accessoryCode = req.getParameter("accessoryCode");
			String description = req.getParameter("description");
			String model = req.getParameter("accessoryModel");
			int result = 0;
			if (accessoryId == null) {		
				result = accessoryService.createAccessoryItem(Double.parseDouble(pricing), accessoryManufacturer, accessoryCode, description, model);
			}
			else {
				result = accessoryService.updateAccessoryItem(Integer.parseInt(accessoryId), Double.parseDouble(pricing), accessoryManufacturer, accessoryCode, description, model);
			}		
			
			if (result == 1)
				outputMessage = outputMessage + utilityService.buildResponseMessage(result,"");
			else
				outputMessage = outputMessage + utilityService.buildResponseMessage(result,"Error saving the accessory - please check that all fields are completed correctly");
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.DELETE_ACCESSORY_ITEM)) {
			int result = accessoryService.deleteAccessoryItem(Integer.parseInt(accessoryId));
			if (result == 1)
				outputMessage = outputMessage + utilityService.buildResponseMessage(result,"");
			else
				outputMessage = outputMessage + utilityService.buildResponseMessage(result,"Error deleting accessory item.");
		}
		
		outputMessage = outputMessage + ");";
		System.err.println(outputMessage);
		out.write(outputMessage);
		out.flush();
		out.close();
	}

}
