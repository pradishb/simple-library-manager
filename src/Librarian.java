package slm;
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

	public static void add_member(Member myMember){
		try{
			DBManager.add_member_stmt.setString(1,myMember.get_name());
			DBManager.add_member_stmt.setInt(2,myMember.get_semester());
			DBManager.add_member_stmt.setString(3,myMember.get_email());
			DBManager.add_member_stmt.setInt(4,myMember.get_no_books_borrowed());
			int r_affected = DBManager.add_member_stmt.executeUpdate();
			System.out.println(r_affected + " book(s) added to database.");
		}
		catch(SQLException se){
			System.out.println("Error while adding books to database.");
			System.out.println("Details:");
			se.printStackTrace();

		}
	}
}