package slm;
public class Book{
	private int id;
	private String title;
	private String author;
	private String publication;

	public void update_data(int i, String t, String a, String p){
		id = i;
		title = t;
		author = a;
		publication = p;
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

}