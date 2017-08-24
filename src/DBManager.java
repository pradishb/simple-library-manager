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
	static PreparedStatement remove_member_stmt;
	static PreparedStatement update_member_stmt;
	static PreparedStatement add_book_stmt;
	static PreparedStatement remove_book_stmt;
	static PreparedStatement update_book_stmt;
	static PreparedStatement issue_book_stmt;
	static PreparedStatement get_books_stmt;

	public static void init_database(){
		try{
			Class.forName("com.mysql.jdbc.Driver");

			System.out.println("Connecting to database...");

			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			stmt = conn.createStatement();			
			add_member_stmt = conn.prepareStatement("INSERT INTO members(id, name, semester, email, books_borrowed) VALUES (0,?,?,?,?)");
			remove_member_stmt = conn.prepareStatement("DELETE FROM members WHERE id=?");
			update_member_stmt = conn.prepareStatement("UPDATE members SET id=0,name=?,semester=?,email=?,books_borrowed=?");
			add_book_stmt = conn.prepareStatement("INSERT INTO books(id, title, author, publication) VALUES (0,?,?,?)");
			remove_book_stmt = conn.prepareStatement("DELETE FROM books WHERE id=?");
			update_book_stmt = conn.prepareStatement("UPDATE books SET id=0,title=?,author=?,publication=?");
			issue_book_stmt = conn.prepareStatement("INSERT INTO borrows(id, borrower_id, book_id, borrowed_date) VALUES (0,?,?,CURRENT_TIMESTAMP)");
			get_books_stmt = conn.prepareStatement("SELECT * FROM books ORDER BY id");
			System.out.println("Connection has been made to database.");
		}catch(SQLException se){
			System.out.println("ERROR: Error while loading from database.");
			System.out.println("Details:");
			se.printStackTrace();
		}catch(Exception e){
			System.out.println("ERROR: Error while loading from database.");
			System.out.println("Details:");
			e.printStackTrace();
		}
	}

	public static void close_database(){
		try{
			stmt.close();
			conn.close();
			System.out.println("Connection closed.");
		}catch(SQLException se){
			System.out.println("ERROR: Error while destroying database objects.");
			System.out.println("Details:");
			se.printStackTrace();
		}

	}
}
