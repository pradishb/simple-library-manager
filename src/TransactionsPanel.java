package slm;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class TransactionsPanel extends JPanel implements ListSelectionListener,ActionListener{
		private Librarian librarian;
		private GroupLayout layout;
		private JScrollPane scroll;
		private JButton btn;
		private Object[][] data;
		public JTable table;

		TransactionsPanel(Librarian librarian){
			this.librarian = librarian;
			table = new JTable();
			scroll = new JScrollPane(table);
			btn = new JButton("Return Book");

			btn.setEnabled(false);
			btn.addActionListener(this);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.getSelectionModel().addListSelectionListener(this);
			layout = new GroupLayout(this);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			setName("transactions");
			setLayout(layout);
			
			layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(scroll)
				.addComponent(btn)
			);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(scroll)
				.addComponent(btn)
			);
		}

		public void update_table(){
			data = librarian.transactions_to_array(librarian.get_transactions());
			librarian.update_table(table, new String[]{"ID","BOOK","BORROWED BY","BORROWED TIME"}, data);
		}

		public void actionPerformed(ActionEvent ae){
			int[] selectedRow = table.getSelectedRows();
			Transaction result = librarian.get_transaction((int)data[selectedRow[0]][0]);

			int yearDiff = LocalDateTime.now().getYear()-result.get_borrowed_date().toLocalDateTime().getYear();
			int monthDiff = LocalDateTime.now().getMonthValue()-result.get_borrowed_date().toLocalDateTime().getMonthValue();
			int dayDiff = LocalDateTime.now().getDayOfMonth()-result.get_borrowed_date().toLocalDateTime().getDayOfMonth();

			int fine = (yearDiff*365+monthDiff*30+dayDiff-Settings.due);

			String msg;

			if(fine<=0){
				msg = "Do you want to return the book?";
			}
			else{
				msg = "The fine amout is "+fine+" x "+Settings.price+" = Rs. "+fine*Settings.price+".\nDo you want to return the book?";
			}

			if(JOptionPane.showConfirmDialog(null,msg, "Confirm Dialog", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
				librarian.return_book(result.get_id());
				update_table();
			}

		}

		public void valueChanged(ListSelectionEvent le){
			btn.setEnabled(true);
		}
	}