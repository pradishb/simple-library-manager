package slm;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

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
			System.out.println("Error while load books from database.");
			System.out.println("Details:");
			se.printStackTrace();
		}
		return result;
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