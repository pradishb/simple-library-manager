import slm.DBManager;
import slm.Librarian;
import slm.Book;

class LibraryInterface{
	public static void main(String[] args) {
		DBManager.init_database();

		Book tBook = new Book();
		tBook.update_data(0,"COA","COA teacher","COA PUblication",0);
		Librarian.add_book(tBook);
	}
}