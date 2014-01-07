package za.co.las.stock.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import za.co.las.stock.constants.StockConstants;
import za.co.las.stock.object.User;
import za.co.las.stock.services.UserService;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String callBack = req.getParameter("callback");
		String userName = req.getParameter("username");
		String password = req.getParameter("password");
		String serverMethod = req.getParameter("serverMethod");
		
		System.out.println(callBack);
		System.out.println(userName);
		System.out.println(password);
		System.out.println(serverMethod);
		
		PrintWriter out = resp.getWriter();
		String outputMessage = callBack+"(";
		
		if (serverMethod.equalsIgnoreCase(StockConstants.DO_LOGIN)) {
			User user = userService.validateUser(userName, password);
			if (user != null) {
				outputMessage = outputMessage + user.toJSONString();
			}
		}
		outputMessage = outputMessage + ");";
		System.err.println(outputMessage);
		out.write(outputMessage);
		out.flush();
		out.close();
	}
}
