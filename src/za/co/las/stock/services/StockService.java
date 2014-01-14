package za.co.las.stock.services;

import java.util.ArrayList;

import za.co.las.stock.dao.StockDAO;
import za.co.las.stock.object.Stock;
import za.co.las.stock.object.StockLevel;

public class StockService {

	private StockDAO stockDAO = new StockDAO();
	
	public int deleteStockItem(int stockId) {
		return stockDAO.deleteStock(stockId);
	}
	
	public int updateStockItem(int stockId, double pricing, String stockManufacturer, String stockModel, String stockSeries, String stockCode, String technicalSpecs, String description) {
		Stock stock = new Stock();
		stock.setStockManufacturer(stockManufacturer);
		stock.setStockModel(stockModel);
		stock.setStockSeries(stockSeries);
		stock.setPricing(pricing);
		stock.setStockCode(stockCode);
		stock.setTechnicalSpecs(technicalSpecs);
		stock.setStockDescription(description);
		return stockDAO.updateStock(stockId, stock);
	}
	
	public int createStockItem(double pricing, String stockManufacturer, String stockModel, String stockSeries, String stockCode, String technicalSpecs, String description) {
		Stock stock = new Stock();
		stock.setStockManufacturer(stockManufacturer);
		stock.setStockModel(stockModel);
		stock.setStockSeries(stockSeries);
		stock.setPricing(pricing);
		stock.setStockCode(stockCode);
		stock.setTechnicalSpecs(technicalSpecs);
		stock.setStockDescription(description);
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
}
