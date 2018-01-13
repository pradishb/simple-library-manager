package slm;
import java.awt.*;
import org.krysalis.barcode4j.impl.upcean.UPCEANLogicImpl;

class SLM{
	private static DBManager db_manager;
	private static Librarian librarian;
	private static LibraryInterface library_interface;
	private static SecurityManager security_manager;
	private static CSVExporter csv;
	public static void main(String[] args){

		db_manager = new DBManager();
		security_manager = new SecurityManager();
		librarian = new Librarian(db_manager);
		csv = new CSVExporter(librarian);

		EventQueue.invokeLater(new Runnable(){
		public void run() {
				library_interface = new LibraryInterface(librarian,db_manager,security_manager);
			}
		});
	
	}
}