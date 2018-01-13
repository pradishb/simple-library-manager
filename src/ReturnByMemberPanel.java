package slm;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.FlowLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.sql.Timestamp;

public class ReturnByMemberPanel extends TablePanel{
	private Object[][] data;
	public ReturnByMemberPanel(int id){
		super(new String[]{"ID","BOOK","BORROWED TIME","FINE"});
		try{
			ResultSet rs = DBManager.stmt.executeQuery("SELECT COUNT(id) as total FROM transactions_display WHERE member_id="+id);
			if(rs.next()){
				data = new Object[rs.getInt("total")][4];
				rs = DBManager.stmt.executeQuery("SELECT * FROM transactions_display WHERE member_id="+id);
				int i = 0;
				while(rs.next()){
					int fine =  Transaction.calculate_fine(rs.getTimestamp("borrowed_date"));
					data[i][0] = rs.getInt("id");
					data[i][1] = rs.getString("book_title");
					data[i][2] = rs.getTimestamp("borrowed_date");
					data[i][3] = fine<0?0:fine;
					i++;
				}
			}
		}
		catch(SQLException e){
			System.out.println(e.toString());
		}
		update(data);
		add(table_sp);
	}

	public int calculate_fine(){
		int sum = 0;
		for(int i=0;i<data.length;i++){
			sum+=(int)data[i][3];
		}
		return sum;
	}

	public void return_all_books(){
		for(int i=0;i<table.getRowCount();i++){
			try{
				int count = DBManager.stmt.executeUpdate("DELETE FROM transactions WHERE id="+table.getValueAt(i,0));
				System.out.println(count+" book(s) has been returned.");
				JOptionPane.showMessageDialog(this,count+" book(s) has been successfully returned.");
			}
			catch(SQLException e){
				System.out.println(e.toString());
			}
		}
	}
}