package slm;
import java.sql.*;

public class DBManager{
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/simple_library_manager?useSSL=false";
	static final String USER = "root";
	static final String PASS = "";
	static Connection conn = null;
	static Statement stmt = null;
	static PreparedStatement add_member_stmt;
	static PreparedStatement add_book_stmt;
	static PreparedStatement issue_book_stmt;

	public static void init_database(){
		try{
			Class.forName("com.mysql.jdbc.Driver");

			System.out.println("Connecting to database...");

			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			stmt = conn.createStatement();			
			add_member_stmt = conn.prepareStatement("INSERT INTO members(id, name, semester, email, books_borrowed) VALUES (0,?,?,?,?)");
			add_book_stmt = conn.prepareStatement("INSERT INTO books(id, title, author, publication) VALUES (0,?,?,?)");
			issue_book_stmt = conn.prepareStatement("INSERT INTO borrows(id, borrower_id, book_id, borrowed_date) VALUES (0,?,?,CURRENT_TIMESTAMP)");
		}catch(SQLException se){
			System.out.println("Error while loading from database.");
			System.out.println("Details:");
			se.printStackTrace();
		}catch(Exception e){
			System.out.println("Error while loading from database.");
			System.out.println("Details:");
			e.printStackTrace();
		}
	}

	public static void close_database(){
		try{
			stmt.close();
			conn.close();
		}catch(SQLException se){
			System.out.println("Error while destroying database objects.");
			System.out.println("Details:");
			se.printStackTrace();
		}

	}
}
