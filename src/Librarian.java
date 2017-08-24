package slm;
import java.sql.*;

public class Librarian{
	public static void add_book(Book myBook){
		try{
			DBManager.add_book_stmt.setString(1,myBook.get_title());
			DBManager.add_book_stmt.setString(2,myBook.get_author());
			DBManager.add_book_stmt.setString(3,myBook.get_publication());
			int r_affected = DBManager.add_book_stmt.executeUpdate();
			System.out.println(r_affected + " book(s) added to database.");
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while adding books to database.");
			System.out.println("Details:");
			se.printStackTrace();
		}
	}

	public static void remove_book(int book_id){
		try{
			DBManager.remove_book_stmt.setInt(1,book_id);
			int r_affected = DBManager.remove_book_stmt.executeUpdate();
			System.out.println(r_affected + " book(s) removed from database.");
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while removing books from database.");
			System.out.println("Details:");
			se.printStackTrace();
		}
	}

	public static void update_book(Book myBook){
		try{
			DBManager.update_book_stmt.setString(1,myBook.get_title());
			DBManager.update_book_stmt.setString(2,myBook.get_author());
			DBManager.update_book_stmt.setString(3,myBook.get_publication());
			int r_affected = DBManager.update_book_stmt.executeUpdate();
			System.out.println(r_affected + " book(s) updated in database.");
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while updating books in database.");
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
			System.out.println(r_affected + " member(s) added to database.");
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while member book to database.");
			System.out.println("Details:");
			se.printStackTrace();
		}
	}

	public static void remove_member(int member_id){
		try{
			DBManager.remove_member_stmt.setInt(1,member_id);
			int r_affected = DBManager.remove_member_stmt.executeUpdate();
			System.out.println(r_affected + " member(s) removed from database.");
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while removing members from database.");
			System.out.println("Details:");
			se.printStackTrace();
		}
	}

	public static void update_member(Member myMember){
		try{
			DBManager.update_member_stmt.setString(1,myMember.get_name());
			DBManager.update_member_stmt.setInt(2,myMember.get_semester());
			DBManager.update_member_stmt.setString(3,myMember.get_email());
			DBManager.update_member_stmt.setInt(3,myMember.get_no_books_borrowed());
			int r_affected = DBManager.update_member_stmt.executeUpdate();
			System.out.println(r_affected + " member(s) updated in database.");
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while updating members in database.");
			System.out.println("Details:");
			se.printStackTrace();
		}
	}

	public static void issue_book(int borrower_id,int book_id){
		try{
			DBManager.issue_book_stmt.setInt(1,borrower_id);
			DBManager.issue_book_stmt.setInt(2,book_id);
			int r_affected = DBManager.issue_book_stmt.executeUpdate();
			System.out.println(r_affected + " book(s) has been issued.");
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while issuing book.");
			System.out.println("Details:");
			se.printStackTrace();
		}
	}
}