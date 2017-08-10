package slm;
import slm.DBManager;
import slm.Book;
import java.sql.*;

public class Librarian{
	public static void add_book(Book b){
		String sql = "INSERT INTO books(id, title, author, publication, borrower_id) VALUES (0,\""+b.get_title()+"\",\""+b.get_author()+"\",\""+b.get_publication()+"\",0)";

		try{
			int r_affected = DBManager.stmt.executeUpdate(sql);
			System.out.println(r_affected + " book(s) added to database.");
		}
		catch(SQLException se){
			System.out.println("Error while adding books to database.");
			System.out.println("Details:");
			se.printStackTrace();

		}
	}
}