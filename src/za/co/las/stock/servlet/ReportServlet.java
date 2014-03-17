package za.co.las.stock.servlet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import za.co.las.stock.constants.StockConstants;
import za.co.las.stock.object.User;
import za.co.las.stock.services.StockService;
import za.co.las.stock.services.UserService;
import za.co.las.stock.services.UtilityService;

public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StockService stockService = new StockService();
	private UtilityService utilityService = new UtilityService();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String callBack = req.getParameter("callback");
		String serverMethod = req.getParameter("serverMethod");
		
		System.out.println(callBack);
		System.out.println(serverMethod);
		
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_ALL_AVAILABLE_STOCK_FOR_REPORT)) {
			PrintWriter out = resp.getWriter();
			String outputMessage = callBack+"(";
			outputMessage = outputMessage + utilityService.convertListOfReportStockToJSONString(stockService.getAllAvailableStockForReport());
			outputMessage = outputMessage + ");";
			System.err.println(outputMessage);
			out.write(outputMessage);
			out.flush();
			out.close();
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_ALL_SOLD_STOCK_FOR_REPORT)) {
			PrintWriter out = resp.getWriter();
			String outputMessage = callBack+"(";
			outputMessage = outputMessage + utilityService.convertListOfReportStockToJSONString(stockService.getAllSoldStockForReport());
			outputMessage = outputMessage + ");";
			System.err.println(outputMessage);
			out.write(outputMessage);
			out.flush();
			out.close();
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_ALL_STOCK_FOR_REPORT)) {
			PrintWriter out = resp.getWriter();
			String outputMessage = callBack+"(";
			outputMessage = outputMessage + utilityService.convertListOfReportAllStockToJSONString(stockService.getAllStockForReport());
			outputMessage = outputMessage + ");";
			System.err.println(outputMessage);
			out.write(outputMessage);
			out.flush();
			out.close();
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.EXPORT_ALL_AVAILABLE_STOCK_FOR_REPORT)) {
			ServletOutputStream output = resp.getOutputStream();
			resp.setHeader("Content-Type", "text/csv");
		    resp.setHeader("Content-Disposition", "attachment;filename=\"StockList.csv\"");
		    byte[] fileBytes = utilityService.generateExportReport(stockService.getAllAvailableStockForReport());
		    output.write(fileBytes);
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.EXPORT_ALL_STOCK_FOR_REPORT)) {
			ServletOutputStream output = resp.getOutputStream();
			resp.setHeader("Content-Type", "text/csv");
		    resp.setHeader("Content-Disposition", "attachment;filename=\"StockList.csv\"");
		    byte[] fileBytes = utilityService.generateAllExportReport(stockService.getAllStockForReport());
		    output.write(fileBytes);
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.EXPORT_ALL_SOLD_STOCK_FOR_REPORT)) {
			ServletOutputStream output = resp.getOutputStream();
			resp.setHeader("Content-Type", "text/csv");
		    resp.setHeader("Content-Disposition", "attachment;filename=\"StockList.csv\"");
		    byte[] fileBytes = utilityService.generateExportReport(stockService.getAllAvailableStockForReport());
		    output.write(fileBytes);
		}
	}
}
