import java.sql.Connection;
import java.time.LocalDate;
import java.util.TreeMap;

public class twoHundredStrategy {

	public twoHundredStrategy() {
		
	}

	DatabaseManager database = new DatabaseManager();

	public void twoHoundretStrategy(Connection connection, TreeMap<LocalDate, Double> stockData,
			TreeMap<LocalDate, Double> avgStockData, String aktie, double amount, double depotkonto, boolean flag) {

		for (LocalDate i : stockData.keySet()) {
			// System.out.println(i + " " + avgStockData.get(i) + " | " + stockData.get(i));
			if (stockData.get(i) <= avgStockData.get(i) && flag == false) {
				amount = (depotkonto / stockData.get(i));
				depotkonto = depotkonto - (amount * stockData.get(i));
				flag = true;
				System.out.println(amount + " " + i + " " + flag + " " + depotkonto + " ");
				database.insertTradeValues(connection, aktie, i, flag, amount, depotkonto);
			}
			if (stockData.get(i) > avgStockData.get(i) && flag == true) {
				depotkonto = (amount * stockData.get(i));
				amount = amount - (depotkonto / stockData.get(i));
				flag = false;
				System.out.println(amount + " " + i + " " + flag + " " + depotkonto + " ");
				database.insertTradeValues(connection, aktie, i, flag, amount, depotkonto);
			}
		}
	}
}