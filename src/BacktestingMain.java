import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.TreeMap;

public class BacktestingMain {

	private static TreeMap<LocalDate, Double> stockData = new TreeMap<LocalDate, Double>();
	private static TreeMap<LocalDate, Double> avgStockData = new TreeMap<LocalDate, Double>();
	static File file = new File(
			"C:\\Users\\Maximilian Neuner\\Documents\\Java\\BacktestingSoftware\\bin\\aktienStrategien.txt");
	
	private static String aktie;
	
	private static String user;
	private static String password;
	private static int depotkonto;
	private static String startDate;
	
	static double startClose = 0;
	static double endClose = 0;

	private BacktestingMain() {
	}

	public static void main(String[] args) {
		DatabaseManager database = new DatabaseManager();
		TwoHundredStrategy strategy = new TwoHundredStrategy();
		BuyAndHoldStrategy buyHold = new BuyAndHoldStrategy();
		ConfigFileManager config = new ConfigFileManager();
		
		System.out.println(config.getUser());
		
		
		System.out.println(user+password+startDate+depotkonto);
		
		final double depotkonto = 10000;
		//Scanner sc = new Scanner(System.in);
		//System.out.println("Wählen sie ein StartDatum");
		//String startDate = sc.next();
		boolean flag = false;
		try {
			final Connection connection = database.getInstance().getDatabaseConnection(3306, "aktien");
			Scanner fileReader = new Scanner(file);
			while (fileReader.hasNextLine()) {
				
				aktie = fileReader.nextLine();
			
				
				database.getDatabaseData(connection, aktie, stockData, avgStockData, config.getDate());
				strategy.twoHoundretStrategy(connection, stockData, avgStockData, aktie, 0.0, config.getDepotkonto(), false);
				startClose = database.selectFirst(connection, aktie, config.getDate());	
				endClose = database.selectLast(connection, aktie, config.getDate());
				buyHold.buyHoldStrategy(connection, aktie, config.getDate(), config.getDepotkonto(), 0, startClose, endClose);
				strategy.twoHoundretStrategyOptimize(connection, stockData, avgStockData, aktie, 0.0, config.getDepotkonto(), false);
				stockData.clear();
				avgStockData.clear();
				// System.out.println(startClose+" "+endClose);
				
			}
			System.out.println("Finished");
			//sc.close();
			fileReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
