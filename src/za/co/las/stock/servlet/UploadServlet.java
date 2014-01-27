package za.co.las.stock.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import za.co.las.stock.constants.StockConstants;
import za.co.las.stock.services.StockService;

public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StockService stockService = new StockService();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String outcome = "";
			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
			byte[] stockImageBytes = null;
			String action = "";
			int stockId = 0;
			for (FileItem item : items) {
	            if (item.isFormField()) {
	            	//this is a "regular form field"...
	                String fieldname = item.getFieldName();
	                String fieldvalue = item.getString();
	                if (fieldname.equalsIgnoreCase(StockConstants.ACTION)){
	                	action = fieldvalue;
	                }
	                if (fieldname.equalsIgnoreCase(StockConstants.STOCK_ID)){
	                	stockId = Integer.parseInt(fieldvalue);
	                }
	            } else {
	            	//this is a file, so deal it as such...
	            	stockImageBytes = IOUtils.toByteArray(item.getInputStream());
			    }
			}
			//now we have all the details, so we can start dealing with the uploads...
			if (action.equalsIgnoreCase(StockConstants.IMAGE_UPLOAD)) {
				//this is a template upload...
				int result = stockService.insertStockImage(stockId, stockImageBytes);
				outcome = ""+result;
			}
			
			resp.setContentType("text/plain");
			PrintWriter printWriter = new PrintWriter(resp.getOutputStream());
			try {
				printWriter.print(outcome);
				printWriter.flush();
			} finally {
				printWriter.close();
			}
		}
		catch (FileUploadException fue) {
			fue.printStackTrace();
		}
	}
}
