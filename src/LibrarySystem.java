import java.sql.*;
import slm.Book;

class LibrarySystem{
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/simple_library_manager?useSSL=false";
	static final String USER = "root";
	static final String PASS = "";
	static int no_of_books = 0;
	static Book[] books;

	public static void main(String args[]){
		load_database();
	}

	public static void load_database(){
		try{
			Class.forName("com.mysql.jdbc.Driver");

			System.out.println("Connecting to database...");
			Connection conn = null;
			Statement stmt = null;

			conn = DriverManager.getConnection(DB_URL,USER,PASS);	

			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM books");
			rs.last();
			no_of_books = rs.getRow();
			rs.beforeFirst();
			
			System.out.println(no_of_books + " book(s) found.");
			System.out.println("Creating book objects...");

			books = new Book[no_of_books];

			while(rs.next()){
				int id  = rs.getInt("id");
				String title = rs.getString("title");
				String author = rs.getString("author");
				String publication = rs.getString("publication");
				int borrower_id = rs.getInt("borrower_id");


				books[rs.getRow()-1] = new Book();
				books[rs.getRow()-1].update_data(id,title,author,publication,borrower_id);				
			}
			rs.close();
			stmt.close();
			conn.close();
			
		}catch(SQLException se){
			System.out.println("Error while loading from database.");
			se.printStackTrace();
		}catch(Exception e){
			System.out.println("Error while loading from database.");
			e.printStackTrace();
		}finally{
			
		}
	}
}
