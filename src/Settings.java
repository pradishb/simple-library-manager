package slm;

import java.sql.*;

public class Settings{
	static int threshold;
	static int due;
	static int price;

	static void update(DBManager db_manager){
		try{
			ResultSet rs = db_manager.get_settings_stmt.executeQuery();
			if(rs.next()){
				threshold = rs.getInt("threshold");
				due = rs.getInt("overdue_duration");
				price = rs.getInt("fine_per_day");
			}
		}
		catch(Exception se){
			System.out.println("ERROR: Error while fetching settings from database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
	}	
}
