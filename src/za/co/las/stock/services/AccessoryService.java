package za.co.las.stock.services;

import java.util.ArrayList;

import za.co.las.stock.dao.AccessoryDAO;
import za.co.las.stock.object.Accessory;
import za.co.las.stock.object.StockLevel;

public class AccessoryService {

	private AccessoryDAO accessoryDAO = new AccessoryDAO();
	
	public int deleteAccessoryItem(int accessoryId) {
		return accessoryDAO.deleteAccessory(accessoryId);
	}
	
	public int updateAccessoryItem(int accessoryId, String currency, double pricing, String accessoryManufacturer, String accessoryCode, String description, String model, int markup, int shipping) {
		Accessory accessory = new Accessory();
		accessory.setAccessoryManufacturer(accessoryManufacturer);
		accessory.setAccessoryCurrency(currency);
		accessory.setPricing(pricing);
		accessory.setAccessoryCode(accessoryCode);
		accessory.setAccessoryDescription(description);
		accessory.setAccessoryModel(model);
		accessory.setAccessoryMarkup(markup);
		accessory.setAccessoryShipping(shipping);
		return accessoryDAO.updateAccessory(accessoryId, accessory);
	}
	
	public int createAccessoryItem(String currency, double pricing, String accessoryManufacturer, String accessoryCode, String description, String model, int markup, int shipping) {
		Accessory accessory = new Accessory();
		accessory.setAccessoryManufacturer(accessoryManufacturer);
		accessory.setPricing(pricing);
		accessory.setAccessoryCurrency(currency);
		accessory.setAccessoryCode(accessoryCode);
		accessory.setAccessoryDescription(description);
		accessory.setAccessoryModel(model);
		accessory.setAccessoryMarkup(markup);
		accessory.setAccessoryShipping(shipping);
		return accessoryDAO.insertAccessory(accessory);
	}
	
	public Accessory getAccessoryItemForAccessoryId(int accessoryId) {
		return accessoryDAO.getAccessoryItemForAccessoryId(accessoryId, false);
	}
	
	public Accessory getAvailableAccessoryItemForAccessoryId(int accessoryId) {
		return accessoryDAO.getAccessoryItemForAccessoryId(accessoryId, true);
	}
	
	public ArrayList<String> getAccessoryManufacturers() {
		return accessoryDAO.getAccessoryManufacturers();
	}
	
	public ArrayList<Accessory> getAccessoryItemsFromManufacturer(String manufacturer) {
		return accessoryDAO.getAccessoryItemsFromManufacturer(manufacturer);
	}
	
	public ArrayList<StockLevel> getStockLevelsForAccessoryId(int accessoryId) {
		return accessoryDAO.getSerialNumberForAccessoryId(accessoryId);
	}
	
	public int addSerialNumberToAccessoryId(String serialNumber, int accessoryId) {
		return accessoryDAO.addSerialNumberToAccessoryId(serialNumber, accessoryId);
	}
	
	public int deleteSerialNumberFromAccessoryId(String serialNumber, int accessoryId) {
		return accessoryDAO.deleteSerialNumberFromAccessoryId(serialNumber, accessoryId);
	}
}
