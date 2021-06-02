import java.sql.Connection;
import java.time.LocalDate;
public class buyAndHoldStrategy {

	public buyAndHoldStrategy() {

	}
	
	DatabaseManager database = new DatabaseManager();
	
		public void buyHoldStrategy(final Connection connection, String aktie, LocalDate date, double depotkonto, 
				double amount,double startClose, double endClose)
		{
			double startKonto = depotkonto;
			amount = depotkonto/startClose;
			depotkonto = depotkonto-(amount * startClose);
			depotkonto = amount * endClose;
			database.insertBuyHold(connection, aktie, date, depotkonto,startKonto);
		}
		
}
