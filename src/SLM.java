package slm;

class SLM{
	public static void main(String[] args) {
			DBManager dm = new DBManager();
			Librarian l = new Librarian(dm);
			LibraryInterface li = new LibraryInterface(l);
		}
}