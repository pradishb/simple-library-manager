package slm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IssueBookPanel extends JPanel implements ActionListener{
		private Librarian librarian;
		private JLabel member_id_label;
		private JLabel book_id_label;
		private JLabel m_details;
		private JLabel b_details;
		private JLabel title_lb;
		private JLabel author_lb;
		private JLabel pub_lb;
		private JLabel left_lb;
		private JLabel name_lb;
		private JLabel email_lb;
		private JLabel sem_lb;
		private JTextField member;
		private JTextField book;
		private JTextField title;
		private JTextField author;
		private JTextField pub;
		private JTextField left;
		private JTextField name;
		private JTextField email;
		private JTextField sem;
		private JButton btn;
		private GroupLayout layout;

		public IssueBookPanel(Librarian librarian){
			this.librarian = librarian;
			setName("issue_book");

			member_id_label = new JLabel("Enter Member Id");
			book_id_label = new JLabel("Enter Book Id");
			m_details = new JLabel("Member Details:");
			b_details = new JLabel("Book Details:");
			title_lb = new JLabel("Title:");
			author_lb = new JLabel("Author:");
			pub_lb = new JLabel("Publication:");
			left_lb = new JLabel("Copies Left:");
			name_lb = new JLabel("Name:");
			email_lb = new JLabel("Email:");
			sem_lb = new JLabel("Semester:");
			title = new JTextField();
			author = new JTextField();
			pub = new JTextField();
			left = new JTextField();
			member = new JTextField();
			book = new JTextField();
			name = new JTextField();
			email = new JTextField();
			sem = new JTextField();
			btn = new JButton("Issue Book");
			
			name.setEditable(false);
			email.setEditable(false);
			sem.setEditable(false);
			title.setEditable(false);
			author.setEditable(false);
			pub.setEditable(false);
			left.setEditable(false);

			btn.addActionListener(this);

			book.addKeyListener(new KeyAdapter(){
				public void keyReleased(KeyEvent e){
					update_book_details();
				}
			});

			member.addKeyListener(new KeyAdapter(){
				public void keyReleased(KeyEvent e){
					update_memeber_details();
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
						.addComponent(title_lb)
						.addComponent(author_lb)
						.addComponent(pub_lb)
						.addComponent(left_lb))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(title)
						.addComponent(author)
						.addComponent(pub)
						.addComponent(left))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(m_details)
						.addComponent(name_lb)
						.addComponent(email_lb)
						.addComponent(sem_lb))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(name)
						.addComponent(email)
						.addComponent(sem))
					)
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
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(b_details)
					.addComponent(m_details))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(name_lb)
					.addComponent(name)
					.addComponent(title_lb)
					.addComponent(title)
					)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(email_lb)
					.addComponent(email)
					.addComponent(author_lb)
					.addComponent(author)
					)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(sem_lb)
					.addComponent(sem)
					.addComponent(pub_lb)
					.addComponent(pub)
					)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(left_lb)
					.addComponent(left))
			);
		}
		public void actionPerformed(ActionEvent ae){
			try{
				if(title.getText().equals("")){
					JOptionPane.showMessageDialog(this, "Book not found.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				}
				else if(left.getText().equals("0")){
					JOptionPane.showMessageDialog(this, "There is no books remaining.", "Bad Input", JOptionPane.ERROR_MESSAGE);	
				}
				else if(name.getText().equals("")){
					JOptionPane.showMessageDialog(this, "Member not found.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				}
				else{
					librarian.issue_book(Integer.parseInt(member.getText()),Integer.parseInt(book.getText()));
					update_book_details();
					update_memeber_details();
					JOptionPane.showMessageDialog(this, "The book has been issued.", "Sucess", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			catch(NumberFormatException e){
				JOptionPane.showMessageDialog(this, "The form is not valid.", "Bad Input", JOptionPane.ERROR_MESSAGE);
			}
		}
		public void update_book_details(){
			try{
				int remaining;
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

		public void update_memeber_details(){
			try{
				name.setText(librarian.get_member(Integer.parseInt(member.getText())).get_name());
				email.setText(librarian.get_member(Integer.parseInt(member.getText())).get_email());
				sem.setText(Integer.toString(librarian.get_member(Integer.parseInt(member.getText())).get_semester()));
				if(sem.getText().equals("0")){
					sem.setText("");
				}
			}
			catch(NumberFormatException ne){
				name.setText("");
				email.setText("");
				sem.setText("");
			}	
		}
	}