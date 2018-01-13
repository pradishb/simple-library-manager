package slm;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.krysalis.barcode4j.impl.upcean.UPCEANLogicImpl;

public class IssueBookPanel extends JPanel implements ActionListener,ListSelectionListener,ItemListener,BarcodeListener{
		private Librarian librarian;
		private JLabel member_id_label;
		private JLabel book_id_label;
		private JLabel m_details;
		private JLabel b_details;
		private JLabel isbn_lb;
		private JLabel title_lb;
		private JLabel author_lb;
		private JLabel pub_lb;
		private JLabel left_lb;
		private JLabel name_lb;
		private JLabel email_lb;
		private JLabel sem_lb;
		private JLabel borrowed_lb;
		private JLabel suggestions;
		private JTextField member;
		private JTextField book;
		private JTextField isbn;
		private JTextField title;
		private JTextField author;
		private JTextField pub;
		private JTextField left;
		private JTextField name;
		private JTextField email;
		private JTextField sem;
		private JTextField borrowed;
		private JCheckBox reader;
		private DefaultListModel<String> listModel;
		private JList<String> list;
		private JScrollPane list_s;
		private Vector<Integer> id_arr;
		private Boolean mem_sel;
		private JButton btn;
		private GroupLayout layout;
		private int no_books_borrowed;
		private BarcodeReader b_reader;

