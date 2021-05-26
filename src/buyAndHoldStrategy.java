import java.sql.Connection;
import java.time.LocalDate;
import java.util.TreeMap;
public class buyAndHoldStrategy {

	public buyAndHoldStrategy() {

	}
	
	DatabaseManager database = new DatabaseManager();
	
		public void buyHoldStrategy(final Connection connection, String aktie, LocalDate date, double depotkonto, double amount)
		{
			double startClose = 0,endClose = 0;
			database.selectFirstLast(connection, aktie, startClose, endClose, date);
			amount = depotkonto/startClose;
			depotkonto = depotkonto-(amount * startClose);
			depotkonto = amount * endClose;
			System.out.println(depotkonto);
		}
}
