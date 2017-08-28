package slm;
import java.sql.Timestamp;

public class Transaction{
	private int id;
	private int borrower_id;
	private int book_id;
	private Timestamp borrowed_date;

	public Transaction(int i, int br, int bo, Timestamp bd){
		id = i;
		borrower_id = br;
		book_id = bo;
		borrowed_date = bd;
	}

	public void update_data(int i, int br, int bo, Timestamp bd){
		id = i;
		borrower_id = br;
		book_id = bo;
		borrowed_date = bd;
	}

	public int get_id(){
		return id;
	}

	public int get_borrower_id(){
		return borrower_id;
	}

	public int get_book_id(){
		return book_id;
	}

	public Timestamp get_borrowed_date(){
		return borrowed_date;
	}
}