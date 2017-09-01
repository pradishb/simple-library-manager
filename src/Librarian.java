package slm;
import java.sql.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import com.Ostermiller.util.LabeledCSVParser;
import com.Ostermiller.util.CSVParser;

class InvalidCsvFormatException extends Exception{
	public InvalidCsvFormatException(){
		super("ERROR: The CSV file is not valid.");
	}
}

public class Librarian{
	private LibraryInterface library_interface;
	private DBManager dm;
	public Librarian(DBManager dm, LibraryInterface library_interface){
		this.dm = dm;
		this.library_interface = library_interface;
	}

	public int add_book(Book myBook){
		int r_affected = 0;
		try{
			dm.add_book_stmt.setString(1,myBook.get_title());
			dm.add_book_stmt.setString(2,myBook.get_author());
			dm.add_book_stmt.setString(3,myBook.get_publication());
			r_affected = dm.add_book_stmt.executeUpdate();
		}
		catch(Exception se){
			System.out.println("ERROR: Error while adding books to database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return r_affected;
	}

	public void remove_book(int book_id){
		try{
			dm.remove_book_stmt.setInt(1,book_id);
			int r_affected = dm.remove_book_stmt.executeUpdate();
			System.out.println(r_affected + " book(s) removed from database.");
		}
		catch(Exception se){
			System.out.println("ERROR: Error while removing books from database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
	}

	public void update_book(Book myBook){
		try{
			dm.update_book_stmt.setString(1,myBook.get_title());
			dm.update_book_stmt.setString(2,myBook.get_author());
			dm.update_book_stmt.setString(3,myBook.get_publication());
			int r_affected = dm.update_book_stmt.executeUpdate();
			System.out.println(r_affected + " book(s) updated in database.");
		}
		catch(Exception se){
			System.out.println("ERROR: Error while updating books in database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
	}

	public int add_member(Member myMember){
		int r_affected = 0;
		try{
			dm.add_member_stmt.setString(1,myMember.get_name());
			dm.add_member_stmt.setInt(2,myMember.get_semester());
			dm.add_member_stmt.setString(3,myMember.get_email());
			dm.add_member_stmt.setInt(4,myMember.get_no_books_borrowed());
			r_affected = dm.add_member_stmt.executeUpdate();
		}
		catch(Exception se){
			System.out.println("ERROR: Error while member book to database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return r_affected;
	}

	public void remove_member(int member_id){
		try{
			dm.remove_member_stmt.setInt(1,member_id);
			int r_affected = dm.remove_member_stmt.executeUpdate();
			System.out.println(r_affected + " member(s) removed from database.");
		}
		catch(Exception se){
			System.out.println("ERROR: Error while removing members from database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
	}

	public void update_member(Member myMember){
		try{
			dm.update_member_stmt.setString(1,myMember.get_name());
			dm.update_member_stmt.setInt(2,myMember.get_semester());
			dm.update_member_stmt.setString(3,myMember.get_email());
			dm.update_member_stmt.setInt(3,myMember.get_no_books_borrowed());
			int r_affected = dm.update_member_stmt.executeUpdate();
			System.out.println(r_affected + " member(s) updated in database.");
		}
		catch(Exception se){
			System.out.println("ERROR: Error while updating members in database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
	}

	public void issue_book(int borrower_id,int book_id){
		try{
			dm.issue_book_stmt.setInt(1,borrower_id);
			dm.issue_book_stmt.setInt(2,book_id);
			int r_affected = dm.issue_book_stmt.executeUpdate();
			System.out.println(r_affected + " book(s) has been issued.");
		}
		catch(Exception se){
			System.out.println("ERROR: Error while issuing book.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
	}

	public Vector<Book> search_books(String myString){
		Vector<Book> result = new Vector<Book>();
		try{
			dm.search_books_stmt.setString(1, "%"+myString+"%");
			dm.search_books_stmt.setString(2, "%"+myString+"%");
			dm.search_books_stmt.setString(3, "%"+myString+"%");
			dm.search_books_stmt.setString(4, "%"+myString+"%");
			ResultSet rs = dm.search_books_stmt.executeQuery();
			while(rs.next()){
				Book temp = new Book(rs.getInt("id"),rs.getString("title"),rs.getString("author"),rs.getString("publication"));
				result.addElement(temp);
			}
		}
		catch(Exception se){
			System.out.println("ERROR: Error while searching books.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return result;
	}

	public Vector<Member> search_members(String myString){
		Vector<Member> result = new Vector<Member>();
		try{
			dm.search_members_stmt.setString(1, "%"+myString+"%");
			dm.search_members_stmt.setString(2, "%"+myString+"%");
			dm.search_members_stmt.setString(3, "%"+myString+"%");
			dm.search_members_stmt.setString(4, "%"+myString+"%");
			ResultSet rs = dm.search_members_stmt.executeQuery();
			while(rs.next()){
				Member temp = new Member(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getInt("semester"),rs.getInt("books_borrowed"));
				result.addElement(temp);
			}
		}
		catch(Exception se){
			System.out.println("ERROR: Error while searching members.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return result;
	}

	public Vector<TransactionDisplay> search_transactions(String myString){
		Vector<TransactionDisplay> result = new Vector<TransactionDisplay>();
		try{
			dm.search_transactions_stmt.setString(1, "%"+myString+"%");
			dm.search_transactions_stmt.setString(2, "%"+myString+"%");
			dm.search_transactions_stmt.setString(3, "%"+myString+"%");
			dm.search_transactions_stmt.setString(4, "%"+myString+"%");
			ResultSet rs = dm.search_transactions_stmt.executeQuery();
			while(rs.next()){
				TransactionDisplay temp = new TransactionDisplay(rs.getInt("id"),rs.getString("title"),rs.getString("name"),rs.getTimestamp("borrowed_date"));
				result.addElement(temp);
			}
		}
		catch(Exception se){
			System.out.println("ERROR: Error while searching transactions.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return result;
	}

	public Member get_member(int id){
		Member result = new Member(0,"","",0,0);
		try{
			dm.get_member_stmt.setInt(1, id);
			ResultSet rs = dm.get_member_stmt.executeQuery();
			if(rs.next()){
				result.update_data(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getInt("semester"),rs.getInt("books_borrowed"));
			}
		}
		catch(Exception se){
			System.out.println("ERROR: Error while loading member from database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return result;
	}

	public Book get_book(int id){
		Book result = new Book(0,"","","");
		try{
			dm.get_book_stmt.setInt(1, id);
			ResultSet rs = dm.get_book_stmt.executeQuery();
			if(rs.next()){
				result.update_data(rs.getInt("id"),rs.getString("title"),rs.getString("author"),rs.getString("publication"));
			}
		}
		catch(Exception se){
			System.out.println("ERROR: Error while loading book from database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return result;
	}

	public Vector<Book> get_books(){
		Vector<Book> result = new Vector<Book>();
		
		try{
			ResultSet rs = dm.get_books_stmt.executeQuery();
			
			while(rs.next()){
				Book temp = new Book(rs.getInt("id"),rs.getString("title"),rs.getString("author"),rs.getString("publication"));
				result.addElement(temp);
			}
			
		}
		catch(Exception se){
			System.out.println("ERROR: Error while load books from database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return result;
	}

	public Vector<Member> get_members(){
		Vector<Member> result = new Vector<Member>();
		
		try{
			ResultSet rs = dm.get_members_stmt.executeQuery();
			
			while(rs.next()){
				Member temp = new Member(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getInt("semester"),rs.getInt("books_borrowed"));
				result.addElement(temp);
			}
			
		}
		catch(Exception se){
			System.out.println("ERROR: Error while loading members from database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return result;
	}

	public Vector<TransactionDisplay> get_transactions(){
		Vector<TransactionDisplay> result = new Vector<TransactionDisplay>();
		
		try{
			ResultSet rs = dm.get_transactions_stmt.executeQuery();
			
			while(rs.next()){
				TransactionDisplay temp = new TransactionDisplay(rs.getInt("id"),rs.getString("title"),rs.getString("name"),rs.getTimestamp("borrowed_date"));
				result.addElement(temp);
			}
			
		}
		catch(Exception se){
			System.out.println("ERROR: Error while loading transactions from database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return result;
	}

	public void import_books(File myFile) throws InvalidCsvFormatException{
		int count = 0;
		try{
			LabeledCSVParser lcsvp = new LabeledCSVParser(new CSVParser(new FileReader(myFile)));
			String[] cells = lcsvp.getLabels();
			if(cells[0].equals("title") && cells[1].equals("author") && cells[2].equals("publication") && cells.length==3){
				while(lcsvp.getLine() != null){
					Book myBook = new Book(0,lcsvp.getValueByLabel("title"),lcsvp.getValueByLabel("author"),lcsvp.getValueByLabel("publication"));
					count += add_book(myBook);
				}	
			}
			else{
				throw new InvalidCsvFormatException();				//when the 1st line of CSV is invalid
			}
		}catch(IOException e){
			System.out.println("ERROR: Error while importing books.");
			System.out.println("Details:");
			e.printStackTrace();
		}
		System.out.println(count + " book(s) has been added to database.");
	}

	public void import_members(File myFile) throws InvalidCsvFormatException{
		int count = 0;
		try{
			LabeledCSVParser lcsvp = new LabeledCSVParser(new CSVParser(new FileReader(myFile)));
			String[] cells = lcsvp.getLabels();
			if(cells[0].equals("name") && cells[1].equals("email") && cells[2].equals("semester") && cells.length==3){
				while(lcsvp.getLine() != null){
					try{
						Member myMember = new Member(0,lcsvp.getValueByLabel("name"),lcsvp.getValueByLabel("email"),Integer.parseInt(lcsvp.getValueByLabel("semester")),0);
						count += add_member(myMember);
					}
					catch(NumberFormatException e){
						throw new InvalidCsvFormatException();		
					}
				}	
			}
			else{
				throw new InvalidCsvFormatException();				//when the 1st line of CSV is invalid
			}
		}catch(IOException e){
			System.out.println("ERROR: Error while importing members.");
			System.out.println("Details:");
			e.printStackTrace();
		}
		System.out.println(count + " member(s) added to database.");
	}

	public Object[][] books_to_array(Vector<Book> books){
		Object[][] result = new Object[books.size()][4];

		for(int i=0; i<books.size();i++){
			result[i][0]=books.elementAt(i).get_id();
			result[i][1]=books.elementAt(i).get_title();
			result[i][2]=books.elementAt(i).get_author();
			result[i][3]=books.elementAt(i).get_publication();
		}
		return result;
	}

	public Object[][] members_to_array(Vector<Member> members){
		Object[][] result = new Object[members.size()][5];

		for(int i=0; i<members.size();i++){
			result[i][0]=members.elementAt(i).get_id();
			result[i][1]=members.elementAt(i).get_name();
			result[i][2]=members.elementAt(i).get_email();
			result[i][3]=members.elementAt(i).get_semester();
			result[i][4]=members.elementAt(i).get_no_books_borrowed();
		}
		return result;
	}

	public Object[][] transactions_to_array(Vector<TransactionDisplay> transactions){
		Object[][] result = new Object[transactions.size()][4];

		for(int i=0; i<transactions.size();i++){
			result[i][0]=transactions.elementAt(i).get_id();
			result[i][1]=transactions.elementAt(i).get_book_title();
			result[i][2]=transactions.elementAt(i).get_borrower_name();
			result[i][3]=transactions.elementAt(i).get_borrowed_date().toString();
		}
		return result;
	}
	
	public void update_table(JTable table, String[] cols, Object[][]  data){
		table.setModel(new DefaultTableModel(data, cols));
		table.getColumn("ID").setMaxWidth(30);
	}
}