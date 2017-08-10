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
}