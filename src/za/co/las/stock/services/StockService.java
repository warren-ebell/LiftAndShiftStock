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
	
	public int updateStockItem(int stockId, String modelName, double pricing, String stockCategory, String stockCode, String technicalSpecs, String description) {
		Stock stock = new Stock();
		stock.setModelName(modelName);
		stock.setPricing(pricing);
		stock.setStockCategory(stockCategory);
		stock.setStockCode(stockCode);
		stock.setTechnicalSpecs(technicalSpecs);
		stock.setStockDescription(description);
		return stockDAO.updateStock(stockId, stock);
	}
	
	public int createStockItem(String modelName, double pricing, String stockCategory, String stockCode, String technicalSpecs, String description) {
		Stock stock = new Stock();
		stock.setModelName(modelName);
		stock.setPricing(pricing);
		stock.setStockCategory(stockCategory);
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
	
	public ArrayList<String> getStockCategories() {
		return stockDAO.getStockCategories();
	}
	
	public ArrayList<Stock> getStockItemsForStockCategories(String stockCategory) {
		return stockDAO.getStockItemsFromCategory(stockCategory);
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
