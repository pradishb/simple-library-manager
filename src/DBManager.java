package slm;
import java.sql.*;

public class DBManager{
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/?useSSL=false";
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
			System.out.println("INFORMATION: Make sure that your database server username is \"root\" and password is \"\"");
			System.out.println("Connecting to database server...");
			
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			System.out.println("Connection has been made to database.");

			stmt = conn.createStatement();

			//create database and tables if not exists
			stmt.execute("CREATE DATABASE IF NOT EXISTS simple_library_manager");
			stmt.execute("USE simple_library_manager");
			stmt.execute("CREATE TABLE IF NOT EXISTS books(id smallint(6) AUTO_INCREMENT, title varchar(20), author varchar(20), publication varchar(20), PRIMARY KEY (id))");
			stmt.execute("CREATE TABLE IF NOT EXISTS members(id smallint(6) AUTO_INCREMENT, name varchar(20), semester tinyint(1), email varchar(20), books_borrowed tinyint(4), PRIMARY KEY (id))");
			stmt.execute("CREATE TABLE IF NOT EXISTS borrows(id smallint(6) AUTO_INCREMENT, borrower_id smallint(6), book_id smallint(6), borrowed_date timestamp, PRIMARY KEY (id))");
			stmt.execute("CREATE TABLE IF NOT EXISTS settings(id smallint(6) AUTO_INCREMENT, password varchar(20), threshold tinyint(4), overdue_duration tinyint(4), PRIMARY KEY (id))");
			//create prepared statements
			add_member_stmt = conn.prepareStatement("INSERT INTO members(id, name, semester, email, books_borrowed) VALUES (0,?,?,?,?)");
			remove_member_stmt = conn.prepareStatement("DELETE FROM members WHERE id=?");
			update_member_stmt = conn.prepareStatement("UPDATE members SET id=0,name=?,semester=?,email=?,books_borrowed=?");
			add_book_stmt = conn.prepareStatement("INSERT INTO books(id, title, author, publication) VALUES (0,?,?,?)");
			remove_book_stmt = conn.prepareStatement("DELETE FROM books WHERE id=?");
			update_book_stmt = conn.prepareStatement("UPDATE books SET id=0,title=?,author=?,publication=?");
			issue_book_stmt = conn.prepareStatement("INSERT INTO borrows(id, borrower_id, book_id, borrowed_date) VALUES (0,?,?,CURRENT_TIMESTAMP)");
			get_books_stmt = conn.prepareStatement("SELECT * FROM books ORDER BY id");
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
