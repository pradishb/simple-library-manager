package slm;
import java.sql.Timestamp;

public class TransactionDisplay{
	private int id;
	private String isbn;
	private String book_title;
	private String borrower_name;
	private Timestamp borrowed_date;

	public TransactionDisplay(int i,String is, String b, String bo, Timestamp bd){
		id = i;
		isbn = is;
		book_title = b;
		borrower_name = bo;
		borrowed_date = bd;
	}

	public void update_data(int i,String is, String b, String bo, Timestamp bd){
		id = i;
		isbn = is;
		book_title = b;
		borrower_name = bo;
		borrowed_date = bd;
	}

	public int get_id(){
		return id;
	}

	public String get_isbn(){
		return isbn;
	}

	public String get_borrower_name(){
		return borrower_name;
	}

	public String get_book_title(){
		return book_title;
	}

	public Timestamp get_borrowed_date(){
		return borrowed_date;
	}
}