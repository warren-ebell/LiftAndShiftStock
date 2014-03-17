package za.co.las.stock.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractDAO {

	protected Connection getConnection() {
		Connection conn = null;
		try	{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/las_stock","root", "password");
			//PROD config
			//conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/las_stock","root", "L@SmySQ1r00t");
			//REMOTE DEBUG CONFIG
			//conn = DriverManager.getConnection("jdbc:mysql://197.221.7.50:3306/las_stock","root", "L@SmySQ1r00t");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
			}
		}
	}

	public void closeStatement(Statement statement) {
		if (statement != null) {
			try	{
				statement.close();
			}
			catch (SQLException e) {
			}
		}
	}

	public void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try	{
				resultSet.close();
			}
			catch (SQLException e) {
			}
		}
	}

}