package za.co.las.stock.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import za.co.las.stock.constants.StockConstants;
import za.co.las.stock.services.UserService;
import za.co.las.stock.services.UtilityService;

public class UserServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserService userService = new UserService();
	private UtilityService utilityService = new UtilityService();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String callBack = req.getParameter("callback");
		String userId = req.getParameter("userId");
		String serverMethod = req.getParameter("serverMethod");
		
		PrintWriter out = resp.getWriter();
		String outputMessage = callBack+"(";
		
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_ALL_USERS)) {
			outputMessage = outputMessage + utilityService.convertListOfUsersToJSONString(userService.getAllUsers());
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.GET_USER)) {
			outputMessage = outputMessage + (userService.getUserForId(Integer.parseInt(userId)).toJSONString());
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.DELETE_USER)) {
			int result = userService.deleteUser(Integer.parseInt(userId));
			if (result == 1)
				outputMessage = outputMessage + utilityService.buildResponseMessage(result,"");
			else
				outputMessage = outputMessage + utilityService.buildResponseMessage(result,"Error deleting the user.");
		}
		if (serverMethod.equalsIgnoreCase(StockConstants.SAVE_USER)) {
			int admin = Integer.parseInt(req.getParameter("admin"));
			String userName = req.getParameter("userName");
			String userPassword = req.getParameter("userPassword");
			String displayName = req.getParameter("displayName");
			String emailAddress = req.getParameter("emailAddress");
			String contactNumber = req.getParameter("contactNumber");
			int enabled = Integer.parseInt(req.getParameter("enabled"));
			int result = 0;
			if (userId == null) {
				result = userService.insertUser(admin, userName, userPassword, displayName, emailAddress, contactNumber, enabled);
			}
			else {
				result = userService.updateUser(Integer.parseInt(userId), admin, userName, userPassword, displayName, emailAddress, contactNumber, enabled);
			}
			if (result == 1)
				outputMessage = outputMessage + utilityService.buildResponseMessage(result,"");
			else
				outputMessage = outputMessage + utilityService.buildResponseMessage(result,"Error saving the user - please check that all fields are completed correctly");
		}
		
		outputMessage = outputMessage + ");";
		System.err.println(outputMessage);
		out.write(outputMessage);
		out.flush();
		out.close();
	}
}
