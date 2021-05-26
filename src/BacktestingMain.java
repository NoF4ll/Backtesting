import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.TreeMap;

public class BacktestingMain {

	private double depotkonto;
	private int number;
	private boolean flag;
	private LocalDate date;
	private static String aktie;
	private static TreeMap<LocalDate, Double> stockData = new TreeMap<LocalDate, Double>();
	private static TreeMap<LocalDate, Double> avgStockData = new TreeMap<LocalDate, Double>();

	
	private BacktestingMain(String aktie, LocalDate date, boolean flag, int number, double depotkonto) {
		this.aktie = aktie;
		this.date = date;
		this.flag = flag;
		this.number = number;
		this.depotkonto = depotkonto;
	}

	private BacktestingMain() {
	}

	public static void main(String[] args) {
		DatabaseManager database = new DatabaseManager();
		twoHundredStrategy strategy = new twoHundredStrategy();
		buyAndHoldStrategy buyHold = new buyAndHoldStrategy();
		Scanner sc = new Scanner(System.in);
		System.out.println("Wählen sie ein StartDatum");
		String startDate = sc.next();
		boolean flag = false;
		try {
			final Connection connection = database.getInstance().getDatabaseConnection(3306, "aktien");
			database.getDatabaseData(connection, "tsla", stockData, avgStockData,LocalDate.parse(startDate));
			strategy.twoHoundretStrategy(connection, stockData, avgStockData, "tsla", 0.0, 10000, flag);
			buyHold.buyHoldStrategy(connection, "tsla", LocalDate.parse(startDate), 10000, 0);
			System.out.println("Finished");
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
}
