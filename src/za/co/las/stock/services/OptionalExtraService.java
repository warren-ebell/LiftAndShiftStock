package za.co.las.stock.services;

import java.util.ArrayList;

import za.co.las.stock.dao.OptionalExtraDAO;
import za.co.las.stock.object.OptionalExtra;

public class OptionalExtraService {

	private OptionalExtraDAO optionalExtraDAO = new OptionalExtraDAO();
	
	public int deleteOptionalExtraItem(int optionalExtraId) {
		return optionalExtraDAO.deleteOptionalExtra(optionalExtraId);
	}
	
	public int updateOptionalExtraItem(int optionalExtraId, String description, double pricing) {
		OptionalExtra optionalExtra = new OptionalExtra();
		optionalExtra.setDescription(description);
		optionalExtra.setPricing(pricing);
		return optionalExtraDAO.updateOptionalExtra(optionalExtraId, optionalExtra);
	}
	
	public int createOptionalExtraItem(String description, double pricing) {
		OptionalExtra optionalExtra = new OptionalExtra();
		optionalExtra.setDescription(description);
		optionalExtra.setPricing(pricing);
		return optionalExtraDAO.insertOptionalExtra(optionalExtra);
	}
	
	public ArrayList<OptionalExtra> getOptionalExtraItems() {
		return optionalExtraDAO.getOptionalExtras();
	}
	
	public ArrayList<String> insertOptionalExtraList(ArrayList<OptionalExtra> oeList) {
		ArrayList<String> oeIdList = new ArrayList<String>();
		for (OptionalExtra oe : oeList) {
			String id = ""+optionalExtraDAO.insertOptionalExtra(oe);
			oeIdList.add(id);
		}
		return oeIdList;
	}
}
