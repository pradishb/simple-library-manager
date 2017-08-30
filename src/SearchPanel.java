package slm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SearchPanel extends JPanel implements ActionListener{
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

			books.setActionCommand("books");
			members.setActionCommand("members");
			transactions.setActionCommand("transactions");

    		books.setSelected(true);
    		group.add(books);
    		group.add(members);
    		group.add(transactions);

			setName("search");

			books.addActionListener(this);
			members.addActionListener(this);
			transactions.addActionListener(this);
			data.addKeyListener(new KeyAdapter(){
				public void keyReleased(KeyEvent e){
					if(group.getSelection().getActionCommand().equals("books")){
						update_books();
					}
					else if(group.getSelection().getActionCommand().equals("members")){
						update_members();
					}
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
		
		public void actionPerformed(ActionEvent e){
			if(e.getActionCommand().equals("books")){
						update_books();
			}
			else if(e.getActionCommand().equals("members")){
				update_members();
			}
		}
		public void update_books(){
			librarian.update_table(table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, librarian.books_to_array(librarian.search_books(data.getText())));

		}
		public void update_members(){
			librarian.update_table(table, new String[]{"ID","NAME","EMAIL","SEMESTER","BOOKS BORROWED"}, librarian.members_to_array(librarian.search_members(data.getText())));
		}
	}
