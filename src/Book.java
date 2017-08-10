package slm;
public class Book{
	private int id;
	private String title;
	private String author;
	private String publication;
	private int borrower_id;

	public void update_data(int i, String t, String a, String p, int b){
		id = i;
		title = t;
		author = a;
		publication = p;
		borrower_id = b;
	}

	public int get_id(){
		return id;
	}

	public String get_title(){
		return title;
	}

	public String get_author(){
		return author;
	}

	public String get_publication(){
		return publication;
	}

	public int borrower_id(){
		return borrower_id;
	}
}