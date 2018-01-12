package slm;
import java.sql.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import javax.swing.event.*;
import com.Ostermiller.util.LabeledCSVParser;
import com.Ostermiller.util.CSVParser;

class InvalidCsvFormatException extends Exception{
	private Vector<String> cols;
	public InvalidCsvFormatException(Vector<String> cols){
		super("ERROR: The CSV file is not valid.");
		this.cols = cols;
	}
	public Vector<String> get_missing_cols(){
		return cols;
	}
}

public class Librarian{
	private DBManager dm;

	public Librarian(DBManager dm){
		this.dm = dm;
	}

	public int add_book(Book myBook){
		int r_affected = 0;
		try{
			dm.add_book_stmt.setString(1,myBook.get_isbn());
			dm.add_book_stmt.setString(2,myBook.get_title());
			dm.add_book_stmt.setString(3,myBook.get_author());
			dm.add_book_stmt.setString(4,myBook.get_publication());
			dm.add_book_stmt.setInt(5,myBook.get_copies());
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
			dm.stmt.execute("DELETE FROM transactions WHERE book_id="+book_id);
			int r_affected = dm.remove_book_stmt.executeUpdate();
		}
		catch(Exception se){
			System.out.println("ERROR: Error while removing books from database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
	}

	public void update_book(Book myBook){
		try{
			dm.update_book_stmt.setString(1,myBook.get_isbn());
			dm.update_book_stmt.setString(2,myBook.get_title());
			dm.update_book_stmt.setString(3,myBook.get_author());
			dm.update_book_stmt.setString(4,myBook.get_publication());
			dm.update_book_stmt.setInt(5,myBook.get_copies());
			dm.update_book_stmt.setInt(6,myBook.get_id());
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
			r_affected = dm.add_member_stmt.executeUpdate();
		}
		catch(Exception se){
			System.out.println("ERROR: Error while member to database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return r_affected;
	}

	public void remove_member(int member_id){
		try{
			dm.remove_member_stmt.setInt(1,member_id);
			dm.stmt.execute("DELETE FROM transactions WHERE borrower_id="+member_id);
			int r_affected = dm.remove_member_stmt.executeUpdate();
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
			dm.update_member_stmt.setInt(4,myMember.get_id());
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

	public void return_book(int id){
		try{
			dm.return_book_stmt.setInt(1,id);
			int r_affected = dm.return_book_stmt.executeUpdate();
			System.out.println(r_affected + " book(s) has been returned.");
		}
		catch(Exception se){
			System.out.println("ERROR: Error while removing transaction.");
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
			dm.search_books_stmt.setString(5, "%"+myString+"%");
			ResultSet rs = dm.search_books_stmt.executeQuery();
			while(rs.next()){
				Book temp = new Book(rs.getInt("id"),rs.getString("isbn"),rs.getString("title"),rs.getString("author"),rs.getString("publication"),rs.getInt("copies"));
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
				Member temp = new Member(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getInt("semester"));
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

	public Member get_member(int id){
		Member result = new Member(0,"","",0);
		try{
			dm.get_member_stmt.setInt(1, id);
			ResultSet rs = dm.get_member_stmt.executeQuery();
			if(rs.next()){
				result.update_data(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getInt("semester"));
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
		Book result = new Book(0,"","","","",0);
		try{
			dm.get_book_stmt.setInt(1, id);
			ResultSet rs = dm.get_book_stmt.executeQuery();
			if(rs.next()){
				result.update_data(rs.getInt("id"),rs.getString("isbn"),rs.getString("title"),rs.getString("author"),rs.getString("publication"),rs.getInt("copies"));
			}
		}
		catch(Exception se){
			System.out.println("ERROR: Error while loading book from database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return result;
	}

	public Transaction get_transaction(int id){
		Transaction result = new Transaction(0,0,0,new Timestamp(1900,0,1,0,0,0,0));
		try{
			dm.get_transaction_stmt.setInt(1, id);
			ResultSet rs = dm.get_transaction_stmt.executeQuery();
			if(rs.next()){
				result.update_data(rs.getInt("id"),rs.getInt("borrower_id"),rs.getInt("book_id"),rs.getTimestamp("borrowed_date"));
			}
		}
		catch(Exception se){
			System.out.println("ERROR: Error while loading transaction from database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return result;
	}

	public int get_copies(int id){
		int result = -1;
		try{
			dm.get_copies_stmt.setInt(1, id);
			ResultSet rs = dm.get_copies_stmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("remaining");
			}
		}
		catch(Exception se){
			System.out.println("ERROR: Error while loading remaining copies from database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
		return result;
	}

	public int get_books_borrowed(int id){
		int result = -1;
		try{
			dm.get_books_borrowed_stmt.setInt(1, id);
			ResultSet rs = dm.get_books_borrowed_stmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("books_borrowed");
			}
		}
		catch(Exception se){
			System.out.println("ERROR: Error while loading number of books borrowed from database.");
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
				Book temp = new Book(rs.getInt("id"),rs.getString("isbn"),rs.getString("title"),rs.getString("author"),rs.getString("publication"),rs.getInt("copies"));
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
				Member temp = new Member(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getInt("semester"));
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
				TransactionDisplay temp = new TransactionDisplay(rs.getInt("id"),rs.getString("isbn"),rs.getString("title"),rs.getString("name"),rs.getTimestamp("borrowed_date"));
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

	public int import_books(File myFile) throws InvalidCsvFormatException{
		int count = 0;
		try{
			LabeledCSVParser lcsvp = new LabeledCSVParser(new CSVParser(new FileReader(myFile)));

			if(lcsvp.getLabelIdx("isbn")!=-1 && lcsvp.getLabelIdx("title")!=-1 && lcsvp.getLabelIdx("author")!=-1 && lcsvp.getLabelIdx("publication")!=-1 && lcsvp.getLabelIdx("copies")!=-1){
				while(lcsvp.getLine() != null){
					try{
						Book myBook = new Book(0,lcsvp.getValueByLabel("isbn"),lcsvp.getValueByLabel("title"),lcsvp.getValueByLabel("author"),lcsvp.getValueByLabel("publication"),Integer.parseInt(lcsvp.getValueByLabel("copies")));
						count += add_book(myBook);
					}
					catch(NumberFormatException e){
						System.out.println("ERROR: There was an invalid value of copies in the CSV file.");
						System.out.println("Details:");
						System.out.println(e.toString());
					}
				}
			}
			else{
				Vector<String> cols = new Vector<String>();
				if(lcsvp.getLabelIdx("isbn")==-1)
					cols.addElement("isbn");
				if(lcsvp.getLabelIdx("title")==-1)
					cols.addElement("title");
				if(lcsvp.getLabelIdx("author")==-1)
					cols.addElement("author");
				if(lcsvp.getLabelIdx("publication")==-1)
					cols.addElement("publication");
				if(lcsvp.getLabelIdx("copies")==-1)
					cols.addElement("copies");
				throw new InvalidCsvFormatException(cols);				//when the 1st line of CSV is invalid
			}
		}catch(IOException e){
			System.out.println("ERROR: Error while importing books.");
			System.out.println("Details:");
			e.printStackTrace();
		}
		System.out.println(count + " book(s) has been added to database.");
		return count;
	}

	public void import_members(File myFile) throws InvalidCsvFormatException{
		int count = 0;
		try{
			LabeledCSVParser lcsvp = new LabeledCSVParser(new CSVParser(new FileReader(myFile)));
			if(lcsvp.getLabelIdx("name")!=-1 && lcsvp.getLabelIdx("email")!=-1 && lcsvp.getLabelIdx("semester")!=-1){
				while(lcsvp.getLine() != null){
					try{
						Member myMember = new Member(0,lcsvp.getValueByLabel("name"),lcsvp.getValueByLabel("email"),Integer.parseInt(lcsvp.getValueByLabel("semester")));
						count += add_member(myMember);
					}
					catch(NumberFormatException e){
						System.out.println("ERROR: There was an invalid value of copies in the CSV file.");
						System.out.println("Details:");
						System.out.println(e.toString());
					}
				}	
			}
			else{
				Vector<String> cols = new Vector<String>();
				if(lcsvp.getLabelIdx("name")==-1)
					cols.addElement("name");
				if(lcsvp.getLabelIdx("email")==-1)
					cols.addElement("email");
				if(lcsvp.getLabelIdx("semester")==-1)
					cols.addElement("semester");
				throw new InvalidCsvFormatException(cols);				//when the 1st line of CSV is invalid
			}
		}catch(IOException e){
			System.out.println("ERROR: Error while importing members.");
			System.out.println("Details:");
			e.printStackTrace();
		}
		System.out.println(count + " member(s) added to database.");
	}

	public Object[][] books_to_array(Vector<Book> books){
		Object[][] result = new Object[books.size()][6];

		for(int i=0; i<books.size();i++){
			result[i][0]=books.elementAt(i).get_id();
			result[i][1]=books.elementAt(i).get_isbn();
			result[i][2]=books.elementAt(i).get_title();
			result[i][3]=books.elementAt(i).get_author();
			result[i][4]=books.elementAt(i).get_publication();
			result[i][5]=books.elementAt(i).get_copies();
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
		}
		return result;
	}

	public Object[][] transactions_to_array(Vector<TransactionDisplay> transactions){
		Object[][] result = new Object[transactions.size()][5];

		for(int i=0; i<transactions.size();i++){
			result[i][0]=transactions.elementAt(i).get_id();
			result[i][1]=transactions.elementAt(i).get_isbn();
			result[i][2]=transactions.elementAt(i).get_book_title();
			result[i][3]=transactions.elementAt(i).get_borrower_name();
			result[i][4]=transactions.elementAt(i).get_borrowed_date().toString();
		}
		return result;
	}
	
	public void update_table(JTable table, String[] cols, Object[][] data){
		DefaultTableModel tableModel = new DefaultTableModel(data, cols) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(tableModel);
		table.getColumn("ID").setMaxWidth(30);
	}
}