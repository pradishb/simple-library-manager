package slm;
import java.sql.*;

public class DBManager{
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private final String DB_URL = "jdbc:mysql://localhost:3306/?useSSL=false";
	private final String USER = "root";
	private final String PASS = "";
	Connection conn = null;
	Statement stmt = null;
	PreparedStatement add_member_stmt;
	PreparedStatement remove_member_stmt;
	PreparedStatement update_member_stmt;
	PreparedStatement add_book_stmt;
	PreparedStatement remove_book_stmt;
	PreparedStatement update_book_stmt;
	PreparedStatement issue_book_stmt;
	PreparedStatement return_book_stmt;
	PreparedStatement get_books_stmt;
	PreparedStatement get_book_stmt;
	PreparedStatement get_members_stmt;
	PreparedStatement get_member_stmt;
	PreparedStatement get_transactions_stmt;
	PreparedStatement get_transaction_stmt;
	PreparedStatement get_copies_stmt;
	PreparedStatement search_books_stmt;
	PreparedStatement search_members_stmt;
	PreparedStatement search_transactions_stmt;

	public DBManager(){
		init_database();
	}

	public void init_database(){
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
			stmt.execute("CREATE TABLE IF NOT EXISTS books(id smallint(6) AUTO_INCREMENT, title varchar(100), author varchar(40), publication varchar(40), copies smallint(6), PRIMARY KEY (id))");
			stmt.execute("CREATE TABLE IF NOT EXISTS members(id smallint(6) AUTO_INCREMENT, name varchar(40), semester tinyint(1), email varchar(40), books_borrowed tinyint(4), PRIMARY KEY (id))");
			stmt.execute("CREATE TABLE IF NOT EXISTS transactions(id smallint(6) AUTO_INCREMENT, borrower_id smallint(6), book_id smallint(6), borrowed_date timestamp, PRIMARY KEY (id))");
			stmt.execute("CREATE TABLE IF NOT EXISTS settings(id smallint(6) AUTO_INCREMENT, password varchar(20), threshold tinyint(4), overdue_duration tinyint(4), PRIMARY KEY (id))");
			stmt.execute("CREATE OR REPLACE VIEW copies AS SELECT books.id, 1-(SELECT COUNT(id) FROM transactions WHERE books.id=transactions.book_id) AS remaining FROM books");
			stmt.execute("CREATE OR REPLACE VIEW books_borrowed AS SELECT members.id, (SELECT COUNT(id) FROM transactions WHERE members.id=transactions.borrower_id) AS books_borrowed FROM members");

			//create prepared statements
			add_member_stmt = conn.prepareStatement("INSERT INTO members(id, name, semester, email, books_borrowed) VALUES (0,?,?,?,?)");
			remove_member_stmt = conn.prepareStatement("DELETE FROM members WHERE id=?");
			update_member_stmt = conn.prepareStatement("UPDATE members SET id=0,name=?,semester=?,email=?,books_borrowed=?");
			add_book_stmt = conn.prepareStatement("INSERT INTO books(id, title, author, publication) VALUES (0,?,?,?)");
			remove_book_stmt = conn.prepareStatement("DELETE FROM books WHERE id=?");
			update_book_stmt = conn.prepareStatement("UPDATE books SET id=0,title=?,author=?,publication=?");
			issue_book_stmt = conn.prepareStatement("INSERT INTO transactions(id, borrower_id, book_id, borrowed_date) VALUES (0,?,?,CURRENT_TIMESTAMP)");
			return_book_stmt = conn.prepareStatement("DELETE FROM transactions WHERE id=?");
			get_books_stmt = conn.prepareStatement("SELECT * FROM books ORDER BY id");
			get_book_stmt = conn.prepareStatement("SELECT * FROM books WHERE id=?");
			get_members_stmt = conn.prepareStatement("SELECT * FROM members ORDER BY id");
			get_member_stmt = conn.prepareStatement("SELECT * FROM members WHERE id=?");
			get_copies_stmt = conn.prepareStatement("SELECT * FROM copies WHERE id=?");
			get_transactions_stmt = conn.prepareStatement("SELECT transactions.id, books.title, members.name, transactions.borrowed_date FROM transactions, members, books WHERE transactions.borrower_id=members.id && transactions.book_id=books.id ORDER BY transactions.id");
			get_transaction_stmt = conn.prepareStatement("SELECT * FROM transactions WHERE id=?");
			search_books_stmt = conn.prepareStatement("SELECT * FROM books WHERE id LIKE ? OR title LIKE ? OR author LIKE ? OR publication LIKE ?");
			search_members_stmt = conn.prepareStatement("SELECT * FROM members WHERE id LIKE ? OR name LIKE ? OR email LIKE ? OR semester LIKE ?");
			search_transactions_stmt = conn.prepareStatement("SELECT transactions.id, books.title, members.name, transactions.borrowed_date FROM transactions, members, books WHERE transactions.borrower_id=members.id && transactions.book_id=books.id && (transactions.id LIKE ? OR books.title LIKE ? OR members.name LIKE ? OR transactions.borrowed_date LIKE ?)");
			
		}catch(Exception e){
			System.out.println("ERROR: Error while loading from database.");
			System.out.println("Details:");
			System.out.println(e.toString());
			System.exit(0);
		}
	}

	public void close_database(){
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
