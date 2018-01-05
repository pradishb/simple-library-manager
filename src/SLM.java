package slm;

class SLM{
	private static DBManager db_manager;
	private static Librarian librarian;
	private static LibraryInterface library_interface;
	private static SecurityManager security_manager;
	public static void main(String[] args) {
			db_manager = new DBManager();
			security_manager = new SecurityManager();
			librarian = new Librarian(db_manager, library_interface);
			library_interface = new LibraryInterface(librarian,db_manager,security_manager);

	}
}