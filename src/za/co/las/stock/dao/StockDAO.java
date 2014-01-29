package za.co.las.stock.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import za.co.las.stock.object.InstallationLocation;
import za.co.las.stock.object.ReportAllStock;
import za.co.las.stock.object.ReportStock;
import za.co.las.stock.object.Stock;
import za.co.las.stock.object.StockImage;
import za.co.las.stock.object.StockLevel;

public class StockDAO extends AbstractDAO {
	
	public int updateStock(int stockId, Stock newStock) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			//1. update the stock table...
			statement = connection.prepareStatement("update las_stock.stock set stock_code = ?, stock_manufacturer = ?, stock_model = ?, stock_series = ?, pricing = ?, technical_specs = ?, stock_description = ?, stock_used = ?, stock_markup = ?, stock_shipping = ? where stock_id = ?");
			
			statement.setString(1, newStock.getStockCode());
			statement.setString(2, newStock.getStockManufacturer());
			statement.setString(3, newStock.getStockModel());
			statement.setString(4, newStock.getStockSeries());
			statement.setDouble(5, newStock.getPricing());
			statement.setString(6,  newStock.getTechnicalSpecs());
			statement.setString(7, newStock.getStockDescription());
			statement.setInt(8, newStock.getStockUsed());
			statement.setInt(9, newStock.getStockMarkup());
			statement.setInt(10, newStock.getStockShipping());
			statement.setInt(11, stockId);
			
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
			statement = connection.prepareStatement("insert into las_stock.stock (stock_code, stock_manufacturer, stock_model, stock_series, pricing, technical_specs, stock_description, stock_used, stock_markup, stock_shipping) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, newStock.getStockCode());
			statement.setString(2, newStock.getStockManufacturer());
			statement.setString(3, newStock.getStockModel());
			statement.setString(4, newStock.getStockSeries());
			statement.setDouble(5, newStock.getPricing());
			statement.setString(6, newStock.getTechnicalSpecs());
			statement.setString(7, newStock.getStockDescription());
			statement.setInt(8, newStock.getStockUsed());
			statement.setInt(9, newStock.getStockMarkup());
			statement.setInt(10, newStock.getStockShipping());
			
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
	
	public ArrayList<String> getStockManufacturers() {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<String> stockManufacturers = new ArrayList<String>();
		
		try {
			statement = connection.prepareStatement("select distinct stock_manufacturer from las_stock.stock");
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String tempMan = resultSet.getString("stock_manufacturer");
				stockManufacturers.add(tempMan);
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
		return stockManufacturers;
	}
	
	public ArrayList<String> getStockModelsForManufacturer(String manufacturer) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<String> stockModels = new ArrayList<String>();
		
		try {
			statement = connection.prepareStatement("select distinct stock_model from las_stock.stock where stock_manufacturer = ?");
			statement.setString(1, manufacturer);
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String tempModel = resultSet.getString("stock_model");
				stockModels.add(tempModel);
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
		return stockModels;
	}
	
	public ArrayList<Stock> getStockItemsFromManufacturerAndModel(String manufacturer, String model) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<Stock> stockItems = new ArrayList<Stock>();
		
		try {
			statement = connection.prepareStatement("select s.stock_id, s.stock_code, s.stock_manufacturer, s.stock_model, s.stock_series, s.pricing, s.technical_specs, s.stock_description, s.stock_used, s.stock_markup, s.stock_shipping from las_stock.stock s where s.stock_manufacturer = ? and s.stock_model = ?");
			
			statement.setString(1, manufacturer);
			statement.setString(2, model);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				Stock stock = new Stock();
				stock.setStockModel(resultSet.getString("stock_model"));
				stock.setStockSeries(resultSet.getString("stock_series"));
				stock.setPricing(resultSet.getDouble("pricing"));
				stock.setStockManufacturer(resultSet.getString("stock_manufacturer"));
				stock.setStockCode(resultSet.getString("stock_code"));
				stock.setStockId(resultSet.getInt("stock_id"));
				stock.setTechnicalSpecs(resultSet.getString("technical_specs"));
				stock.setStockDescription(resultSet.getString("stock_description"));
				stock.setStockUsed(resultSet.getInt("stock_used"));
				stock.setStockMarkup(resultSet.getInt("stock_markup"));
				stock.setStockShipping(resultSet.getInt("stock_shipping"));
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
		PreparedStatement statement2 = null;
		ResultSet resultSet2 = null;
		Stock stock = null;
		
		try {
			//1. get the stock item...
			statement = connection.prepareStatement("select s.stock_id, s.stock_code, s.stock_manufacturer, s.stock_model, s.stock_series, s.pricing, s.technical_specs, s.stock_description, s.stock_used, s.stock_markup, s.stock_shipping from las_stock.stock s where s.stock_id = ?");
			
			statement.setInt(1, stockId);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				stock = new Stock();
				stock.setStockModel(resultSet.getString("stock_model"));
				stock.setStockSeries(resultSet.getString("stock_series"));
				stock.setPricing(resultSet.getDouble("pricing"));
				stock.setStockManufacturer(resultSet.getString("stock_manufacturer"));
				stock.setStockCode(resultSet.getString("stock_code"));
				stock.setStockId(resultSet.getInt("stock_id"));
				stock.setTechnicalSpecs(resultSet.getString("technical_specs"));
				stock.setStockDescription(resultSet.getString("stock_description"));
				stock.setStockUsed(resultSet.getInt("stock_used"));
				stock.setStockMarkup(resultSet.getInt("stock_markup"));
				stock.setStockShipping(resultSet.getInt("stock_shipping"));
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
			
			//3. get all of the install locations for this stock item...
			ArrayList<InstallationLocation> installLocations = new ArrayList<InstallationLocation>();
			
			statement2 = connection.prepareStatement("SELECT stock_id, install_location, install_price FROM las_stock.stock_install where stock_id = ?");
			
			statement2.setInt(1, stockId);
			resultSet2 = statement2.executeQuery();
			
			while (resultSet2.next()) {
				InstallationLocation temp = new InstallationLocation();
				temp.setLocation(resultSet2.getString("install_location"));
				temp.setPrice(resultSet2.getDouble("install_price"));
				
				installLocations.add(temp);
			}
			
			stock.setInstallLocations(installLocations);
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
	
	public int addInstallLocationForStockId(InstallationLocation installLocation, int stockId){
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("insert into las_stock.stock_install (stock_id, install_location, install_price) values (?, ?, ?)");
			
			statement.setInt(1, stockId);
			statement.setString(2, installLocation.getLocation());
			statement.setDouble(3, installLocation.getPrice());
			
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
	
	public int deleteInstallLocationForStockId(InstallationLocation installLocation, int stockId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("delete from las_stock.stock_install where stock_id = ? and install_location = ?");

			statement.setInt(1, stockId);
			statement.setString(2, installLocation.getLocation());
			
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
	
	public ArrayList<ReportStock> getAllAvailableStockForReport() {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<ReportStock> reportStockItems = new ArrayList<ReportStock>();
		
		try {
			statement = connection.prepareStatement("select st.stock_id, st.stock_code, st.stock_manufacturer, st.stock_model, st.stock_series, st.stock_used, sl.serial_number "
					+ "from"
					+ " las_stock.stock st, "
					+ " las_stock.stock_level sl "
					+ "where "
					+ " sl.stock_id = st.stock_id "
					+ "and "
					+ " sl.stock_status = 0 "
					+ "order by "
					+ " st.stock_manufacturer, st.stock_model;");
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				ReportStock report = new ReportStock();
				report.setStockId(resultSet.getInt("stock_id"));
				report.setSerialNumber(resultSet.getString("serial_number"));
				report.setStockCode(resultSet.getString("stock_code"));
				report.setStockManufacturer(resultSet.getString("stock_manufacturer"));
				report.setStockModel(resultSet.getString("stock_model"));
				report.setStockSeries(resultSet.getString("stock_series"));
				report.setStockUsed(resultSet.getInt("stock_used"));

				reportStockItems.add(report);
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
		return reportStockItems;
	}
	
	public ArrayList<ReportAllStock> getAllStockForReport() {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<ReportAllStock> reportStockItems = new ArrayList<ReportAllStock>();
		
		try {
			statement = connection.prepareStatement("select st.stock_id, st.stock_code, st.stock_manufacturer, st.stock_model, st.stock_series, st.stock_used, sl.serial_number, sl.stock_status, st.pricing "+
													"from "+
													"    las_stock.stock st, "+ 
													"	las_stock.stock_level sl "+ 
													"where  "+
													"	sl.stock_id = st.stock_id "+  
													"order by  "+
													"	st.stock_manufacturer, st.stock_model;");
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				ReportAllStock report = new ReportAllStock();
				report.setStockId(resultSet.getInt("stock_id"));
				report.setSerialNumber(resultSet.getString("serial_number"));
				report.setStockCode(resultSet.getString("stock_code"));
				report.setStockManufacturer(resultSet.getString("stock_manufacturer"));
				report.setStockModel(resultSet.getString("stock_model"));
				report.setStockSeries(resultSet.getString("stock_series"));
				report.setStockUsed(resultSet.getInt("stock_used"));
				report.setStockStatus(resultSet.getInt("stock_status"));
				report.setStockPrice(resultSet.getDouble("pricing"));

				reportStockItems.add(report);
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
		return reportStockItems;
	}
	
	public int insertStockImage(StockImage stockImage) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("insert into las_stock.stock_image (stock_id, stock_image) values (?, ?)");
			
			statement.setInt(1, stockImage.getStockId());
			statement.setBytes(2, stockImage.getStockImage());
			
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
	
	public StockImage getStockImageFroStockId(int stockId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareStatement("select stock_id, stock_image from las_stock.stock_image where stock_id = ?");
			
			statement.setInt(1, stockId);
			
			resultSet = statement.executeQuery(); 
			StockImage stockImage = new StockImage();
			
			while (resultSet.next()) {
				stockImage.setStockId(stockId);
				stockImage.setStockImage(resultSet.getBytes("stock_image"));
				
			}
			return stockImage;
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
