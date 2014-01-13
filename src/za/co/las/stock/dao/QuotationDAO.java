package za.co.las.stock.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import za.co.las.stock.object.Customer;
import za.co.las.stock.object.Defaults;
import za.co.las.stock.object.MiniQuote;
import za.co.las.stock.object.OptionalExtra;
import za.co.las.stock.object.Quotation;
import za.co.las.stock.object.Stock;

import com.mysql.jdbc.Statement;

public class QuotationDAO extends AbstractDAO {
	
	public Quotation populateQuoteWithDefaults(Quotation quote) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareStatement("select notes, delivery, installation, warranty, variation, validity from las_stock.defaults");
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				quote.setNotes(resultSet.getString("notes"));
				quote.setDelivery(resultSet.getString("delivery"));
				quote.setInstallation(resultSet.getString("installation"));
				quote.setWarranty(resultSet.getString("warranty"));
				quote.setVariation(resultSet.getString("variation"));
				quote.setValidity(resultSet.getString("validity"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeStatement(statement);
			closeResultSet(resultSet);
			closeConnection(connection);
		}
		return quote;
	}
	
	public Defaults getQuoteDefaults() {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Defaults defaults = new Defaults();
		try {
			statement = connection.prepareStatement("select notes, delivery, installation, warranty, variation, validity from las_stock.defaults");
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				defaults.setNotes(resultSet.getString("notes"));
				defaults.setDelivery(resultSet.getString("delivery"));
				defaults.setInstallation(resultSet.getString("installation"));
				defaults.setWarranty(resultSet.getString("warranty"));
				defaults.setVariation(resultSet.getString("variation"));
				defaults.setValidity(resultSet.getString("validity"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeStatement(statement);
			closeResultSet(resultSet);
			closeConnection(connection);
		}
		return defaults;
	}
	
	public int insertQuotation(int customerId, ArrayList<String> stockItemIds, ArrayList<String> optionalExtraItemIds, String notes, String delivery, String installation, String warranty,String variation, String validity, String date, int userId, String serialNumber, double pricing, String rate) {
		Connection connection = getConnection();
		PreparedStatement statement1 = null;
		PreparedStatement statement2 = null;
		PreparedStatement statement3 = null;
		PreparedStatement statement4 = null;
		
		try {
			String status = "Created";
			//1. insert into the quotation table - customerId is the only thing that goes in here...
			statement1 = connection.prepareStatement("insert into las_stock.quotation (customer_id, notes, delivery, installation, warranty, variation, validity, quotation_date, user_id, status, rate) values (?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			statement1.setInt(1, customerId);
			statement1.setString(2, notes);
			statement1.setString(3, delivery);
			statement1.setString(4, installation);
			statement1.setString(5, warranty);
			statement1.setString(6, variation);
			statement1.setString(7, validity);
			statement1.setString(8, date);
			statement1.setInt(9, userId);
			statement1.setString(10, status);
			statement1.setString(11, rate);
			
			statement1.execute();
			
			ResultSet rs = statement1.getGeneratedKeys();
			int quotationId = -1;
			
			while (rs.next()) {
				quotationId = rs.getInt(1);
			}
			
			//2. add the stock items to the quotation...
			statement2 = connection.prepareStatement("insert into las_stock.quotation_line_item (stock_id, quotation_id, serial_number, pricing) values (?,?,?,?)");
			
			if (stockItemIds != null) {
				for (String s:stockItemIds) {
					statement2.setInt(1, Integer.parseInt(s));
					statement2.setInt(2, quotationId);
					statement2.setString(3, serialNumber);
					statement2.setDouble(4, pricing);
				
					statement2.executeUpdate();
				}
			}
			
			//2.1. update the serial number in the stock levels to a status of 1... 
			statement4 = connection.prepareStatement("update las_stock.stock_level set stock_status = 1 where stock_id = ? and serial_number = ?");
			
			if (stockItemIds != null) {
				for (String s:stockItemIds) {
					statement4.setInt(1, Integer.parseInt(s));
					statement4.setString(2, serialNumber);
					
					statement4.executeUpdate();
				}
			}
			
			
			//3. add the optional extra items to the quotation...
			statement3 = connection.prepareStatement("insert into las_stock.quotation_line_item (optional_extra_id, quotation_id) values (?,?)");
			
			if (optionalExtraItemIds != null) {
				for (String oe:optionalExtraItemIds) {
					statement3.setInt(1, Integer.parseInt(oe));
					statement3.setInt(2, quotationId);
					
					statement3.executeUpdate();
				}
			}
			return quotationId;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeStatement(statement1);
			closeStatement(statement2);
			closeStatement(statement3);
			closeStatement(statement4);
			closeConnection(connection);
		}
		return -1;
	}
	
	public int updateQuotationStock(int quotationId, int status) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		PreparedStatement statement1 = null;
		PreparedStatement statement2 = null;
		
		ResultSet resultSet = null;
		try {
			//1. get the quotation information for this quote...
			statement = connection.prepareStatement("select qli.stock_id, qli.serial_number from las_stock.quotation q, las_stock.quotation_line_item qli where q.quotation_id = ? and q.quotation_id = qli.quotation_id and qli.serial_number not in ('null')");
			statement.setInt(1, quotationId);
			
			resultSet = statement.executeQuery();
			int stockId = -1;
			String serialNumber = "";
			while (resultSet.next()) {
				stockId = resultSet.getInt("stock_id");
				serialNumber = resultSet.getString("serial_number");
			}
			
			if (stockId != -1){
				String statusStr = "";
				if (status == 0) {
					statusStr = "Declined";
				}
				if (status == 1) {
					statusStr = "Completed";
				}
				if (status == 2) {
					statusStr = "Accepted";
				}
				
				statement2 = connection.prepareStatement("update las_stock.quotation set status = ? where quotation_id = ?");
				statement2.setString(1, statusStr);
				statement2.setInt(2, quotationId);
				
				int result = statement2.executeUpdate();
				
				//now need to set the stock level...
				//if the status is declined OR competed, then the status needs to be 0...
				int stockLevelStatus = 0;
				if (status == 2)
					stockLevelStatus = 2;
				statement1 = connection.prepareStatement("update las_stock.stock_level set stock_status = ? where stock_id = ? and serial_number = ?");
				
				statement1.setInt(1, stockLevelStatus);
				statement1.setInt(2, stockId);
				statement1.setString(3, serialNumber);
				
				result =  statement1.executeUpdate();
				
				return result;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeResultSet(resultSet);
			closeStatement(statement2);
			closeStatement(statement1);
			closeStatement(statement);
			closeConnection(connection);
		}
		return -1;
	}
	
	public Quotation getQuotationForQuotationId(int quotationId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		PreparedStatement statement1 = null;
		PreparedStatement statement2 = null;
		PreparedStatement statement3 = null;
		
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		Quotation quote = new Quotation();
		quote.setQuotationId(quotationId);
		try {
			//1. get the quotation information for this quote...
			statement = connection.prepareStatement("select quotation_id, notes, delivery, installation, warranty, variation, validity, quotation_date, user_id, rate from las_stock.quotation where quotation_id = ?");
			statement.setInt(1, quotationId);
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				quote.setNotes(resultSet.getString("notes"));
				quote.setDelivery(resultSet.getString("delivery"));
				quote.setInstallation(resultSet.getString("installation"));
				quote.setWarranty(resultSet.getString("warranty"));
				quote.setValidity(resultSet.getString("validity"));
				quote.setVariation(resultSet.getString("variation"));
				quote.setQuotationDate(resultSet.getString("quotation_date"));
				quote.setUserId(resultSet.getInt("user_id"));
				quote.setRate(resultSet.getString("rate"));
			}
			
			//2. get the customer for this quotation...
			statement1 = connection.prepareStatement("select c.customer_id, c.customer_name, c.customer_address, c.customer_email_address, c.customer_phone_number, c.customer_attention " + 
													"from "+ 
													"    las_stock.customer c, las_stock.quotation q "+ 
													"where "+
													"    q.quotation_id = ? "+
													"and "+
													"    c.customer_id = q.customer_id");

			statement1.setInt(1, quotationId);
			
			resultSet1 = statement1.executeQuery();
			
			while (resultSet1.next()) {
				Customer customer = new Customer();
				customer.setAddress(resultSet1.getString("customer_address"));
				customer.setAttention(resultSet1.getString("customer_attention"));
				customer.setCustomerId(resultSet1.getInt("customer_id"));
				customer.setEmailAddress(resultSet1.getString("customer_email_address"));
				customer.setName(resultSet1.getString("customer_name"));
				customer.setPhoneNumber(resultSet1.getString("customer_phone_number"));

				quote.setCustomer(customer);
			}
			
			//3.1. get the line items that are linked to this quote...
			ArrayList<Stock> stockItems = new ArrayList<Stock>();
			statement2 = connection.prepareStatement("select "+
													"    s.stock_id, qli.serial_number, s.stock_code, s.stock_category, s.model_name, qli.pricing, s.technical_specs, s.stock_description "+ 
													"from "+
													"    las_stock.quotation_line_item qli, las_stock.stock s "+
													"where "+ 
													"    qli.quotation_id = ? "+ 
													"and  "+
													"    qli.stock_id not in ('null') "+
													"and  "+
													"    qli.stock_id = s.stock_id"); 
			statement2.setInt(1, quotationId);
			
			resultSet2 = statement2.executeQuery();
			String serialNumber = "";
			while (resultSet2.next()) {
				Stock stock = new Stock();
				stock.setModelName(resultSet2.getString("model_name"));
				stock.setPricing(resultSet2.getDouble("pricing"));
				serialNumber = resultSet2.getString("serial_number");
				stock.setStockCategory(resultSet2.getString("stock_category"));
				stock.setStockCode(resultSet2.getString("stock_code"));
				stock.setStockId(resultSet2.getInt("stock_id"));
				stock.setTechnicalSpecs(resultSet2.getString("technical_specs"));
				stock.setStockDescription(resultSet2.getString("stock_description"));
			
				stockItems.add(stock);
			}
			
			quote.setQuotationLineItems(stockItems);
			
			//4. get all the optional extras for this quotation...
			ArrayList<OptionalExtra> optionalExtraItems = new ArrayList<OptionalExtra>();
			statement3 = connection.prepareStatement("select "+
													"    oe.optional_extra_id, oe.optional_extra_description, oe.pricing "+ 
													"from  "+
													"    las_stock.quotation_line_item qli, las_stock.optional_extra oe "+
													"where "+
													"    qli.quotation_id = ? "+ 
													"and  "+
													"    qli.optional_extra_id not in ('null') "+
													"and  "+
													"    qli.optional_extra_id = oe.optional_extra_id");
			statement3.setInt(1, quotationId);
			
			resultSet3 = statement3.executeQuery();
			
			while (resultSet3.next()) {
				OptionalExtra optionalExtra = new OptionalExtra();
				
				optionalExtra.setOptionalExtraId(resultSet3.getInt("optional_extra_id"));
				optionalExtra.setDescription(resultSet3.getString("optional_extra_description"));
				optionalExtra.setPricing(resultSet3.getDouble("pricing"));
				
				optionalExtraItems.add(optionalExtra);
			}
			quote.setOptionalExtraItems(optionalExtraItems);
			
			return quote;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			closeResultSet(resultSet1);
			closeResultSet(resultSet2);
			closeResultSet(resultSet3);
			closeStatement(statement1);
			closeStatement(statement2);
			closeStatement(statement3);
			closeConnection(connection);
		}
	}
	
	public ArrayList<MiniQuote> getquoteHistoryList() {
		ArrayList<MiniQuote> quoteList = new ArrayList<MiniQuote>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			statement = connection.prepareStatement("select c.customer_name, q.quotation_id, q.quotation_date, q.status from las_stock.customer c, las_stock.quotation q where q.customer_id = c.customer_id order by quotation_id desc");
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				MiniQuote quote = new MiniQuote();
				quote.setCustomerName(resultSet.getString("customer_name"));
				quote.setQuotationDate(resultSet.getString("quotation_date"));
				quote.setQuotationId(resultSet.getInt("quotation_id"));
				quote.setStatus(resultSet.getString("status"));			
				
				quoteList.add(quote);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeStatement(statement);
			closeResultSet(resultSet);
			closeConnection(connection);
		}
		return quoteList;
	}
	
	public Defaults getDefaults() {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Defaults defaults = new Defaults();
		try {
			statement = connection.prepareStatement("select notes, delivery, installation, warranty, variation, validity from las_stock.defaults");
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				defaults.setNotes(resultSet.getString("notes"));
				defaults.setDelivery(resultSet.getString("delivery"));
				defaults.setInstallation(resultSet.getString("installation"));
				defaults.setWarranty(resultSet.getString("warranty"));
				defaults.setVariation(resultSet.getString("variation"));
				defaults.setValidity(resultSet.getString("validity"));
			}
		}
		catch (Exception e) {
			
		}
		finally {
			closeStatement(statement);
			closeResultSet(resultSet);
			closeConnection(connection);
		}
		return defaults;
	}
	
	public int updateDefaults(Defaults defaults) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		int result = 0;
		
		try {
			statement = connection.prepareStatement("update las_stock.defaults set notes=?, delivery=?, installtion=?, warranty=?, variation=?, validity=?");
			statement.setString(1, defaults.getNotes());
			statement.setString(2, defaults.getDelivery());
			statement.setString(3, defaults.getInstallation());
			statement.setString(4, defaults.getWarranty());
			statement.setString(5, defaults.getVariation());
			statement.setString(6, defaults.getValidity());
			
			
			result = statement.executeUpdate();
			
		}
		catch (Exception e) {
			return -1;
		}
		finally {
			closeStatement(statement);
			closeConnection(connection);
		}
		return result;
	}
}
