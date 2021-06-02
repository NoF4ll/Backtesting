import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.TreeMap;

public class BacktestingMain {

	private static TreeMap<LocalDate, Double> stockData = new TreeMap<LocalDate, Double>();
	private static TreeMap<LocalDate, Double> avgStockData = new TreeMap<LocalDate, Double>();


	private BacktestingMain() {
	}

	public static void main(String[] args) {
		DatabaseManager database = new DatabaseManager();
		twoHundredStrategy strategy = new twoHundredStrategy();
		buyAndHoldStrategy buyHold = new buyAndHoldStrategy();
		double startClose = 0;
		double endClose = 0;
		Scanner sc = new Scanner(System.in);
		System.out.println("Wählen sie ein StartDatum");
		String startDate = sc.next();
		boolean flag = false;
		try {
			final Connection connection = database.getInstance().getDatabaseConnection(3306, "aktien");
			database.getDatabaseData(connection, "tsla", stockData, avgStockData,LocalDate.parse(startDate));
			strategy.twoHoundretStrategy(connection, stockData, avgStockData, "tsla", 0.0, 10000, flag);
			startClose = database.selectFirst(connection, "tsla", LocalDate.parse(startDate));
			endClose = database.selectLast(connection, "tsla", LocalDate.parse(startDate));
			buyHold.buyHoldStrategy(connection, "tsla", LocalDate.parse(startDate), 10000, 0, startClose, endClose);
			strategy.twoHoundretStrategyOptimize(connection, stockData, avgStockData, "tsla", 0.0, 10000, flag);
			//System.out.println(startClose+" "+endClose);
			System.out.println("Finished");
			sc.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
}
