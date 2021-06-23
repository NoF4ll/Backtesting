import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;



public class ConfigFileManager<Propertie> {

	private String user;
	private String password;
	private LocalDate date;
	private double depotkonto;
	private ArrayList<String> aktien = new ArrayList<String>();;
	
	public ArrayList<String> getAktien() {
		return aktien;
	}

	public ConfigFileManager() {
		this.readConfigFile();
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public LocalDate getDate() {
		return date;
	}

	public double getDepotkonto() {
		return depotkonto;
	}

	public void readConfigFile() {
		try {
			Properties prop = new Properties();
			FileInputStream config = new FileInputStream(
					"C:\\Users\\Maximilian Neuner\\Documents\\Java\\BacktestingSoftware\\ConfigFile\\config.properties");
			
			prop.load(config);
			
			this.user = prop.getProperty("user");
			this.password = prop.getProperty("password");
			this.date = LocalDate.parse(prop.getProperty("startDate"));
			this.depotkonto = Double.valueOf((prop.getProperty("depotkonto")));
			
			int i = 1;
			
			while(prop.getProperty("aktie"+i)!=null)
			{
				this.aktien.add(prop.getProperty("aktie"+i));
				i++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
