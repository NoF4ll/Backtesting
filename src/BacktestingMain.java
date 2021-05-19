import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;



public class BacktestingMain {

	private int i;
	private double depotkonto;
	private int number;
	private boolean flag;
	private LocalDate date;
	private String aktie;
	private static HashMap<LocalDate, Double> stockData,avgStockData;
	

	private BacktestingMain(String aktie, LocalDate date, boolean flag, int number,double depotkonto) {
		this.aktie= aktie;
		this.date=date;
		this.flag = flag;
		this.number = number;
		this.depotkonto=depotkonto;
	}
	private BacktestingMain()
	{
		
	}

	public static void main(String[] args) {
		
		DatabaseManager user = new DatabaseManager();
		try {
			final Connection connection = user.getInstance().getDatabaseConnection(3306, "aktien");
			user.getDatabaseData(connection, "tsla", stockData, avgStockData);
			System.out.println(stockData.isEmpty());
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	

}
