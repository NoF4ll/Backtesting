import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import java.util.TreeMap;

public class DatabaseManager {

	

	public DatabaseManager() {

	}

	private static DatabaseManager instance;

	public DatabaseManager getInstance() {
		if (instance != null) {
			return instance;
		}
		instance = new DatabaseManager();
		return instance;
	}

	public Connection getDatabaseConnection(final int port, final String database,String user, String password) throws SQLException {
		System.out.println(String.format("[mysql]: Connecting to MySQL:%s - %s", port, database));
		
		return DriverManager.getConnection(
				String.format("jdbc:mysql://localhost:%s/%s?serverTimezone=UTC&useSSL=false", port, database), user,
				password);
		
	}

	public void releaseConnection(final Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}
	
	public void getDatabaseData(final Connection connection, String aktie, TreeMap<LocalDate, Double> stockData,
			TreeMap<LocalDate, Double> avgStockData, LocalDate startDate) {
		String selectForm = "Select datum, closeSplit from " + aktie + " where datum >= '" + startDate + "'";
		String selectAvgForm = "Select datum, avg from " + aktie + "AVG where datum >= '" + startDate + "'";

		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectForm);

			while (rs.next()) {
				stockData.put(LocalDate.parse(rs.getString("Datum")), rs.getDouble("closeSplit"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectAvgForm);

			while (rs.next()) {
				avgStockData.put(LocalDate.parse(rs.getString("Datum")), rs.getDouble("AVG"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void insertTradeValues(final Connection connection, String aktie, LocalDate date, boolean flag,
			double amount, double depotkonto) {
		String createTable = "create table if not exists " + aktie
				+ "200strategy (Datum varchar(10), aktie varchar(10), flag boolean, amount int, depotkonto decimal(10,2), PRIMARY KEY(Datum));";
		try (PreparedStatement statement = connection.prepareStatement(createTable)) {
			statement.execute(createTable);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String insertInto = "Replace into " + aktie
				+ "200strategy (Datum, aktie, flag, amount, depotkonto) values (?,?,?,?,?);";
		try (PreparedStatement statement = connection.prepareStatement(insertInto)) {
			statement.setString(1, date.toString());
			statement.setString(2, aktie);
			statement.setBoolean(3, flag);
			statement.setInt(4, (int) amount);
			statement.setDouble(5, depotkonto);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertTradeValuesOptimize(final Connection connection, String aktie, LocalDate date, boolean flag,
			double amount, double depotkonto) {
		String createTable = "create table if not exists " + aktie
				+ "200strategyOptimize (Datum varchar(10), aktie varchar(10), flag boolean, amount int, depotkonto decimal(10,2), PRIMARY KEY(Datum));";
		try (PreparedStatement statement = connection.prepareStatement(createTable)) {
			statement.execute(createTable);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String insertInto = "Replace into " + aktie
				+ "200strategyOptimize (Datum, aktie, flag, amount, depotkonto) values (?,?,?,?,?);";
		try (PreparedStatement statement = connection.prepareStatement(insertInto)) {
			if (amount == 0) {
			}
			statement.setString(1, date.toString());
			statement.setString(2, aktie);
			statement.setBoolean(3, flag);
			statement.setInt(4, (int) amount);
			statement.setDouble(5, depotkonto);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertBuyHold(final Connection connection, String aktie, LocalDate date, double depotkonto,
			double startKonto) {
		String createTable = "create table if not exists " + aktie
				+ "buyHold (Datum varchar(10), aktie varchar(10), depotkonto decimal(10,2), startMoney decimal(10,2), PRIMARY KEY(Datum));";
		try (PreparedStatement statement = connection.prepareStatement(createTable)) {
			statement.execute(createTable);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String insertInto = "Replace into " + aktie
				+ "buyHold (Datum, aktie, depotkonto, startMoney) values (?,?,?,?);";
		try (PreparedStatement statement = connection.prepareStatement(insertInto)) {
			statement.setString(1, date.toString());
			statement.setString(2, aktie);
			statement.setDouble(3, depotkonto);
			statement.setDouble(4, startKonto);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public double selectFirst(Connection connection, String aktie, LocalDate startDate) {
		String selectStartClose = "Select closeSplit from " + aktie + " where datum >'" + startDate
				+ "' order by datum asc LIMIT 1;";

		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectStartClose);

			while (rs.next()) {
				return (rs.getDouble("closeSplit"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 0;

	}

	public double selectLast(Connection connection, String aktie, LocalDate startDate) {
		String selectEndClose = "Select closeSplit from " + aktie + " where datum > '" + startDate
				+ "' order by datum desc LIMIT 1;";
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectEndClose);

			while (rs.next()) {
				return (rs.getDouble("closeSplit"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 0;

	}
/*
	public void selectDepotKonto(Connection connection, String aktie, double twoHundretStrategy,
			double twoHundretOptimized, double buyHold) {
		String selectBuyHold = "Select depotkonto from " + aktie + "buyHold order by datum desc LIMIT 1;";
		String selectTwoHundretOptimized = "Select depotkonto from " + aktie
				+ "200StrategyOptimize order by datum desc LIMIT 1;";
		String selectTwoHundret = "Select depotkonto from " + aktie + "200Strategy order by datum desc LIMIT 1;";
	}
*/

}
