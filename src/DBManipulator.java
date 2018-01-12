package slm;

import java.sql.*;

public class DBManipulator{
	private static DBManager manager;

	public DBManipulator(DBManager db){
		manager = db;
	}

	public static void increment_semester(){
		try{
			manager.stmt.execute("UPDATE members SET semester=semester+1");
		}
		catch(SQLException e){
			System.out.println(e.toString());

		}
	}
}