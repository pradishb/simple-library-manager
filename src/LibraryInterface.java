import javax.swing.*;
import slm.DBManager;
import slm.Librarian;
import slm.Book;
import slm.Member;

class LibraryInterface{
	public static void main(String[] args) {
		DBManager.init_database();

		JFrame library_window = new JFrame();

		JButton add_book = new JButton("Add Book");
		add_book.setBounds(10,10,100,30);

		library_window.setVisible(true);
		library_window.setLayout(null);
		library_window.setSize(800,500);

		library_window.add(add_book);

		DBManager.close_database();
	}
}