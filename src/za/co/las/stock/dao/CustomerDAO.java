package za.co.las.stock.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import za.co.las.stock.object.Customer;

public class CustomerDAO extends AbstractDAO {
	
	public int updateCustomer(int customerId, Customer newCustomer) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("update las_stock.customer set customer_name = ?, customer_address = ?, customer_email_address = ?, customer_phone_number = ?, customer_attention = ? where customer_id = ?");
			
			statement.setString(1, newCustomer.getName());
			statement.setString(2, newCustomer.getAddress());
			statement.setString(3, newCustomer.getEmailAddress());
			statement.setString(4, newCustomer.getPhoneNumber());
			statement.setString(5, newCustomer.getAttention());
			statement.setInt(6, customerId);
			
			int result = statement.executeUpdate();
			
			return result;
		}
		catch (Exception e) {
			closeStatement(statement);
			closeConnection(connection);
			e.printStackTrace();
		}
		return -1;
	}
	
	public int insertCustomer(Customer newCustomer) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("insert into las_stock.customer (customer_name, customer_address, customer_email_address, customer_phone_number, customer_attention) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, newCustomer.getName());
			statement.setString(2, newCustomer.getAddress());
			statement.setString(3, newCustomer.getEmailAddress());
			statement.setString(4, newCustomer.getPhoneNumber());
			statement.setString(5, newCustomer.getAttention());
			
			statement.execute();
			
			ResultSet rs = statement.getGeneratedKeys();
			int customerId = -1;
			
			while (rs.next()) {
				customerId = rs.getInt(1);
			}
			
			return customerId;
		}
		catch (Exception e) {
			closeStatement(statement);
			closeConnection(connection);
			e.printStackTrace();
		}
		return -1;
	}
	
	public int deleteCustomer(int customerId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("delete from las_stock.customer where customer_id = ?");

			statement.setInt(1, customerId);
			
			int result = statement.executeUpdate();
			
			return result;
		}
		catch (Exception e) {
			closeStatement(statement);
			closeConnection(connection);
			e.printStackTrace();
		}
		return -1;
	}
	
	public ArrayList<Customer> getAllCustomers() {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<Customer> customerItems = new ArrayList<Customer>();
		
		try {
			statement = connection.prepareStatement("select customer_id, customer_name, customer_address, customer_email_address, customer_phone_number, customer_attention from las_stock.customer");

			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				Customer customer = new Customer();
				customer.setAddress(resultSet.getString("customer_address"));
				customer.setAttention(resultSet.getString("customer_attention"));
				customer.setCustomerId(resultSet.getInt("customer_id"));
				customer.setEmailAddress(resultSet.getString("customer_email_address"));
				customer.setName(resultSet.getString("customer_name"));
				customer.setPhoneNumber(resultSet.getString("customer_phone_number"));
				
				customerItems.add(customer);
			}
		}
		catch (Exception e) {
			closeResultSet(resultSet);
			closeStatement(statement);
			closeConnection(connection);
			e.printStackTrace();
		}
		return customerItems;
	}
	
	public Customer getCustomer(int customerId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			statement = connection.prepareStatement("select customer_id, customer_name, customer_address, customer_email_address, customer_phone_number, customer_attention from las_stock.customer where customer_id = ?");
			statement.setInt(1, customerId);
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				Customer customer = new Customer();
				customer.setAddress(resultSet.getString("customer_address"));
				customer.setAttention(resultSet.getString("customer_attention"));
				customer.setCustomerId(resultSet.getInt("customer_id"));
				customer.setEmailAddress(resultSet.getString("customer_email_address"));
				customer.setName(resultSet.getString("customer_name"));
				customer.setPhoneNumber(resultSet.getString("customer_phone_number"));
				
				return customer;
			}
		}
		catch (Exception e) {
			closeResultSet(resultSet);
			closeStatement(statement);
			closeConnection(connection);
			e.printStackTrace();
		}
		return null;
	}
}
