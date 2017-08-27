package slm;

import javax.swing.filechooser.FileNameExtensionFilter;  
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LibraryInterface extends JFrame{
	private Librarian librarian;
	private final int WIDTH = 500;
	private final int HEIGHT = 500;
	private JFileChooser chooser;
	private ManageMembersPanel mm_panel;
	private JTabbedPane jtp;
	private GroupLayout layout;
	private RemoveBookDialog rbd;
	private AddBookDialog abd;
	private IssueBookPanel ib_panel;
	private ManageBooksPanel mb_panel;

	public LibraryInterface(Librarian librarian){
		super("Simple Library Manager");
		this.librarian = librarian;
		init_interface();
		load_interface();
	}

	public void init_interface(){
		System.out.println("Initializing Interface...");
		//Initialization
		chooser = new JFileChooser();
		mm_panel = new ManageMembersPanel();
		jtp = new JTabbedPane();
		rbd = new RemoveBookDialog();
		abd = new AddBookDialog();
		ib_panel = new IssueBookPanel();
		mb_panel = new ManageBooksPanel();
	}

	public void load_interface(){
		System.out.println("Loading Interface...");

		//Panel
		ib_panel.setName("issue_book");
		mb_panel.setName("manage_books");
		mm_panel.setName("manage_memberships");
		
		//Tabbed Pane
		jtp.addTab("Issue Book", ib_panel);
		jtp.addTab("Managae Books", mb_panel);
		jtp.addTab("Managae Memberships", mm_panel);
		jtp.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e){
				if(jtp.getSelectedComponent().getName()=="manage_books"){
					librarian.update_table(mb_panel.table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, librarian.books_to_array(librarian.get_books()));
				}
				if(jtp.getSelectedComponent().getName()=="manage_memberships"){
					librarian.update_table(mm_panel.table, new String[]{"ID","NAME","EMAIL","SEMESTER","BOOKS BORROWED"}, librarian.members_to_array(librarian.get_members()));
				}
			}
		});

		//Frame
		layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		setVisible(true);
		setSize(WIDTH,HEIGHT);
		setResizable(false);	
		add(jtp);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				SLM.db_manager.close_database();
				setVisible(false);
				dispose();
			}
		});

		//Layout Configuration
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addComponent(jtp)
			);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(jtp)
			);
	}

	class IssueBookPanel extends JPanel implements ActionListener{
		private JLabel member_id_label;
		private JLabel book_id_label;
		private JTextField member_id_tf;
		private JTextField book_id_tf;
		private JButton btn;
		private GroupLayout layout;

		IssueBookPanel(){
			member_id_label = new JLabel("Enter Member Id");
			book_id_label = new JLabel("Enter Book Id");
			member_id_tf = new JTextField();
			book_id_tf = new JTextField();
			btn = new JButton("Issue Book");
			add(member_id_label);
			add(member_id_tf);
			add(book_id_label);
			add(book_id_tf);
			add(btn);

			layout = new GroupLayout(this);
			setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			layout.setHorizontalGroup(
			layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(book_id_label)
				.addComponent(member_id_label))
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(book_id_tf)
				.addComponent(member_id_tf)
				.addComponent(btn))
			);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(book_id_label)
					.addComponent(book_id_tf))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(member_id_label)
					.addComponent(member_id_tf))
				.addComponent(btn)
			);
		}
		public void actionPerformed(ActionEvent ae){

		}
	}

	class ManageBooksPanel extends JPanel implements ActionListener{
		private JButton add_book_btn;
		private JButton remove_book_btn;
		private JButton import_books_btn;
		private JTable table;
		private JScrollPane table_sp;
		private GroupLayout layout;

		ManageBooksPanel(){
			add_book_btn = new JButton("Add Book");
			remove_book_btn = new JButton("Remove Book");
			import_books_btn = new JButton("Import Books");

			table = new JTable();		
			table.setEnabled(false);
			table_sp = new JScrollPane(table);

			add(table_sp);
			add(add_book_btn);
			add(import_books_btn);
			add(remove_book_btn);

			add_book_btn.addActionListener(this);
			remove_book_btn.addActionListener(this);
			import_books_btn.addActionListener(this);

			layout = new GroupLayout(this);
			setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(table_sp)
				.addGroup(layout.createSequentialGroup()
					.addComponent(add_book_btn)
					.addComponent(import_books_btn)
					.addComponent(remove_book_btn))
			);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(table_sp)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(add_book_btn)
					.addComponent(import_books_btn)
					.addComponent(remove_book_btn))
			);
		}
		public void actionPerformed(ActionEvent ae){
			if(ae.getSource()==add_book_btn){
				 abd.setVisible(true);
			}
			else if(ae.getSource()==remove_book_btn){
				rbd.setVisible(true);
			}
			else{
				chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
				int returnVal = chooser.showOpenDialog(LibraryInterface.this);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					try{
						librarian.import_books(chooser.getSelectedFile());
					}catch(InvalidCsvFormatException e){
						System.out.println(e.getMessage());
						JOptionPane.showMessageDialog(LibraryInterface.this, "Some errors occured while import the CSV file.", "Bad Input File", JOptionPane.ERROR_MESSAGE);
					}
					librarian.update_table(table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, librarian.books_to_array(librarian.get_books()));
				}
			}
		}
	}

	class ManageMembersPanel extends JPanel implements ActionListener{
		private JButton add_member_btn;
		private JButton remove_member_btn;
		private JButton import_members_btn;
		private JTable table;
		private JScrollPane table_sp;
		private GroupLayout layout;

		ManageMembersPanel(){
			add_member_btn = new JButton("Add Member");
			remove_member_btn = new JButton("Remove Member");
			import_members_btn = new JButton("Import Members");

			table = new JTable();		
			table.setEnabled(false);
			table_sp = new JScrollPane(table);

			add(table_sp);
			add(add_member_btn);
			add(import_members_btn);
			add(remove_member_btn);

			add_member_btn.addActionListener(this);
			remove_member_btn.addActionListener(this);
			import_members_btn.addActionListener(this);

			layout = new GroupLayout(this);
			setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(table_sp)
				.addGroup(layout.createSequentialGroup()
					.addComponent(add_member_btn)
					.addComponent(import_members_btn)
					.addComponent(remove_member_btn))
			);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(table_sp)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(add_member_btn)
					.addComponent(import_members_btn)
					.addComponent(remove_member_btn))
			);
		}
		public void actionPerformed(ActionEvent ae){
			if(ae.getSource()==add_member_btn){
			}
			else if(ae.getSource()==remove_member_btn){
			}
			else{
				chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
				int returnVal = chooser.showOpenDialog(LibraryInterface.this);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					try{
						librarian.import_members(chooser.getSelectedFile());
					}catch(InvalidCsvFormatException e){
						System.out.println(e.getMessage());
						JOptionPane.showMessageDialog(LibraryInterface.this, "Some errors occured while import the CSV file.", "Bad Input File", JOptionPane.ERROR_MESSAGE);
					}
					librarian.update_table(table, new String[]{"ID","NAME","EMAIL","SEMESTER","BOOKS BORROWED"}, librarian.members_to_array(librarian.get_members()));
				}
			}
		}
	}

	class AddBookDialog extends JDialog implements ActionListener{
		private JLabel title_label;
		private JLabel author_label;
		private JLabel publication_label;
		private JTextField title_tf;
		private JTextField author_tf;
		private JTextField publication_tf;
		private JButton btn;
		private GroupLayout layout;

		AddBookDialog(){
			super(LibraryInterface.this,"Add Book Form",true);

			title_label = new JLabel("Title:");
			author_label = new JLabel("Author:");
			publication_label = new JLabel("Publication:");
			title_tf = new JTextField();
			author_tf = new JTextField();
			publication_tf = new JTextField();
			btn = new JButton("Add Book");

			layout = new GroupLayout(getContentPane());
			getContentPane().setLayout(layout);

			btn.addActionListener(this);

			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);

			layout.setHorizontalGroup(
			layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(title_label)
				.addComponent(author_label)
				.addComponent(publication_label))
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(title_tf)
				.addComponent(author_tf)
				.addComponent(publication_tf)
				.addComponent(btn))
				);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(title_label)
					.addComponent(title_tf))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(author_label)
					.addComponent(author_tf))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(publication_label)
					.addComponent(publication_tf))
				.addComponent(btn)
				);
			
			setSize(400,165);
			setResizable(false);
			add(title_label);
			add(title_tf);
			add(author_label);
			add(author_tf);
			add(publication_label);
			add(publication_tf);
			add(btn);
		}
		public void actionPerformed(ActionEvent ae){
			if(title_tf.getText().equals("") || author_tf.getText().equals("") || publication_tf.getText().equals("")){
				System.out.println("ERROR: Some fields are empty in add book form.");
				JOptionPane.showMessageDialog(this, "Form is not complete.", "Bad Input", JOptionPane.ERROR_MESSAGE);
			}
			else{
				Book myBook = new Book(0,title_tf.getText(),author_tf.getText(),publication_tf.getText());
				librarian.add_book(myBook);
				librarian.update_table(mb_panel.table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, librarian.books_to_array(librarian.get_books()));
				setVisible(false);
			}
		}
	}

	class RemoveBookDialog extends JDialog implements ActionListener{
		private JLabel book_id_label;
		private JTextField book_id_tf;
		private JButton btn;
		private GroupLayout layout;

		RemoveBookDialog(){
			super(LibraryInterface.this,"Remove Book Form",true);

			book_id_tf = new JTextField();
			btn = new JButton("Remove Book");
			book_id_label = new JLabel("Book Id:");
			layout = new GroupLayout(getContentPane());
			getContentPane().setLayout(layout);

			btn.addActionListener(this);

			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(book_id_label))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(book_id_tf)
					.addComponent(btn))
				);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(book_id_label)
					.addComponent(book_id_tf))
				.addComponent(btn)
				);

			
			setSize(400,110);
			setResizable(false);
			add(book_id_label);
			add(book_id_tf);
			add(btn);
		}
		public void actionPerformed(ActionEvent ae){
			try{
				librarian.remove_book(Integer.parseInt(book_id_tf.getText()));
				librarian.update_table(mb_panel.table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, librarian.books_to_array(librarian.get_books()));
				setVisible(false);
			}
			catch(NumberFormatException e){
				System.out.println("ERROR: Book id provided is not valid.");
				JOptionPane.showMessageDialog(this, "Please enter a valid book id.", "Bad Input", JOptionPane.ERROR_MESSAGE);
			}		
		}
	}
}

