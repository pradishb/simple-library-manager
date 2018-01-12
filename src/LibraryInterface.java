package slm;

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
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	private JTabbedPane jtp;
	private GroupLayout layout;
	private ManageMembersPanel mm_panel;
	private IssueBookPanel ib_panel;
	private ManageBooksPanel mb_panel;
	private TransactionsPanel tr_panel;
	// private SearchPanel s_panel;
	private SettingsPanel set_panel;
	private MyMenuBar menu;

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
		mm_panel = new ManageMembersPanel(new String[]{"ID","NAME","EMAIL","SEMESTER"},librarian);
		ib_panel = new IssueBookPanel(librarian);
		mb_panel = new ManageBooksPanel(new String[]{"ID","ISBN","TITLE","AUTHOR","PUBLICATION","TOTAL COPIES"},librarian);
		tr_panel = new TransactionsPanel(new String[]{"ID","ISBN","BOOK","BORROWED BY","BORROWED TIME"},librarian);
		set_panel = new SettingsPanel(db_manager,security_manager);

		menu = new MyMenuBar();
		jtp = new JTabbedPane();
	}

	public void load_interfaces(){
		jtp.addTab("Issue Book", ib_panel);
		jtp.addTab("Manage Books", mb_panel);
		jtp.addTab("Manage Memberships", mm_panel);
		jtp.addTab("Transactions", tr_panel);
		// jtp.addTab("Search", s_panel);
		jtp.addTab("Settings", set_panel);
		
		jtp.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e){
				if(jtp.getSelectedComponent().getName()=="manage_books"){
					mb_panel.reset();
					mb_panel.update_table();
				}else if(jtp.getSelectedComponent().getName()=="manage_memberships"){
					mm_panel.reset();
					mm_panel.update_table();
				}else if(jtp.getSelectedComponent().getName()=="transactions"){
					tr_panel.reset();
					tr_panel.update_table();
				}else if(jtp.getSelectedComponent().getName()=="settings"){
					set_panel.update_panel();
				}else if(jtp.getSelectedComponent().getName()=="issue_book"){
					ib_panel.reset();
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
		setJMenuBar(menu);

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

		ib_panel.reset();
	}

}
