package za.co.las.stock.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import za.co.las.stock.object.OptionalExtra;

public class OptionalExtraDAO extends AbstractDAO {
	
	public int updateOptionalExtra(int optionalExtraId, OptionalExtra newOptionalExtra) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("update las_stock.optional_extra set optional_extra_description = ?, pricing = ? where optional_extra_id = ?");
			
			statement.setString(1, newOptionalExtra.getDescription());
			statement.setDouble(2, newOptionalExtra.getPricing());
			statement.setInt(3, newOptionalExtra.getOptionalExtraId());
			
			int result = statement.executeUpdate();
			
			return result;
		}
		catch (Exception e) {
			closeStatement(statement);
			closeConnection(connection);
			e.printStackTrace();
		}
		return -1;
	}
	
	public int insertOptionalExtra(OptionalExtra newOptionalExtra) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("insert into las_stock.optional_extra (optional_extra_description, pricing) values (?, ?)");
			
			statement.setString(1, newOptionalExtra.getDescription());
			statement.setDouble(2, newOptionalExtra.getPricing());
			
			int result = statement.executeUpdate();
			
			return result;
		}
		catch (Exception e) {
			closeStatement(statement);
			closeConnection(connection);
			e.printStackTrace();
		}
		return -1;
	}
	
	public int deleteOptionalExtra(int optionalExtraId) {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("delete from las_stock.optional_extra where option_extra_id = ?");

			statement.setInt(1, optionalExtraId);
			
			int result = statement.executeUpdate();
			
			return result;
		}
		catch (Exception e) {
			closeStatement(statement);
			closeConnection(connection);
			e.printStackTrace();
		}
		return -1;
	}
	
	public ArrayList<OptionalExtra> getOptionalExtras() {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<OptionalExtra> optionalExtraItems = new ArrayList<OptionalExtra>();
		
		try {
			statement = connection.prepareStatement("select optional_extra_id, optional_extra_description, pricing from las.stock");

			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				OptionalExtra optionalExtra = new OptionalExtra();
				
				optionalExtra.setOptionalExtraId(resultSet.getInt("optional_extra_id"));
				optionalExtra.setDescription(resultSet.getString("optional_extra_description"));
				optionalExtra.setPricing(resultSet.getDouble("pricing"));
				
				optionalExtraItems.add(optionalExtra);
			}
		}
		catch (Exception e) {
			closeResultSet(resultSet);
			closeStatement(statement);
			closeConnection(connection);
			e.printStackTrace();
		}
		return optionalExtraItems;
	}
}
