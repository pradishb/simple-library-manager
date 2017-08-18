package slm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LibraryInterface{
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	private static final int MARGIN = 10;

	public static void init_interface(){
		JLabel ib_member_id_label;
		JLabel ib_book_id_label;
		JTextField ib_member_id_tf;
		JTextField ib_book_id_tf;
		JButton ib_btn;
		JButton mb_add_book_btn;
		JButton mb_import_books_btn;
		JTable mb_table;
		JScrollPane mb_table_sp;
		JPanel ib_panel;
		JPanel mb_panel;
		JPanel mb_add_book_panel;
		JPanel mb_update_book_panel;
		JPanel mb_remove_book_panel;
		JPanel mm_panel;
		JTabbedPane jtp;
		JFrame library_window;
		GroupLayout ib_layout;
		GroupLayout mb_layout;

		//Labels
		ib_member_id_label = new JLabel("Enter Member Id");
		ib_book_id_label = new JLabel("Enter Book Id");

		//Text Fields
		ib_member_id_tf = new JTextField(10);
		ib_book_id_tf = new JTextField(10);

		//Buttons
		ib_btn = new JButton("Issue Book");
		mb_add_book_btn = new JButton("Add Book");
		mb_import_books_btn = new JButton("Import Books");

		//Tables
		String data[][]={ {"1","Something","Someone","Some Publication"},    
		{"1","Something","Someone","Some Publication"},    
		{"1","Something","Someone","Some Publication"}};    
		String column[]={"ID","TITLE","AUTHER","PUBLICATION"};  
		mb_table = new JTable(data,column);		
		mb_table_sp =  new JScrollPane(mb_table);

		//Panels
		ib_panel = new JPanel();
		mb_panel = new JPanel();
		mm_panel = new JPanel();
		ib_panel.add(ib_member_id_label);
		ib_panel.add(ib_member_id_tf);
		ib_panel.add(ib_book_id_label);
		ib_panel.add(ib_book_id_tf);
		ib_panel.add(ib_btn);
		ib_layout = new GroupLayout(ib_panel);
		ib_panel.setLayout(ib_layout);
		mb_layout = new GroupLayout(mb_panel);
		mb_panel.setLayout(mb_layout);
		mb_panel.add(mb_table_sp);
		mb_panel.add(mb_add_book_btn);
		mb_panel.add(mb_import_books_btn);

		//Layout Configuration
		ib_layout.setAutoCreateGaps(true);
		ib_layout.setAutoCreateContainerGaps(true);
		mb_layout.setAutoCreateGaps(true);
		mb_layout.setAutoCreateContainerGaps(true);
		ib_layout.setHorizontalGroup(
			ib_layout.createSequentialGroup()
			.addGroup(ib_layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(ib_book_id_label)
				.addComponent(ib_member_id_label))
			.addGroup(ib_layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(ib_book_id_tf)
				.addComponent(ib_member_id_tf)
				.addComponent(ib_btn))
			);
		ib_layout.setVerticalGroup(
			ib_layout.createSequentialGroup()
			.addGroup(ib_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(ib_book_id_label)
				.addComponent(ib_book_id_tf))
			.addGroup(ib_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(ib_member_id_label)
				.addComponent(ib_member_id_tf))
			.addComponent(ib_btn)
			);
		mb_layout.setHorizontalGroup(
			mb_layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(mb_table_sp)
				.addGroup(mb_layout.createSequentialGroup()
					.addComponent(mb_add_book_btn)
					.addComponent(mb_import_books_btn))
			);
		mb_layout.setVerticalGroup(
			mb_layout.createSequentialGroup()
			.addComponent(mb_table_sp)
			.addGroup(mb_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(mb_add_book_btn)
				.addComponent(mb_import_books_btn))
			);	



		//Tabbed Pane
		jtp = new JTabbedPane();
		jtp.setBounds(MARGIN,MARGIN,WIDTH-2*MARGIN-5,HEIGHT-2*MARGIN-35);
		jtp.addTab("Issue Book", ib_panel);
		jtp.addTab("Managae Books", mb_panel);
		jtp.addTab("Managae Memberships", mm_panel);

		//Frame
		library_window = new JFrame("Simple Library Manager");
		library_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		library_window.setVisible(true);
		library_window.setLayout(null);
		library_window.setSize(WIDTH,HEIGHT);
		library_window.setResizable(false);	
		library_window.add(jtp);

	}
}