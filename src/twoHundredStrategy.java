import java.sql.Connection;
import java.time.LocalDate;
import java.util.TreeMap;

public class twoHundredStrategy {

	public twoHundredStrategy() {

	}

	DatabaseManager database = new DatabaseManager();

	public void twoHoundretStrategy(Connection connection, TreeMap<LocalDate, Double> stockData,
			TreeMap<LocalDate, Double> avgStockData, String aktie, double amount, double depotkonto, boolean flag) {

		double lastCloseValue = 0;
		LocalDate lastDay = null;
		for (LocalDate i : stockData.keySet()) {
			if (stockData.get(i) <= avgStockData.get(i) && flag == false) {
				// System.out.println(i + " " + avgStockData.get(i) + " | AktienPreis: " +
				// stockData.get(i));
				amount = (depotkonto / stockData.get(i));
				depotkonto = depotkonto % stockData.get(i);
				flag = true;
				// System.out.println(amount + " Stück " + i + " " + flag + " Konto : " +
				// depotkonto + " ");
				database.insertTradeValues(connection, aktie, i, flag, amount, depotkonto);
			}
			if (stockData.get(i) > avgStockData.get(i) && flag == true) {
				depotkonto = (amount * stockData.get(i));
				amount = amount - (depotkonto / stockData.get(i));
				flag = false;
				// System.out.println(amount + "Stück " + i + " " + flag + " Konto : " +
				// depotkonto + " ");
				database.insertTradeValues(connection, aktie, i, flag, amount, depotkonto);
			}
			lastCloseValue = stockData.get(i);
			lastDay = i;
		}
		if (amount != 0) {
			depotkonto = lastCloseValue * amount;
			System.out.println(lastCloseValue * amount);
			database.insertTradeValues(connection, aktie, lastDay, flag, 0, depotkonto);
		}

	}

	public void twoHoundretStrategyOptimize(Connection connection, TreeMap<LocalDate, Double> stockData,
			TreeMap<LocalDate, Double> avgStockData, String aktie, double amount, double depotkonto, boolean flag) {

		double lastCloseValue = 0;
		LocalDate lastDay = null;
		for (LocalDate i : stockData.keySet()) {

			if (stockData.get(i) <= (avgStockData.get(i) * 0.97) && flag == false) {
				// System.out.println(i + " " + avgStockData.get(i) + " | AktienPreis: " +
				// stockData.get(i));
				amount = (depotkonto / stockData.get(i));
				depotkonto = depotkonto % stockData.get(i);
				flag = true;
				database.insertTradeValuesOptimize(connection, aktie, i, flag, amount, depotkonto);

			}
			if (stockData.get(i) > (avgStockData.get(i) * 1.03) && flag == true) {
				depotkonto = (amount * stockData.get(i));
				amount = amount - (depotkonto / stockData.get(i));
				flag = false;
				database.insertTradeValuesOptimize(connection, aktie, i, flag, amount, depotkonto);
			}
			lastCloseValue = stockData.get(i);
			lastDay = i;
		}
		if (amount != 0) {
			depotkonto = lastCloseValue * amount;
			System.out.println(depotkonto);
			database.insertTradeValuesOptimize(connection, aktie, lastDay, flag, 0, depotkonto);
		}
	}
}	