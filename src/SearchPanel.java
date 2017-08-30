package slm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SearchPanel extends JPanel{
		private Librarian librarian;
		private GroupLayout layout;
		private JScrollPane table_sp;
		private JTextField data;
		private JTable table;
		private JRadioButton books;
		private JRadioButton members;
		private JRadioButton transactions;
		private ButtonGroup group;

		SearchPanel(Librarian librarian){
			this.librarian = librarian;
			table = new JTable();
			table.setEnabled(false);
			table_sp = new JScrollPane(table);
			books = new JRadioButton("books");
			members = new JRadioButton("members");
			transactions = new JRadioButton("transactions");
			data = new JTextField();
			group = new ButtonGroup();

    		books.setSelected(true);
    		group.add(books);
    		group.add(members);
    		group.add(transactions);

			setName("search");

			data.addKeyListener(new KeyAdapter(){
				public void keyReleased(KeyEvent e){
					librarian.update_table(table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, librarian.books_to_array(librarian.search_books(data.getText())));
				}
			});

			layout = new GroupLayout(this);
			setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);

			layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(books)
					.addComponent(members)
					.addComponent(transactions))
				.addComponent(data)
				.addComponent(table_sp)
			);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(data))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(books)
					.addComponent(members)
					.addComponent(transactions))
				.addComponent(table_sp)
			);
		}
		// public void keyTyped(KeyEvent e){
		// // public void actionPerformed(ActionEvent ae){
		// 	librarian.update_table(table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, librarian.books_to_array(librarian.search_books(data.getText())));
		// }
	}
