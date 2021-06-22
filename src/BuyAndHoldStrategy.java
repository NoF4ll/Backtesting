import java.sql.Connection;
import java.time.LocalDate;
public class BuyAndHoldStrategy {

	public BuyAndHoldStrategy() {

	}
	
	DatabaseManager database = new DatabaseManager();
	
		public void buyHoldStrategy(final Connection connection, String aktie, LocalDate date, double depotkonto, 
				double amount,double startClose, double endClose)
		{
			double startKonto = depotkonto;
			amount = depotkonto/startClose;
			depotkonto = depotkonto-(amount * startClose);
			depotkonto = amount * endClose;
			System.out.println("Strategie : BuyAndHold  Aktie : "+aktie+" Startkapital: "+Math.round(startKonto)+" Euro Startdatum: "+date+" Endkonto: "+Math.round(depotkonto)+" Euro");
			database.insertBuyHold(connection, aktie, date, depotkonto,startKonto);
		}
		
}
