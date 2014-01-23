package za.co.las.stock.services;

import java.util.ArrayList;

import za.co.las.stock.dao.StockDAO;
import za.co.las.stock.object.InstallationLocation;
import za.co.las.stock.object.ReportStock;
import za.co.las.stock.object.Stock;
import za.co.las.stock.object.StockLevel;

public class StockService {

	private StockDAO stockDAO = new StockDAO();
	
	public int deleteStockItem(int stockId) {
		return stockDAO.deleteStock(stockId);
	}
	
	public ArrayList<ReportStock> getAllAvailableStockForReport() {
		return stockDAO.getAllAvailableStockForReport();
	}
	
	public int updateStockItem(int stockId, double pricing, String stockManufacturer, String stockModel, String stockSeries, String stockCode, String technicalSpecs, String description, int used) {
		Stock stock = new Stock();
		stock.setStockManufacturer(stockManufacturer);
		stock.setStockModel(stockModel);
		stock.setStockSeries(stockSeries);
		stock.setPricing(pricing);
		stock.setStockCode(stockCode);
		stock.setTechnicalSpecs(technicalSpecs);
		stock.setStockDescription(description);
		stock.setStockUsed(used);
		return stockDAO.updateStock(stockId, stock);
	}
	
	public int createStockItem(double pricing, String stockManufacturer, String stockModel, String stockSeries, String stockCode, String technicalSpecs, String description, int used) {
		Stock stock = new Stock();
		stock.setStockManufacturer(stockManufacturer);
		stock.setStockModel(stockModel);
		stock.setStockSeries(stockSeries);
		stock.setPricing(pricing);
		stock.setStockCode(stockCode);
		stock.setTechnicalSpecs(technicalSpecs);
		stock.setStockDescription(description);
		stock.setStockUsed(used);
		return stockDAO.insertStock(stock);
	}
	
	public Stock getStockItemForStockId(int stockId) {
		return stockDAO.getStockItemForStockId(stockId, false);
	}
	
	public Stock getAvailableStockItemForStockId(int stockId) {
		return stockDAO.getStockItemForStockId(stockId, true);
	}
	
	public ArrayList<String> getStockManufacturers() {
		return stockDAO.getStockManufacturers();
	}
	
	public ArrayList<String> getStockModelsForManufacturer(String manufacturer) {
		return stockDAO.getStockModelsForManufacturer(manufacturer);
	}
	
	public ArrayList<Stock> getStockItemsFromManufacturerAndModel(String manufacturer, String model) {
		return stockDAO.getStockItemsFromManufacturerAndModel(manufacturer, model);
	}
	
	public ArrayList<StockLevel> getStockLevelsForStockId(int stockId) {
		return stockDAO.getSerialNumberForStockId(stockId);
	}
	
	public int addSerialNumberToStockId(String serialNumber, int stockId) {
		return stockDAO.addSerialNumberToStockId(serialNumber, stockId);
	}
	
	public int deleteSerialNumberFromStockId(String serialNumber, int stockId) {
		return stockDAO.deleteSerialNumberFromStockId(serialNumber, stockId);
	}
	
	public int addInstallLocationToStockId(String location, double pricing, int stockId) {
		InstallationLocation installLocation = new InstallationLocation();
		installLocation.setLocation(location);
		installLocation.setPrice(pricing);
		return stockDAO.addInstallLocationForStockId(installLocation, stockId);
	}
	
	public int deleteInstallLocationFromStockId(String location, double pricing, int stockId) {
		InstallationLocation installLocation = new InstallationLocation();
		installLocation.setLocation(location);
		installLocation.setPrice(pricing);
		return stockDAO.deleteInstallLocationForStockId(installLocation, stockId);
	}
	
}
