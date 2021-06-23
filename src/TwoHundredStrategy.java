import java.sql.Connection;
import java.time.LocalDate;
import java.util.TreeMap;

public class TwoHundredStrategy {

	public TwoHundredStrategy() {

	}

	DatabaseManager database = new DatabaseManager();

	public void twoHoundretStrategy(Connection connection, TreeMap<LocalDate, Double> stockData,
			TreeMap<LocalDate, Double> avgStockData, String aktie, double amount, double depotkonto, boolean flag,LocalDate date) {

		double lastCloseValue = 0;
		double startKapital = depotkonto;
		LocalDate lastDay = null;
		for (LocalDate i : stockData.keySet()) {
			if (stockData.get(i) >= avgStockData.get(i) && flag == false) {
				// System.out.println(i + " " + avgStockData.get(i) + " | AktienPreis: " +
				// stockData.get(i));
				amount = (depotkonto / stockData.get(i));
				depotkonto = depotkonto % stockData.get(i);
				flag = true;
				// System.out.println(amount + " Stück " + i + " " + flag + " Konto : " +
				// depotkonto + " ");
				database.insertTradeValues(connection, aktie, i, flag, amount, depotkonto);
			}
			if (stockData.get(i) < avgStockData.get(i) && flag == true) {
				depotkonto = depotkonto + (amount * stockData.get(i));
				amount = 0;
				flag = false;
				// System.out.println(amount + "Stück " + i + " " + flag + " Konto : " +
				// depotkonto + " ");
				database.insertTradeValues(connection, aktie, i, flag, amount, depotkonto);
			}
			lastCloseValue = stockData.get(i);
			lastDay = i;
		}
		if (flag == true) {
			depotkonto = depotkonto + lastCloseValue * amount;
			
			database.insertTradeValues(connection, aktie, lastDay, !flag, 0, depotkonto);
		}
		System.out.println("\nStrategie: 200er  | Aktie : "+aktie+" Startkapital: "+Math.round(startKapital)+" Euro Startdatum: "+date+" Endkapital: "+Math.round(depotkonto)+" Euro");
	}
	
	public void twoHoundretStrategyOptimize(Connection connection, TreeMap<LocalDate, Double> stockData,
			TreeMap<LocalDate, Double> avgStockData, String aktie, double amount, double depotkonto, boolean flag,LocalDate date) {

		double lastCloseValue = 0;
		double startKapital = depotkonto;
		LocalDate lastDay = null;
		for (LocalDate i : stockData.keySet()) {
			if (stockData.get(i) >= (avgStockData.get(i) * 1.03) && flag == false) {
				// System.out.println(i + " " + avgStockData.get(i) + " | AktienPreis: " +
				// stockData.get(i));
				amount = (depotkonto / stockData.get(i));
				depotkonto = depotkonto % stockData.get(i);
				flag = true;
				database.insertTradeValuesOptimize(connection, aktie, i, flag, amount, depotkonto);
			}
			if (stockData.get(i) < (avgStockData.get(i) * 0.97) && flag == true) {
				depotkonto = depotkonto + (amount * stockData.get(i));
				amount = 0;
				flag = false;
				database.insertTradeValuesOptimize(connection, aktie, i, flag, amount, depotkonto);
			}
			lastCloseValue = stockData.get(i);
			lastDay = i;
		}
		if (flag == true) {
			depotkonto = depotkonto + lastCloseValue * amount;
			database.insertTradeValuesOptimize(connection, aktie, lastDay, flag, 0, depotkonto);
		}
		System.out.println("Strategie : 200erOptimiert  | Aktie : "+aktie+" Startkapital: "+Math.round(startKapital)+" Euro Startdatum: "+date+" Endkapital: "+Math.round(depotkonto)+" Euro");
	}
	
}