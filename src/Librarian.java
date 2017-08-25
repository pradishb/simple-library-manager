package slm;
import java.sql.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;

class InvalidCsvFormatException extends Exception{
	public InvalidCsvFormatException(){
		super("ERROR: The CSV file is not valid.");
	}
}

public class Librarian{
	private DBManager dm;
	public Librarian(DBManager dm){
		this.dm = dm;
	}
	public void add_book(Book myBook){
		try{
			dm.add_book_stmt.setString(1,myBook.get_title());
			dm.add_book_stmt.setString(2,myBook.get_author());
			dm.add_book_stmt.setString(3,myBook.get_publication());
			int r_affected = dm.add_book_stmt.executeUpdate();
			System.out.println(r_affected + " book(s) added to database.");
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while adding books to database.");
			System.out.println("Details:");
			se.printStackTrace();
		}
	}

	public void remove_book(int book_id){
		try{
			dm.remove_book_stmt.setInt(1,book_id);
			int r_affected = dm.remove_book_stmt.executeUpdate();
			System.out.println(r_affected + " book(s) removed from database.");
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while removing books from database.");
			System.out.println("Details:");
			se.printStackTrace();
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
		catch(SQLException se){
			System.out.println("ERROR: Error while updating books in database.");
			System.out.println("Details:");
			se.printStackTrace();
		}
	}

	public void add_member(Member myMember){
		try{
			dm.add_member_stmt.setString(1,myMember.get_name());
			dm.add_member_stmt.setInt(2,myMember.get_semester());
			dm.add_member_stmt.setString(3,myMember.get_email());
			dm.add_member_stmt.setInt(4,myMember.get_no_books_borrowed());
			int r_affected = dm.add_member_stmt.executeUpdate();
			System.out.println(r_affected + " member(s) added to database.");
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while member book to database.");
			System.out.println("Details:");
			se.printStackTrace();
		}
	}

	public void remove_member(int member_id){
		try{
			dm.remove_member_stmt.setInt(1,member_id);
			int r_affected = dm.remove_member_stmt.executeUpdate();
			System.out.println(r_affected + " member(s) removed from database.");
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while removing members from database.");
			System.out.println("Details:");
			se.printStackTrace();
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
		catch(SQLException se){
			System.out.println("ERROR: Error while updating members in database.");
			System.out.println("Details:");
			se.printStackTrace();
		}
	}

	public void issue_book(int borrower_id,int book_id){
		try{
			dm.issue_book_stmt.setInt(1,borrower_id);
			dm.issue_book_stmt.setInt(2,book_id);
			int r_affected = dm.issue_book_stmt.executeUpdate();
			System.out.println(r_affected + " book(s) has been issued.");
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while issuing book.");
			System.out.println("Details:");
			se.printStackTrace();
		}
	}

	/////////////////////////////////
	/////////////////////////////////
	////////////////////////////////

	public Vector<Book> get_books(){
		Vector<Book> result = new Vector<Book>();
		
		try{
			ResultSet rs = dm.get_books_stmt.executeQuery();
			
			while(rs.next()){
				Book temp = new Book(rs.getInt("id"),rs.getString("title"),rs.getString("author"),rs.getString("publication"));
				result.addElement(temp);
			}
			
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while load books from database.");
			System.out.println("Details:");
			se.printStackTrace();
		}
		return result;
	}

	public void import_books(File myFile) throws InvalidCsvFormatException{
		try{
			BufferedReader br=new BufferedReader(new FileReader(myFile));
			String line="";
			line=br.readLine();
			String[] cells = line.split(",");
			if(cells[0].equals("title") && cells[1].equals("author") && cells[2].equals("publication") && cells.length==3){
				while((line=br.readLine())!=null){
					cells = line.split(",");
					if(cells.length==3){
						Book myBook = new Book(0,cells[0],cells[1],cells[2]);
						add_book(myBook);
					}
					else{
						throw new InvalidCsvFormatException();		//when the content of CSV is invalid
					}
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

	public void update_table(JTable table, String[] cols, Object[][]  data){
		table.setModel(new DefaultTableModel(data, cols));
		table.getColumn("ID").setMaxWidth(30);
	}
}