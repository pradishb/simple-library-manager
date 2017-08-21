package slm;

import javax.swing.filechooser.FileNameExtensionFilter;  
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class LibraryInterface{
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	private static final int MARGIN = 10;
	
	public static void init_interface(){
		JLabel ib_member_id_label;
		JLabel ib_book_id_label;
		JLabel id_label;
		JLabel title_label;
		JLabel author_label;
		JLabel publication_label;
		JTextField ib_member_id_tf;
		JTextField ib_book_id_tf;
		JTextField mb_add_book_title_tf;
		JTextField mb_add_book_author_tf;
		JTextField mb_add_book_publication_tf;
		JTextField mb_remove_book_id_tf;
		JFileChooser chooser;
		JButton ib_btn;
		JButton mb_add_book_btn;
		JButton mb_remove_book_btn;
		JButton mb_add_book_dialog_btn;
		JButton mb_remove_book_dialog_btn;
		JButton mb_import_books_btn;
		JDialog mb_add_book_dialog;
		JDialog mb_remove_book_dialog;
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
		GroupLayout mb_add_book_dialog_layout;
		GroupLayout mb_remove_book_dialog_layout;

		//Initialization
		ib_member_id_label = new JLabel("Enter Member Id");
		ib_book_id_label = new JLabel("Enter Book Id");
		id_label = new JLabel("Book Id:");
		title_label = new JLabel("Title:");
		author_label = new JLabel("Author:");
		publication_label = new JLabel("Publication:");
		ib_member_id_tf = new JTextField();
		ib_book_id_tf = new JTextField();
		mb_add_book_title_tf = new JTextField();
		mb_add_book_author_tf = new JTextField();
		mb_add_book_publication_tf = new JTextField();
		mb_remove_book_id_tf = new JTextField();
		chooser = new JFileChooser();
		ib_btn = new JButton("Issue Book");
		mb_add_book_btn = new JButton("Add Book");
		mb_remove_book_btn = new JButton("Remove Book");
		mb_import_books_btn = new JButton("Import Books");
		mb_add_book_dialog_btn = new JButton("Add Book");
		mb_remove_book_dialog_btn = new JButton("Remove Book");
		mb_table = new JTable();		
		ib_panel = new JPanel();
		mb_panel = new JPanel();
		mm_panel = new JPanel();
		jtp = new JTabbedPane();
		library_window = new JFrame("Simple Library Manager");
		mb_add_book_dialog = new JDialog(library_window, "Add Book Form", true);
		mb_remove_book_dialog = new JDialog(library_window, "Remove Book Form", true);

		//Buttons
		mb_add_book_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				 mb_add_book_dialog.setVisible(true);
			}
		});
		mb_import_books_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
				int returnVal = chooser.showOpenDialog(library_window);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					try{
						LibrarySystem.import_books(chooser.getSelectedFile());
						LibrarySystem.update_table(mb_table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, LibrarySystem.books_to_array(LibrarySystem.get_books()));
					}catch(InvalidCsvFormatException e){
						System.out.println(e.getMessage());
					}
				}

			}
		});
			
		mb_remove_book_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				 mb_remove_book_dialog.setVisible(true);
			}
		});	
		mb_add_book_dialog_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				Book myBook = new Book(0,mb_add_book_title_tf.getText(),mb_add_book_author_tf.getText(),mb_add_book_publication_tf.getText());
				Librarian.add_book(myBook);
				LibrarySystem.update_table(mb_table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, LibrarySystem.books_to_array(LibrarySystem.get_books()));
				mb_add_book_dialog.setVisible(false);
			}
		});
		mb_remove_book_dialog_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				Librarian.remove_book(Integer.parseInt(mb_remove_book_id_tf.getText()));
				LibrarySystem.update_table(mb_table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, LibrarySystem.books_to_array(LibrarySystem.get_books()));
				mb_remove_book_dialog.setVisible(false);
			}
		});

		//Dialogs
		mb_add_book_dialog_layout = new GroupLayout(mb_add_book_dialog.getContentPane());
		mb_add_book_dialog.getContentPane().setLayout(mb_add_book_dialog_layout);
		mb_add_book_dialog.setSize(400,180);
		mb_remove_book_dialog_layout = new GroupLayout(mb_remove_book_dialog.getContentPane());
		mb_remove_book_dialog.getContentPane().setLayout(mb_remove_book_dialog_layout);
		mb_remove_book_dialog.setSize(400,130);
		mb_add_book_dialog.add(title_label);
		mb_add_book_dialog.add(mb_add_book_title_tf);
		mb_add_book_dialog.add(author_label);
		mb_add_book_dialog.add(mb_add_book_author_tf);
		mb_add_book_dialog.add(publication_label);
		mb_add_book_dialog.add(mb_add_book_publication_tf);
		mb_add_book_dialog.add(mb_add_book_dialog_btn);
		mb_remove_book_dialog.add(id_label);
		mb_remove_book_dialog.add(mb_remove_book_id_tf);
		mb_remove_book_dialog.add(mb_remove_book_dialog_btn);

		//Tables
		mb_table.setEnabled(false);
		mb_table_sp = new JScrollPane(mb_table);

		//Panel
		ib_panel.setName("issue_book");
		mb_panel.setName("manage_books");
		mm_panel.setName("manage_memberships");
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
		mb_panel.add(mb_remove_book_btn);

		//Layout Configuration
		ib_layout.setAutoCreateGaps(true);
		ib_layout.setAutoCreateContainerGaps(true);
		mb_layout.setAutoCreateGaps(true);
		mb_layout.setAutoCreateContainerGaps(true);
		mb_add_book_dialog_layout.setAutoCreateGaps(true);
		mb_add_book_dialog_layout.setAutoCreateContainerGaps(true);
		mb_remove_book_dialog_layout.setAutoCreateGaps(true);
		mb_remove_book_dialog_layout.setAutoCreateContainerGaps(true);
		//issue book panel
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
		//manage book panel
		mb_layout.setHorizontalGroup(
			mb_layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(mb_table_sp)
				.addGroup(mb_layout.createSequentialGroup()
					.addComponent(mb_add_book_btn)
					.addComponent(mb_import_books_btn)
					.addComponent(mb_remove_book_btn))
			);
		mb_layout.setVerticalGroup(
			mb_layout.createSequentialGroup()
			.addComponent(mb_table_sp)
			.addGroup(mb_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(mb_add_book_btn)
				.addComponent(mb_import_books_btn)
				.addComponent(mb_remove_book_btn))
			);
		//add book dialog
		mb_add_book_dialog_layout.setHorizontalGroup(
			mb_add_book_dialog_layout.createSequentialGroup()
			.addGroup(mb_add_book_dialog_layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(title_label)
				.addComponent(author_label)
				.addComponent(publication_label))
			.addGroup(mb_add_book_dialog_layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(mb_add_book_title_tf)
				.addComponent(mb_add_book_author_tf)
				.addComponent(mb_add_book_publication_tf)
				.addComponent(mb_add_book_dialog_btn))
			);
		mb_add_book_dialog_layout.setVerticalGroup(
			mb_add_book_dialog_layout.createSequentialGroup()
			.addGroup(mb_add_book_dialog_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(title_label)
				.addComponent(mb_add_book_title_tf))
			.addGroup(mb_add_book_dialog_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(author_label)
				.addComponent(mb_add_book_author_tf))
			.addGroup(mb_add_book_dialog_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(publication_label)
				.addComponent(mb_add_book_publication_tf))
			.addComponent(mb_add_book_dialog_btn)
			);
		//remove book dialog
		mb_remove_book_dialog_layout.setHorizontalGroup(
			mb_remove_book_dialog_layout.createSequentialGroup()
			.addGroup(mb_remove_book_dialog_layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(id_label))
			.addGroup(mb_remove_book_dialog_layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(mb_remove_book_id_tf)
				.addComponent(mb_remove_book_dialog_btn))
			);
		mb_remove_book_dialog_layout.setVerticalGroup(
			mb_remove_book_dialog_layout.createSequentialGroup()
			.addGroup(mb_remove_book_dialog_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(id_label)
				.addComponent(mb_remove_book_id_tf))
			.addComponent(mb_remove_book_dialog_btn)
			);

		//Tabbed Pane
		jtp.setBounds(MARGIN,MARGIN,WIDTH-2*MARGIN-5,HEIGHT-2*MARGIN-35);
		jtp.addTab("Issue Book", ib_panel);
		jtp.addTab("Managae Books", mb_panel);
		jtp.addTab("Managae Memberships", mm_panel);
		jtp.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e){
				if(jtp.getSelectedComponent().getName()=="manage_books"){
					LibrarySystem.update_table(mb_table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, LibrarySystem.books_to_array(LibrarySystem.get_books()));
				}
			}
		});

		//Frame
		library_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		library_window.setVisible(true);
		library_window.setLayout(null);
		library_window.setSize(WIDTH,HEIGHT);
		library_window.setResizable(false);	
		library_window.add(jtp);
	}
}