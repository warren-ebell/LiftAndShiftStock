package za.co.las.stock.services;

import java.util.ArrayList;

import za.co.las.stock.dao.UserDAO;
import za.co.las.stock.object.User;

public class UserService {
	
	private UserDAO userDAO = new UserDAO();
	
	public int insertUser(int admin, String userName, String userPassword, String displayName, String emailAddress, String contactNumber, int userEnabled) {
		User user = new User();
		user.setAdmin(admin);
		user.setUserName(userName);
		user.setUserPassword(userPassword);
		user.setDisplayName(displayName);
		user.setEmailAddress(emailAddress);
		user.setContactNumber(contactNumber);
		user.setEnabled(userEnabled);
		
		return userDAO.insertUser(user);
	}
	
	public int deleteUser(int userId) {
		return userDAO.deleteUser(userId);
	}
	
	public int updateUser(int userId, int admin, String userName, String userPassword, String displayName, String emailAddress, String contactNumber, int enabled) {
		User user = new User();
		user.setAdmin(admin);
		user.setUserName(userName);
		user.setUserPassword(userPassword);
		user.setDisplayName(displayName);
		user.setEmailAddress(emailAddress);
		user.setContactNumber(contactNumber);
		user.setEnabled(enabled);
		
		return userDAO.updateUser(userId, user);
	}
	
	public User getUserForId(int userId) {
		return userDAO.getUserForUserId(userId);
	}
	
	public ArrayList<User> getAllUsers() {
		return userDAO.getAllUsers();
	}
	
	public User validateUser(String userName, String password) {
		return userDAO.validateUser(userName, password);
	}

}
