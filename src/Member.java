package slm;
public class Member{
	private int id;
	private String name;
	private String email;
	private int semester;

	public Member(int i, String n, String e, int s){
		id = i;
		name = n;
		email = e;
		semester = s;
	}

	public void update_data(int i, String n, String e, int s){
		id = i;
		name = n;
		email = e;
		semester = s;
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
}