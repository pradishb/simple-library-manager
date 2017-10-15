package slm;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class TransactionsPanel extends JPanel implements ListSelectionListener{
		private Librarian librarian;
		private GroupLayout layout;
		private JScrollPane scroll;
		private JButton btn;
		public JTable table;

		TransactionsPanel(Librarian librarian){
			this.librarian = librarian;
			table = new JTable();
			scroll = new JScrollPane(table);
			btn = new JButton("Return Book");

			btn.setEnabled(false);
			layout = new GroupLayout(this);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			table.getSelectionModel().addListSelectionListener(this);
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
			librarian.update_table(table, new String[]{"ID","BOOK","BORROWED BY","BORROWED TIME"}, librarian.transactions_to_array(librarian.get_transactions()));
		}

		public void valueChanged(ListSelectionEvent le){
			btn.setEnabled(true);
	        int[] selectedRow = table.getSelectedRows();
			// System.out.println(selectedRow[0]);
		}
	}