		public IssueBookPanel(Librarian librarian){
			this.librarian = librarian;
			setName("issue_book");

			member_id_label = new JLabel("Enter Member Id");
			book_id_label = new JLabel("Enter Book Id");
			m_details = new JLabel("Member Details:");
			b_details = new JLabel("Book Details:");
			isbn_lb = new JLabel("ISBN:");
			title_lb = new JLabel("Title:");
			author_lb = new JLabel("Author:");
			pub_lb = new JLabel("Publication:");
			left_lb = new JLabel("Copies Left:");
			name_lb = new JLabel("Name:");
			email_lb = new JLabel("Email:");
			sem_lb = new JLabel("Semester:");
			borrowed_lb = new JLabel("Books Borrowed:");
			suggestions = new JLabel("Suggestions:");
			isbn = new JTextField();
			title = new JTextField();
			author = new JTextField();
			pub = new JTextField();
			left = new JTextField();
			member = new JTextField();
			book = new JTextField();
			name = new JTextField();
			email = new JTextField();
			sem = new JTextField();
			borrowed = new JTextField();
			reader = new JCheckBox("Enable barcode reader", true);
			id_arr = new Vector<Integer>();
			mem_sel = new Boolean(true);
			btn = new JButton("Issue Book");
			
			listModel = new DefaultListModel<>();  
            list = new JList<>(listModel);  
          	list_s = new JScrollPane(list);
          	b_reader = new BarcodeReader();

			name.setEditable(false);
			email.setEditable(false);
			sem.setEditable(false);
			borrowed.setEditable(false);
			isbn.setEditable(false);
			title.setEditable(false);
			author.setEditable(false);
			pub.setEditable(false);
			left.setEditable(false);

			btn.addActionListener(this);
			list.addListSelectionListener(this);
			reader.addItemListener(this);
			b_reader.addBarcodeListener(this);

			book.addKeyListener(new KeyAdapter(){
				public void keyReleased(KeyEvent e){
					update_book_list();
					update_book_details();
				}
			});

			member.addKeyListener(new KeyAdapter(){
				public void keyReleased(KeyEvent e){
					update_member_details();
					update_member_list();
				}
			});

			addComponentListener(new ComponentAdapter()
			{
				public void componentShown (ComponentEvent e)
				{
					if(reader.isSelected())
						b_reader.addBarcodeListener(IssueBookPanel.this);
				}

				public void componentHidden (ComponentEvent e)
				{
					if(reader.isSelected())
						b_reader.removeBarcodeListener(IssueBookPanel.this);
				}
			});

			layout = new GroupLayout(this);
			setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(book_id_label)
						.addComponent(member_id_label))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(book)
						.addComponent(member)
						.addComponent(btn)))
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(b_details)
						.addComponent(isbn_lb)
						.addComponent(title_lb)
						.addComponent(author_lb)
						.addComponent(pub_lb)
						.addComponent(left_lb))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(isbn, GroupLayout.PREFERRED_SIZE, 185,GroupLayout.PREFERRED_SIZE)
						.addComponent(title, GroupLayout.PREFERRED_SIZE, 185,GroupLayout.PREFERRED_SIZE)
						.addComponent(author, GroupLayout.PREFERRED_SIZE, 185,GroupLayout.PREFERRED_SIZE)
						.addComponent(pub, GroupLayout.PREFERRED_SIZE, 185,GroupLayout.PREFERRED_SIZE)
						.addComponent(left, GroupLayout.PREFERRED_SIZE, 185,GroupLayout.PREFERRED_SIZE))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(m_details)
						.addComponent(name_lb)
						.addComponent(email_lb)
						.addComponent(sem_lb)
						.addComponent(borrowed_lb))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(name, GroupLayout.PREFERRED_SIZE, 185,GroupLayout.PREFERRED_SIZE)
						.addComponent(email, GroupLayout.PREFERRED_SIZE, 185,GroupLayout.PREFERRED_SIZE)
						.addComponent(sem, GroupLayout.PREFERRED_SIZE, 185,GroupLayout.PREFERRED_SIZE)
						.addComponent(borrowed, GroupLayout.PREFERRED_SIZE, 185,GroupLayout.PREFERRED_SIZE)))
				.addComponent(suggestions)
				.addComponent(list_s)
				.addComponent(reader)
			);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(book_id_label)
					.addComponent(book))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(member_id_label)
					.addComponent(member))
				.addComponent(btn)
				.addGap(10)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(b_details)
					.addComponent(m_details))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(isbn_lb)
					.addComponent(isbn)
					.addComponent(name_lb)
					.addComponent(name)
					)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(title_lb)
					.addComponent(title)
					.addComponent(email_lb)
					.addComponent(email)
					)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(author_lb)
					.addComponent(author)
					.addComponent(sem_lb)
					.addComponent(sem)
					)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(pub_lb)
					.addComponent(pub)
					.addComponent(borrowed_lb)
					.addComponent(borrowed))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(left_lb)
					.addComponent(left))
				.addGap(20)
				.addComponent(suggestions)
				.addComponent(list_s)
				.addComponent(reader)
			);
		}
		public void reset(){
			btn.requestFocus();
		}

		public void actionPerformed(ActionEvent ae){
			try{
				if(title.getText().equals("")){
					JOptionPane.showMessageDialog(this, "Book not found.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				}
				else if(left.getText().equals("0")){
					JOptionPane.showMessageDialog(this, "There is no books remaining.", "Information", JOptionPane.ERROR_MESSAGE);	
				}
				else if(name.getText().equals("")){
					JOptionPane.showMessageDialog(this, "Member not found.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				}
				else if(no_books_borrowed>=Settings.threshold){
					JOptionPane.showMessageDialog(this, "Maximum book borrow limit reached.", "Information", JOptionPane.ERROR_MESSAGE);
				}
				else{
					librarian.issue_book(Integer.parseInt(member.getText()),Integer.parseInt(book.getText()));
					update_book_details();
					update_member_details();
					JOptionPane.showMessageDialog(this, "The book has been issued.", "Sucess", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			catch(NumberFormatException e){
				JOptionPane.showMessageDialog(this, "The form is not valid.", "Bad Input", JOptionPane.ERROR_MESSAGE);
			}
		}

		public void valueChanged(ListSelectionEvent le){
			int index = list.getSelectedIndex();
			if(index!=-1){
				if(mem_sel){
					member.setText(Integer.toString(id_arr.elementAt(index)));
					update_member_details();
				}
				else{
					book.setText(Integer.toString(id_arr.elementAt(index)));
					update_book_details();
				}
			}
		}
		@Override
		public void itemStateChanged(ItemEvent e){
			if(!reader.isSelected()){
				b_reader.removeBarcodeListener(this);
			}
			else{
				b_reader.addBarcodeListener(this);	
			}
		}
		@Override
		public void onBarcodeRead(String barcode){
			try{
				if(barcode.length()==13 || barcode.length()==10){
					ResultSet rs = DBManager.stmt.executeQuery("SELECT * FROM books WHERE isbn=\""+barcode+"\"");
					if(rs.next()){
						isbn.setText(barcode);
						book.setText(rs.getString("id"));
						title.setText(rs.getString("title"));
						author.setText(rs.getString("author"));
						pub.setText(rs.getString("publication"));
						rs = DBManager.stmt.executeQuery("SELECT remaining FROM copies WHERE id="+book.getText());
						if(rs.next())
							left.setText(rs.getString("remaining"));
					}
					else{
						JOptionPane.showMessageDialog(this, "Book not found in the database.", "Bad Input", JOptionPane.ERROR_MESSAGE);
					}
				}
				else if(barcode.length()==8){
					char x = UPCEANLogicImpl.calcChecksum(barcode.substring(0, 7));
					if(barcode.endsWith(String.valueOf(x))){
						ResultSet rs = DBManager.stmt.executeQuery("SELECT * FROM members WHERE id=\""+barcode.substring(0, 7)+"\"");
						if(rs.next()){
							member.setText(rs.getString("id"));
							name.setText(rs.getString("name"));
							email.setText(rs.getString("email"));
							sem.setText(rs.getString("semester"));
							rs = DBManager.stmt.executeQuery("SELECT books_borrowed FROM books_borrowed WHERE id="+member.getText());
							if(rs.next())
								borrowed.setText(rs.getString("books_borrowed"));
						}
						else{
							JOptionPane.showMessageDialog(this, "Member not found in the database.", "Bad Input", JOptionPane.ERROR_MESSAGE);
						}
					}
					else{
						JOptionPane.showMessageDialog(this, "Invalid check digit.", "Bad Input", JOptionPane.ERROR_MESSAGE);
					}
				}
				else{
					JOptionPane.showMessageDialog(this, "Invalid barcode length.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				}
			}
			catch(SQLException e){
				System.out.println(e.toString());	
			}
		}
		public void update_book_details(){
			try{
				int remaining;
				isbn.setText(librarian.get_book(Integer.parseInt(book.getText())).get_isbn());
				title.setText(librarian.get_book(Integer.parseInt(book.getText())).get_title());
				author.setText(librarian.get_book(Integer.parseInt(book.getText())).get_author());
				pub.setText(librarian.get_book(Integer.parseInt(book.getText())).get_publication());

				remaining = librarian.get_copies(Integer.parseInt(book.getText()));
				left.setBackground(Color.GREEN);
				left.setText(Integer.toString(remaining));
				if(remaining==-1){
					left.setBackground(UIManager.getColor(left));
					left.setText("");
				}
				else if(remaining<=0){
					left.setBackground(new Color(255,200,0));
				}
			}
			catch(NumberFormatException ne){
				left.setBackground(UIManager.getColor(left));
				title.setText("");
				author.setText("");
				pub.setText("");
				left.setText("");
			}	
		}

		public void update_member_details(){
			try{
				no_books_borrowed = librarian.get_books_borrowed(Integer.parseInt(member.getText()));
				name.setText(librarian.get_member(Integer.parseInt(member.getText())).get_name());
				email.setText(librarian.get_member(Integer.parseInt(member.getText())).get_email());
				sem.setText(Integer.toString(librarian.get_member(Integer.parseInt(member.getText())).get_semester()));
				borrowed.setText(Integer.toString(no_books_borrowed));
				borrowed.setBackground(Color.GREEN);
				if(sem.getText().equals("0")){
					sem.setText("");
				}
				if(no_books_borrowed==-1){
					borrowed.setBackground(UIManager.getColor(borrowed));
					borrowed.setText("");
				}
				else if(no_books_borrowed>=Settings.threshold){
					borrowed.setBackground(new Color(255,200,0));
				}
			}
			catch(NumberFormatException ne){
				borrowed.setBackground(UIManager.getColor(borrowed));
				name.setText("");
				email.setText("");
				sem.setText("");
				borrowed.setText("");
			}	
		}

		public void update_member_list(){
			listModel.clear();
			id_arr.clear();
			mem_sel = true;
			if(member.getText().length()>=2){
				Vector<Member> myMembers = new Vector<Member>();
				myMembers.addAll(0,librarian.search_members(member.getText()));
				for(int i=0; i<myMembers.size();i++){
					String sup = new String();
					switch(myMembers.elementAt(i).get_semester()){
						case 1:
							sup = "st";
							break;
						case 2:
							sup = "nd";
							break;
						case 3:
							sup = "rd";
							break;
						default:
							sup = "th";
					}
					id_arr.addElement(myMembers.elementAt(i).get_id());
					listModel.addElement(myMembers.elementAt(i).get_name()+", "+myMembers.elementAt(i).get_semester()+sup+" semester.");
				}
			}
		}

		public void update_book_list(){
			listModel.clear();
			id_arr.clear();
			mem_sel = false;
			if(book.getText().length()>=2){
				Vector<Book> myBooks = new Vector<Book>();
				myBooks.addAll(0,librarian.search_books(book.getText()));
				for(int i=0; i<myBooks.size();i++){
					id_arr.addElement(myBooks.elementAt(i).get_id());
					listModel.addElement(myBooks.elementAt(i).get_title()+" by "+myBooks.elementAt(i).get_author()+".");
				}
			}
		}
	}