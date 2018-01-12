package slm;

public class Book{
	private int id;
	private String isbn;
	private String title;
	private String author;
	private String publication;
	private int copies;

	public Book(int i, String is, String t, String a, String p, int c){
		id = i;
		isbn = is;
		title = t;
		author = a;
		publication = p;
		copies = c;

	}

	public void update_data(int i, String is, String t, String a, String p, int c){
		id = i;
		isbn = is;
		title = t;
		author = a;
		publication = p;
		copies = c;
	}

	public int get_id(){
		return id;
	}

	public String get_isbn(){
		return isbn;
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

	public int get_copies(){
		return copies;
	}

}