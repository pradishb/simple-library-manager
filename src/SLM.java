package slm;

class SLM{
	static DBManager db_manager;
	static Librarian librarian;
	static LibraryInterface library_interface;
	public static void main(String[] args) {
			db_manager = new DBManager();
			librarian = new Librarian(db_manager);
			library_interface = new LibraryInterface(librarian);
	}
}