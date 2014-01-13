package za.co.las.stock.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import za.co.las.stock.object.Stock;
import za.co.las.stock.object.StockLevel;

public class StockDAO extends AbstractDAO {
	
	public int updateStock(int stockId, Stock newStock) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			//1. update the stock table...
			statement = connection.prepareStatement("update las_stock.stock set stock_code = ?, stock_category = ?, model_name = ?, pricing = ?, technical_specs = ?, stock_description = ? where stock_id = ?");
			
			statement.setString(1, newStock.getStockCode());
			statement.setString(2, newStock.getStockCategory());
			statement.setString(3, newStock.getModelName());
			statement.setDouble(4, newStock.getPricing());
			statement.setString(5,  newStock.getTechnicalSpecs());
			statement.setString(6, newStock.getStockDescription());
			statement.setInt(7, stockId);
			
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
	
	public int insertStock(Stock newStock) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("insert into las_stock.stock (stock_code, stock_category, model_name, pricing, technical_specs, stock_description) values (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, newStock.getStockCode());
			statement.setString(2, newStock.getStockCategory());
			statement.setString(3, newStock.getModelName());
			statement.setDouble(4, newStock.getPricing());
			statement.setString(5, newStock.getTechnicalSpecs());
			statement.setString(6, newStock.getStockDescription());
			
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
	
	public int deleteStock(int stockId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("delete from las_stock.stock where stock_id = ?");

			statement.setInt(1, stockId);
			
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
	
	public ArrayList<String> getStockCategories() {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<String> stockCategories = new ArrayList<String>();
		
		try {
			statement = connection.prepareStatement("select distinct stock_category from las_stock.stock");
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String tempCat = resultSet.getString("stock_category");
				stockCategories.add(tempCat);
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
		return stockCategories;
	}
	
	public ArrayList<Stock> getStockItemsFromCategory(String category) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<Stock> stockItems = new ArrayList<Stock>();
		
		try {
			statement = connection.prepareStatement("select s.stock_id, s.stock_code, s.stock_category, s.model_name, s.pricing, s.technical_specs, s.stock_description from las_stock.stock s where s.stock_category = ?");
			
			statement.setString(1, category);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				Stock stock = new Stock();
				stock.setModelName(resultSet.getString("model_name"));
				stock.setPricing(resultSet.getDouble("pricing"));
				stock.setStockCategory(resultSet.getString("stock_category"));
				stock.setStockCode(resultSet.getString("stock_code"));
				stock.setStockId(resultSet.getInt("stock_id"));
				stock.setTechnicalSpecs(resultSet.getString("technical_specs"));
				stock.setStockDescription(resultSet.getString("stock_description"));
				
				stockItems.add(stock);
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
		return stockItems;
	}
	
	public Stock getStockItemForStockId(int stockId, boolean available) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		PreparedStatement statement1 = null;
		ResultSet resultSet1 = null;
		Stock stock = null;
		
		try {
			//1. get the stock item...
			statement = connection.prepareStatement("select s.stock_id, s.stock_code, s.stock_category, s.model_name, s.pricing, s.technical_specs, s.stock_description from las_stock.stock s where s.stock_id = ?");
			
			statement.setInt(1, stockId);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				stock = new Stock();
				stock.setModelName(resultSet.getString("model_name"));
				stock.setPricing(resultSet.getDouble("pricing"));
				stock.setStockCategory(resultSet.getString("stock_category"));
				stock.setStockCode(resultSet.getString("stock_code"));
				stock.setStockId(resultSet.getInt("stock_id"));
				stock.setTechnicalSpecs(resultSet.getString("technical_specs"));
				stock.setStockDescription(resultSet.getString("stock_description"));
			}
			
			//2. get all the serial numbers linked to this stock item...
			String sql = "";
			if (available) {
				sql = "select serial_number, stock_status from las_stock.stock_level where stock_id = ? and stock_status = 0";
			}
			else {
				sql = "select serial_number, stock_status from las_stock.stock_level where stock_id = ?";
			}
			statement1 = connection.prepareStatement(sql);
			
			statement1.setInt(1, stockId);
			resultSet1 = statement1.executeQuery();
			
			ArrayList<StockLevel> stockLevelList = new ArrayList<StockLevel>();
			
			while (resultSet1.next()) {
				StockLevel stockLevel = new StockLevel();
				stockLevel.setSerialNumber(resultSet1.getString("serial_number"));
				stockLevel.setStatus(resultSet1.getInt("stock_status"));
				
				stockLevelList.add(stockLevel);
			}
			stock.setStockLevel(stockLevelList);
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
		return stock;
	}
	
	public int deleteSerialNumberFromStockId(String serialNumber, int stockId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("delete from las_stock.stock_level where stock_id = ? and serial_number = ?");

			statement.setInt(1, stockId);
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
	
	public int addSerialNumberToStockId(String serialNumber, int stockId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("insert into las_stock.stock_level (serial_number, stock_id, stock_status) values (?, ?, ?)");
			
			statement.setString(1, serialNumber);
			statement.setInt(2, stockId);
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
	
	public ArrayList<StockLevel> getSerialNumberForStockId(int stockId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<StockLevel> stockLevelItems = new ArrayList<StockLevel>();
		
		try {
			statement = connection.prepareStatement("select serial_number, stock_status from las_stock.stock_level where stock_id = ?");
			
			statement.setInt(1, stockId);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				StockLevel stockLevel = new StockLevel();
				stockLevel.setSerialNumber(resultSet.getString("serial_number"));
				stockLevel.setStatus(resultSet.getInt("stock_status"));
				
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