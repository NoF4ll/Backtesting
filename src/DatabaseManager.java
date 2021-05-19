import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import java.util.HashMap;


public class DatabaseManager {
	
	public DatabaseManager()
	{
		
	}
	private static DatabaseManager instance;

	public DatabaseManager getInstance() {
		if (instance != null) {
			return instance;
		}
		instance = new DatabaseManager();
		return instance;
	}

	public Connection getDatabaseConnection(final int port, final String database) throws SQLException {
		System.out.println(String.format("[mysql]: Connecting to MySQL:%s - %s", port, database));
		return DriverManager.getConnection(
				String.format("jdbc:mysql://localhost:%s/%s?serverTimezone=UTC&useSSL=false", port, database), "root",
				"bichl601");
	}

	public void releaseConnection(final Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	public void insertTradeValues(final Connection connection, String aktie, LocalDate date, boolean flag, int number,
			double depotkonto) {
		String createTable = "create table if not exists " + aktie
				+ "200strategy (Datum varchar(10), aktie varchar(10), flag boolean, number int, depotkonto varchar(10));";
		try (PreparedStatement statement = connection.prepareStatement(createTable)) {
			statement.execute(createTable);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String insertInto = "insert into " + aktie
				+ "200strategy (Datum, aktie, flag, number, depotkonto) values (?,?,?,?,?)";
		try (PreparedStatement statement = connection.prepareStatement(insertInto)) {
			statement.setString(1, date.toString());
			statement.setString(2, aktie);
			statement.setBoolean(3, flag);
			statement.setInt(4, number);
			statement.setDouble(5, depotkonto);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getDatabaseData(final Connection connection, String aktie, HashMap<LocalDate, Double> stockData,
			HashMap<LocalDate, Double> avgStockData) {
		String selectForm = "Select datum, closeSplit from " + aktie + " order by datum asc";
		String selectAvgForm = "Select datum, closeSplit from " + aktie + "AVG order by datum asc";

		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectForm);

			while (rs.next()) {
				stockData.put(LocalDate.parse(rs.getString("Datum")), rs.getDouble("closeSplit"));
			}
			
			rs = stmt.executeQuery(selectAvgForm);
			
			while(rs.next())
			{
				avgStockData.put(LocalDate.parse(rs.getString("Datum")), rs.getDouble("AVG"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
