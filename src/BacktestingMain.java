import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

public class BacktestingMain {

	private static TreeMap<LocalDate, Double> stockData = new TreeMap<LocalDate, Double>();
	private static TreeMap<LocalDate, Double> avgStockData = new TreeMap<LocalDate, Double>();
	
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
		
		try {
			final Connection connection = database.getInstance().getDatabaseConnection(3306, "aktien",config.getUser(),config.getPassword());
			ArrayList<String> aktien =  config.getAktien();
				
				for(String i : aktien)
				{
					System.out.println(i);
					database.getDatabaseData(connection, i, stockData, avgStockData, config.getDate());
					strategy.twoHoundretStrategy(connection, stockData, avgStockData, i, 0.0, config.getDepotkonto(), false,config.getDate());
					startClose = database.selectFirst(connection, i, config.getDate());	
					endClose = database.selectLast(connection, i, config.getDate());
					buyHold.buyHoldStrategy(connection, i, config.getDate(), config.getDepotkonto(), 0, startClose, endClose);
					strategy.twoHoundretStrategyOptimize(connection, stockData, avgStockData, i, 0.0, config.getDepotkonto(), false,config.getDate());
					stockData.clear();
					avgStockData.clear();
				}
			
			System.out.println("Finished");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}