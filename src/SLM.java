import slm.LibraryInterface;
import slm.DBManager;
import slm.Librarian;
import slm.Book;
import slm.Member;

class SLM{
	public static void main(String[] args) {
			DBManager.init_database();
			LibraryInterface.init_interface();
		}
}