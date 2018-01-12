package slm;

import com.Ostermiller.util.CSVPrinter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

public class CSVExporter{
	private static Librarian librarian;

	public CSVExporter(Librarian librarian){
		this.librarian = librarian;
	}

	public static void export_books(String dir){
		try{
			FileOutputStream os = new FileOutputStream(dir+"\\books.csv");
			CSVPrinter cprinter = new CSVPrinter(os);

			Vector<Book> books = librarian.get_books();

			String[][] output = new String[books.size()][6];

			for(int i=0; i<books.size();i++){
				output[i][0]=Integer.toString(books.elementAt(i).get_id());
				output[i][1]=books.elementAt(i).get_isbn();
				output[i][2]=books.elementAt(i).get_title();
				output[i][3]=books.elementAt(i).get_author();
				output[i][4]=books.elementAt(i).get_publication();
				output[i][5]=Integer.toString(books.elementAt(i).get_copies());
			}
			cprinter.println(new String[]{"id","isbn","title","author","publication","copies"});
			cprinter.println(output);
			os.close();
			System.out.println(books.size()+" books exported to \""+dir+"\\books.csv\".");
		}
		catch(IOException e){
			System.out.println(e.toString());
		}
	}

	public static void export_members(String dir){
		try{
			FileOutputStream os = new FileOutputStream(dir+"\\members.csv");
			CSVPrinter cprinter = new CSVPrinter(os);

			Vector<Member> members = librarian.get_members();

			String[][] output = new String[members.size()][4];

			for(int i=0; i<members.size();i++){
				output[i][0]=Integer.toString(members.elementAt(i).get_id());
				output[i][1]=members.elementAt(i).get_name();
				output[i][2]=members.elementAt(i).get_email();
				output[i][3]=Integer.toString(members.elementAt(i).get_semester());
			}
			cprinter.println(new String[]{"id","name","email","semester"});
			cprinter.println(output);
			os.close();
			System.out.println(members.size()+" members exported to \""+dir+"\\members.csv\".");
		}
		catch(IOException e){
			System.out.println(e.toString());
		}
	}

	
}