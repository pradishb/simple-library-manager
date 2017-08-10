import slm.DBManager;
import slm.Librarian;
import slm.Book;
import slm.Member;

class LibraryInterface{
	public static void main(String[] args) {
		DBManager.init_database();

		Librarian.issue_book(1,1);

		DBManager.close_database();
	}
}