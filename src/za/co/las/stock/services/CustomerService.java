package za.co.las.stock.services;

import java.util.ArrayList;

import za.co.las.stock.dao.CustomerDAO;
import za.co.las.stock.object.Customer;

public class CustomerService {
	
	private CustomerDAO customerDAO = new CustomerDAO();
	
	public int deleteCustomer(int customerId) {
		return customerDAO.deleteCustomer(customerId);
	}
	
	public int insertCustomer(String address, String attention, String emailAddress, String name, String phoneNumber) {
		Customer customer = new Customer();
		customer.setAddress(address);
		customer.setAttention(attention);
		customer.setEmailAddress(emailAddress);
		customer.setName(name);
		customer.setPhoneNumber(phoneNumber);
		return customerDAO.insertCustomer(customer);
	}

	public int updateCustomer(int customerId, String address, String attention, String emailAddress, String name, String phoneNumber) {
		Customer customer = new Customer();
		customer.setAddress(address);
		customer.setAttention(attention);
		customer.setEmailAddress(emailAddress);
		customer.setName(name);
		customer.setPhoneNumber(phoneNumber);
		return customerDAO.updateCustomer(customerId, customer);
	}
	
	public ArrayList<Customer> getAllCustomers() {
		ArrayList<Customer> customerList = new ArrayList<Customer>();
		customerList = customerDAO.getAllCustomers();
		Customer temp = new Customer("","","","","");
		customerList.add(0, temp);
		return customerList;
	}
	
	public Customer getCustomerForCustomerId(int customerId) {
		return customerDAO.getCustomer(customerId);
	}
}
