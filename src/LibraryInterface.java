import slm.DBManager;
import slm.Librarian;
import slm.Book;
import slm.Member;

class LibraryInterface{
	public static void main(String[] args) {
		DBManager.init_database();

		Member myMember = new Member();
		myMember.update_data(0,"Pradish","pradish@abc.com",3,0);
		Librarian.add_member(myMember);

		DBManager.close_database();
	}
}