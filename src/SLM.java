package slm;
import java.awt.*;

class SLM{
	private static DBManager db_manager;
	private static Librarian librarian;
	private static LibraryInterface library_interface;
	private static SecurityManager security_manager;
	public static void main(String[] args) {
		// BarCodeReader.ISBN_to_book("9780073523323");
		// // System.out.println(BarCodeReader.read_from_url("d"));
		db_manager = new DBManager();
		security_manager = new SecurityManager();
		librarian = new Librarian(db_manager, library_interface);

		EventQueue.invokeLater(new Runnable(){
		public void run() {
				library_interface = new LibraryInterface(librarian,db_manager,security_manager);
			}
		});
	
	}
}