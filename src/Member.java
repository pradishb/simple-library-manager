package slm;
public class Member{
	private int id;
	private String name;
	private String email;
	private int semester;
	private int no_books_borrowed;

	public void update_data(int i, String n, String e, int s, int b){
		id = i;
		name = n;
		email = e;
		semester = s;
		no_books_borrowed = b;
	}

	public int get_id(){
		return id;
	}

	public String get_name(){
		return name;
	}

	public String get_email(){
		return email;
	}

	public int get_semester(){
		return semester;
	}

	public int get_no_books_borrowed(){
		return no_books_borrowed;
	}
}