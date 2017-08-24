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

public class LibrarySystem{
	public static Vector<Book> get_books(){
		Vector<Book> result = new Vector<Book>();
		
		try{
			ResultSet rs = DBManager.get_books_stmt.executeQuery();
			
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

	public static void import_books(File myFile) throws InvalidCsvFormatException{
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
						Librarian.add_book(myBook);
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

	public static Object[][] books_to_array(Vector<Book> books){
		Object[][] result = new Object[books.size()][4];

		for(int i=0; i<books.size();i++){
			result[i][0]=books.elementAt(i).get_id();
			result[i][1]=books.elementAt(i).get_title();
			result[i][2]=books.elementAt(i).get_author();
			result[i][3]=books.elementAt(i).get_publication();
		}
		return result;
	}

	public static void update_table(JTable table, String[] cols, Object[][]  data){
		table.setModel(new DefaultTableModel(data, cols));
		table.getColumn("ID").setMaxWidth(30);
	}
}