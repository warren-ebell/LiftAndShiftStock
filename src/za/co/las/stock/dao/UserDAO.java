package za.co.las.stock.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import za.co.las.stock.object.User;

public class UserDAO extends AbstractDAO {
	
	public User validateUser(String userName, String password) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			statement = connection.prepareStatement("select user_id, user_name, user_password, user_admin, user_display_name, user_email_address, user_contact_number, user_enabled from las_stock.user where user_name = ? and user_password = ?");
			
			statement.setString(1, userName);
			statement.setString(2, password);
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				//there should only be one of these - username will have to be unique...
				User user = new User();
				user.setUserId(resultSet.getInt("user_id"));
				user.setUserName(resultSet.getString("user_name"));
				user.setUserPassword(resultSet.getString("user_password"));
				user.setAdmin(resultSet.getInt("user_admin"));
				user.setEnabled(resultSet.getInt("user_enabled"));
				user.setDisplayName(resultSet.getString("user_display_name"));
				user.setEmailAddress(resultSet.getString("user_email_address"));
				user.setContactNumber(resultSet.getString("user_contact_number"));
				
				return user;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeResultSet(resultSet);
			closeStatement(statement);
			closeConnection(connection);
		}
		return null;
	}
	
	public int updateUser(int userId, User newUser) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("update las_stock.user set user_name = ?, user_password = ?, user_admin = ?, user_email_address = ?, user_display_name = ?, user_contact_number = ?, user_enabled = ? where user_id = ?");
			
			statement.setString(1, newUser.getUserName());
			statement.setString(2, newUser.getUserPassword());
			statement.setInt(3, newUser.getAdmin());
			statement.setString(4, newUser.getEmailAddress());
			statement.setString(5, newUser.getDisplayName());
			statement.setString(6, newUser.getContactNumber());
			statement.setInt(7, newUser.getEnabled());
			statement.setInt(8, userId);
			
			int result = statement.executeUpdate();
			
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeStatement(statement);
			closeConnection(connection);
		}
		return -1;
	}
	
	public int insertUser(User newUser) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("insert into las_stock.user (user_name, user_password, user_admin, user_email_address, user_display_name, user_contact_number, user_enabled) values (?, ?, ?, ?, ?, ?, ?)");
			
			statement.setString(1, newUser.getUserName());
			statement.setString(2, newUser.getUserPassword());
			statement.setInt(3, newUser.getAdmin());
			statement.setString(4, newUser.getEmailAddress());
			statement.setString(5, newUser.getDisplayName());
			statement.setString(6, newUser.getContactNumber());
			statement.setInt(7, newUser.getEnabled());
			
			int result = statement.executeUpdate();
			
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeStatement(statement);
			closeConnection(connection);
		}
		return -1;
	}
	
	public int deleteUser(int userId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("update las_stock.user set user_enabled = 0 where user_id = ?");

			statement.setInt(1, userId);
			
			int result = statement.executeUpdate();
			
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeStatement(statement);
			closeConnection(connection);
		}
		return -1;
	}
	
	public ArrayList<User> getAllUsers() {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<User> userList = new ArrayList<User>();
		
		try {
			statement = connection.prepareStatement("select user_id, user_name, user_password, user_admin, user_display_name, user_email_address, user_contact_number, user_enabled from las_stock.user");
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				User user = new User();
				user.setUserId(resultSet.getInt("user_id"));
				user.setUserName(resultSet.getString("user_name"));
				user.setUserPassword(resultSet.getString("user_password"));
				user.setAdmin(resultSet.getInt("user_admin"));
				user.setDisplayName(resultSet.getString("user_display_name"));
				user.setEmailAddress(resultSet.getString("user_email_address"));
				user.setContactNumber(resultSet.getString("user_contact_number"));
				user.setEnabled(resultSet.getInt("user_enabled"));
				
				userList.add(user);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeResultSet(resultSet);
			closeStatement(statement);
			closeConnection(connection);
		}
		return userList;
	}
	
	public User getUserForUserId(int userId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareStatement("select user_id, user_name, user_password, user_admin, user_display_name, user_email_address, user_contact_number, user_enabled from las_stock.user where user_id = ?");
			statement.setInt(1, userId);
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				User user = new User();
				user.setUserId(resultSet.getInt("user_id"));
				user.setUserName(resultSet.getString("user_name"));
				user.setUserPassword(resultSet.getString("user_password"));
				user.setAdmin(resultSet.getInt("user_admin"));
				user.setDisplayName(resultSet.getString("user_display_name"));
				user.setEmailAddress(resultSet.getString("user_email_address"));
				user.setContactNumber(resultSet.getString("user_contact_number"));
				user.setEnabled(resultSet.getInt("user_enabled"));
				
				return user;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeResultSet(resultSet);
			closeStatement(statement);
			closeConnection(connection);			
		}
		return null;
	}
}
