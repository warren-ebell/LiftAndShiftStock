package za.co.las.stock.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import za.co.las.stock.object.Accessory;
import za.co.las.stock.object.StockLevel;

import com.mysql.jdbc.Statement;

public class AccessoryDAO extends AbstractDAO {
	
	public int updateAccessory(int accessoryId, Accessory newAccessory) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			//1. update the accessory table...
			statement = connection.prepareStatement("update las_stock.accessory set accessory_code = ?, accessory_manufacturer = ?, pricing = ?, accessory_description = ?, accessory_model = ? where accessory_id = ?");
			
			statement.setString(1, newAccessory.getAccessoryCode());
			statement.setString(2, newAccessory.getAccessoryManufacturer());
			statement.setDouble(3, newAccessory.getPricing());
			statement.setString(4,  newAccessory.getAccessoryDescription());
			statement.setString(5, newAccessory.getAccessoryModel());
			statement.setInt(6, accessoryId);
			
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
	
	public int insertAccessory(Accessory newAccessory) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("insert into las_stock.accessory (accessory_code, accessory_manufacturer, pricing, accessory_description, accessory_model) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, newAccessory.getAccessoryCode());
			statement.setString(2, newAccessory.getAccessoryManufacturer());
			statement.setDouble(3, newAccessory.getPricing());
			statement.setString(4, newAccessory.getAccessoryDescription());
			statement.setString(5, newAccessory.getAccessoryModel());
			
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
	
	public int deleteAccessory(int accessoryId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("delete from las_stock.accessory where accessory_id = ?");

			statement.setInt(1, accessoryId);
			
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
	
	public ArrayList<String> getAccessoryManufacturers() {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<String> accessoryManufacturers = new ArrayList<String>();
		
		try {
			statement = connection.prepareStatement("select distinct accessory_manufacturer from las_stock.accessory");
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String tempMan = resultSet.getString("accessory_manufacturer");
				accessoryManufacturers.add(tempMan);
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
		return accessoryManufacturers;
	}
	
	public ArrayList<Accessory> getAccessoryItemsFromManufacturer(String manufacturer) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<Accessory> accessoryItems = new ArrayList<Accessory>();
		
		try {
			statement = connection.prepareStatement("select a.accessory_id, a.accessory_code, a.accessory_manufacturer, a.pricing, a.accessory_description, a.accessory_model from las_stock.accessory a where a.accessory_manufacturer = ? ");
			
			statement.setString(1, manufacturer);
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				Accessory accessory = new Accessory();
				accessory.setPricing(resultSet.getDouble("pricing"));
				accessory.setAccessoryManufacturer(resultSet.getString("accessory_manufacturer"));
				accessory.setAccessoryCode(resultSet.getString("accessory_code"));
				accessory.setAccessoryId(resultSet.getInt("accessory_id"));
				accessory.setAccessoryDescription(resultSet.getString("accessory_description"));
				accessory.setAccessoryModel(resultSet.getString("accessory_model"));
				
				accessoryItems.add(accessory);
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
		return accessoryItems;
	}
	
	public Accessory getAccessoryItemForAccessoryId(int accessoryId, boolean available) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		PreparedStatement statement1 = null;
		ResultSet resultSet1 = null;
		Accessory accessory = null;
		
		try {
			//1. get the stock item...
			statement = connection.prepareStatement("select a.accessory_id, a.accessory_code, a.accessory_manufacturer, a.pricing, a.accessory_description, a.accessory_model from las_stock.accessory a where a.accessory_id = ?");
			
			statement.setInt(1, accessoryId);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				accessory = new Accessory();
				accessory.setPricing(resultSet.getDouble("pricing"));
				accessory.setAccessoryManufacturer(resultSet.getString("accessory_manufacturer"));
				accessory.setAccessoryCode(resultSet.getString("accessory_code"));
				accessory.setAccessoryId(resultSet.getInt("accessory_id"));
				accessory.setAccessoryDescription(resultSet.getString("accessory_description"));
				accessory.setAccessoryModel(resultSet.getString("accessory_model"));
			}
			
			//2. get all the serial numbers linked to this stock item...
			String sql = "";
			if (available) {
				sql = "select serial_number, accessory_status from las_stock.accessory_level where accessory_id = ? and accessory_status = 0";
			}
			else {
				sql = "select serial_number, accessory_status from las_stock.accessory_level where accessory_id = ?";
			}
			statement1 = connection.prepareStatement(sql);
			
			statement1.setInt(1, accessoryId);
			resultSet1 = statement1.executeQuery();
			
			ArrayList<StockLevel> stockLevelList = new ArrayList<StockLevel>();
			
			while (resultSet1.next()) {
				StockLevel stockLevel = new StockLevel();
				stockLevel.setSerialNumber(resultSet1.getString("serial_number"));
				stockLevel.setStatus(resultSet1.getInt("accessory_status"));
				
				stockLevelList.add(stockLevel);
			}
			accessory.setAccessoryLevel(stockLevelList);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeResultSet(resultSet);
			closeResultSet(resultSet1);
			closeStatement(statement);
			closeStatement(statement1);
			closeConnection(connection);
		}
		return accessory;
	}
	
	public int deleteSerialNumberFromAccessoryId(String serialNumber, int accessoryId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("delete from las_stock.accessory_level where accessory_id = ? and serial_number = ?");

			statement.setInt(1, accessoryId);
			statement.setString(2, serialNumber);
			
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
	
	public int addSerialNumberToAccessoryId(String serialNumber, int accessoryId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("insert into las_stock.accessory_level (serial_number, accessory_id, accessory_status) values (?, ?, ?)");
			
			statement.setString(1, serialNumber);
			statement.setInt(2, accessoryId);
			statement.setString(3, "0");
			
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
	
	public ArrayList<StockLevel> getSerialNumberForAccessoryId(int accessoryId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<StockLevel> stockLevelItems = new ArrayList<StockLevel>();
		
		try {
			statement = connection.prepareStatement("select serial_number, accessory_status from las_stock.accessory_level where accessory_id = ?");
			
			statement.setInt(1, accessoryId);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				StockLevel stockLevel = new StockLevel();
				stockLevel.setSerialNumber(resultSet.getString("serial_number"));
				stockLevel.setStatus(resultSet.getInt("accessory_status"));
				
				stockLevelItems.add(stockLevel);
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
		return stockLevelItems;
	}
}
