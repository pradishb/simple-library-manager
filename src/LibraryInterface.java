package slm;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LibraryInterface extends JFrame{
	private Librarian librarian;
	private DBManager db_manager;
	private SecurityManager security_manager;
	private final int WIDTH = 650;
	private final int HEIGHT = 500;
	private JFileChooser chooser;
	private ManageMembersPanel mm_panel;
	private JTabbedPane jtp;
	private GroupLayout layout;
	private RemoveBookDialog rbd;
	private IssueBookPanel ib_panel;
	private ManageBooksPanel mb_panel;
	private TransactionsPanel tr_panel;
	private SearchPanel s_panel;
	private SettingsPanel set_panel;

	public LibraryInterface(Librarian librarian, DBManager db_manager,SecurityManager security_manager){
		super("Simple Library Manager");
		this.librarian = librarian;
		this.db_manager = db_manager;
		this.security_manager = security_manager;
		check_password();
		Settings.update(db_manager);
		init_interface();
		load_interfaces();
	}

	public void check_password(){
		//fetch settings
		try{
			//check if the password exists
			ResultSet rs = db_manager.stmt.executeQuery("SELECT count(id) AS settings_exists FROM settings");
			rs.next();
			int x = rs.getInt("settings_exists");

			if(x==0){
				JLabel lb = new JLabel("Enter a new password:");
        		JTextField pass = new JPasswordField();
        		Object[] ob = {lb,pass};
        		int result = JOptionPane.showConfirmDialog(this, ob, "Password not set", JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
		        if (result == JOptionPane.OK_OPTION) {
		            db_manager.stmt.execute("INSERT INTO settings VALUES (\"1\",\""+security_manager.sha1(pass.getText())+"\",5,30,1)");
		        }
		        else{
		        	db_manager.close_database();
					dispose();
					System.exit(0);
		        }
				// String s = (String)JOptionPane.showInputDialog(this,"Set new password:","Password not set", JOptionPane.PLAIN_MESSAGE);
				// System.out.println(security_manager.sha1(s));
				
			}
			else{
				JLabel lb = new JLabel("Password:");
        		JTextField input = new JPasswordField();
        		Object[] ob = {lb,input};
        		String pass = new String();
				do{
					input.setText("");
					int result = JOptionPane.showConfirmDialog(this, ob, "Login", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
		        	if (result == JOptionPane.OK_OPTION) {
		        		ResultSet rs1 = db_manager.stmt.executeQuery("SELECT password FROM settings WHERE id=1");
						rs1.next();
						pass = rs1.getString("password");
					}
					else{
						db_manager.close_database();
						dispose();
						System.exit(0);
					}
				}
				while(!security_manager.sha1(input.getText()).equals(pass));
			}
		}
		catch(SQLException se){
			System.out.println("ERROR: Error while fetching settings from database.");
			System.out.println("Details:");
			System.out.println(se.toString());
		}
	}

	public void init_interface(){
		System.out.println("Initializing Interface...");
		//Initialization
		chooser = new JFileChooser();
		rbd = new RemoveBookDialog();
		mm_panel = new ManageMembersPanel();
		ib_panel = new IssueBookPanel(librarian);
		mb_panel = new ManageBooksPanel();
		tr_panel = new TransactionsPanel(librarian);
		s_panel = new SearchPanel(librarian);
		set_panel = new SettingsPanel(db_manager,security_manager);
		jtp = new JTabbedPane();
	}

	public void load_interfaces(){
		jtp.addTab("Issue Book", ib_panel);
		jtp.addTab("Manage Books", mb_panel);
		jtp.addTab("Manage Memberships", mm_panel);
		jtp.addTab("Transactions", tr_panel);
		jtp.addTab("Search", s_panel);
		jtp.addTab("Settings", set_panel);
		
		jtp.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e){
				if(jtp.getSelectedComponent().getName()=="manage_books"){
					librarian.update_table(mb_panel.table, new String[]{"ID","TITLE","AUTHER","PUBLICATION","COPIES"}, librarian.books_to_array(librarian.get_books()));
				}else if(jtp.getSelectedComponent().getName()=="manage_memberships"){
					librarian.update_table(mm_panel.table, new String[]{"ID","NAME","EMAIL","SEMESTER"}, librarian.members_to_array(librarian.get_members()));
				}else if(jtp.getSelectedComponent().getName()=="transactions"){
					tr_panel.update_table();
				}else if(jtp.getSelectedComponent().getName()=="search"){
					s_panel.update_books();
				}else if(jtp.getSelectedComponent().getName()=="settings"){
					set_panel.update_panel();
				}else if(jtp.getSelectedComponent().getName()=="issue_book"){
					ib_panel.update_book_details();
					ib_panel.update_member_details();
					ib_panel.update_book_list();
				}
			}
		});

		//Frame
		layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		setVisible(true);
		setSize(WIDTH,HEIGHT);
		setLocationRelativeTo(null);
		// setResizable(false);
		add(jtp);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				db_manager.close_database();
				setVisible(false);
				dispose();
			}
		});
		//Layout Configuration
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(jtp)
			);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(jtp)
			);
	}

	class ManageBooksPanel extends JPanel implements ActionListener{
		private JButton add_book_btn;
		private JButton remove_book_by_id_btn;
		private JButton remove_book_btn;
		private JButton import_books_btn;
		private JTable table;
		private JScrollPane table_sp;
		private GroupLayout layout;
		private Object[][] data;
		ManageBooksPanel(){
			setName("manage_books");

			add_book_btn = new JButton("Add Book");
			remove_book_by_id_btn = new JButton("Remove Book By Id");
			remove_book_btn = new JButton("Remove Book");
			import_books_btn = new JButton("Import Books");

			table = new JTable();
			table_sp = new JScrollPane(table);

			add(table_sp);
			add(add_book_btn);
			add(import_books_btn);
			add(remove_book_by_id_btn);

			add_book_btn.addActionListener(this);
			remove_book_by_id_btn.addActionListener(this);
			import_books_btn.addActionListener(this);
			remove_book_btn.addActionListener(this);

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
					.addComponent(remove_book_by_id_btn)
					.addComponent(remove_book_btn))
			);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(table_sp)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(add_book_btn)
					.addComponent(import_books_btn)
					.addComponent(remove_book_by_id_btn)
					.addComponent(remove_book_btn))
			);
		}
		public void actionPerformed(ActionEvent ae){
			if(ae.getSource()==add_book_btn){
		 		JLabel title_label = new JLabel("Title:");
				JLabel author_label = new JLabel("Author:");
				JLabel publication_label = new JLabel("Publication:");
				JLabel copies_label = new JLabel("No. of copies:");
				JTextField title_tf = new JTextField();
				JTextField author_tf = new JTextField();	
				JTextField publication_tf = new JTextField();
				JTextField copies_tf = new JTextField();

				Object[] ob = {title_label,title_tf,author_label,author_tf,publication_label,publication_tf,copies_label,copies_tf};
				int result = JOptionPane.showConfirmDialog(this, ob,"Add Book Form",JOptionPane.OK_CANCEL_OPTION);

				if(result == JOptionPane.OK_OPTION){
					if(title_tf.getText().equals("") || author_tf.getText().equals("") || publication_tf.getText().equals("")){
						JOptionPane.showMessageDialog(this, "Form is not complete.", "Bad Input", JOptionPane.ERROR_MESSAGE);
					}
					else{
						try{
							Book myBook = new Book(0,title_tf.getText(),author_tf.getText(),publication_tf.getText(),Integer.parseInt(copies_tf.getText()));
							librarian.add_book(myBook);
							librarian.update_table(mb_panel.table, new String[]{"ID","TITLE","AUTHER","PUBLICATION","COPIES"}, librarian.books_to_array(librarian.get_books()));
						}
						catch(NumberFormatException e){
							JOptionPane.showMessageDialog(this, "No. of copies is not a number.", "Bad Input", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
			else if(ae.getSource()==remove_book_by_id_btn){
				rbd.setVisible(true);
			}
			else if(ae.getSource()==remove_book_btn){
				data = librarian.books_to_array(librarian.get_books());
				int[] selectedRows = table.getSelectedRows();
				int result = JOptionPane.showConfirmDialog(this, "Do you really want to remove "+selectedRows.length+" book(s)?","Confirm",JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION){
					for(int x:selectedRows){
						librarian.remove_book((int)data[x][0]);
					}
					librarian.update_table(table, new String[]{"ID","TITLE","AUTHER","PUBLICATION","COPIES"}, librarian.books_to_array(librarian.get_books()));
					System.out.println(selectedRows.length + " book(s) removed from database.");
				}
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
					librarian.update_table(table, new String[]{"ID","TITLE","AUTHER","PUBLICATION","COPIES"}, librarian.books_to_array(librarian.get_books()));
				}
			}
		}
	}

	class ManageMembersPanel extends JPanel implements ActionListener{
		private JButton add_member_btn;
		private JButton remove_member_by_id_btn;
		private JButton remove_btn;
		private JButton import_members_btn;
		private JTable table;
		private JScrollPane table_sp;
		private GroupLayout layout;
		private Object[][] data;

		ManageMembersPanel(){
			setName("manage_memberships");

			add_member_btn = new JButton("Add Member");
			remove_member_by_id_btn = new JButton("Remove Member By Id");
			remove_btn = new JButton("Remove");
			import_members_btn = new JButton("Import Members");

			table = new JTable();
			table_sp = new JScrollPane(table);

			add(table_sp);
			add(add_member_btn);
			add(import_members_btn);
			add(remove_member_by_id_btn);

			add_member_btn.addActionListener(this);
			remove_member_by_id_btn.addActionListener(this);
			import_members_btn.addActionListener(this);
			remove_btn.addActionListener(this);
			// table.getSelectionModel().addListSelectionListener(this);

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
					.addComponent(remove_member_by_id_btn)
					.addComponent(remove_btn))
			);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(table_sp)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(add_member_btn)
					.addComponent(import_members_btn)
					.addComponent(remove_member_by_id_btn)
					.addComponent(remove_btn))
			);
		}
		public void actionPerformed(ActionEvent ae){
			if(ae.getSource()==add_member_btn){
				AddMemberDialog amd = new AddMemberDialog(LibraryInterface.this,librarian,mm_panel.table);
				amd.setVisible(true);
			}
			else if(ae.getSource()==remove_member_by_id_btn){
				RemoveMemberDialog rmd = new RemoveMemberDialog(LibraryInterface.this,librarian,mm_panel.table);
				rmd.setVisible(true);
			}
			else if(ae.getSource()==remove_btn){
				data = librarian.members_to_array(librarian.get_members());
				int[] selectedRows = table.getSelectedRows();
				int result = JOptionPane.showConfirmDialog(this, "Do you really want to remove "+selectedRows.length+" member(s)?","Confirm",JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION){
					for(int x:selectedRows){
						librarian.remove_member((int)data[x][0]);
					}
					librarian.update_table(table, new String[]{"ID","NAME","EMAIL","SEMESTER"}, librarian.members_to_array(librarian.get_members()));
					System.out.println(selectedRows.length + " book(s) removed from database.");
				}
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
					librarian.update_table(table, new String[]{"ID","NAME","EMAIL","SEMESTER"}, librarian.members_to_array(librarian.get_members()));
				}
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
			setLocationRelativeTo(null);
			setResizable(false);
			add(book_id_label);
			add(book_id_tf);
			add(btn);
		}
		public void actionPerformed(ActionEvent ae){
			try{
				librarian.remove_book(Integer.parseInt(book_id_tf.getText()));
				librarian.update_table(mb_panel.table, new String[]{"ID","TITLE","AUTHER","PUBLICATION","COPIES"}, librarian.books_to_array(librarian.get_books()));
				setVisible(false);
			}
			catch(NumberFormatException e){
				System.out.println("ERROR: Book id provided is not valid.");
				JOptionPane.showMessageDialog(this, "Please enter a valid book id.", "Bad Input", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
