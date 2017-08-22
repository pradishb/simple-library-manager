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
	private static JLabel ib_member_id_label;
	private static JLabel ib_book_id_label;
	private static JTextField ib_member_id_tf;
	private static JTextField ib_book_id_tf;
	private static JFileChooser chooser;
	private static JButton ib_btn;
	private static JButton mb_add_book_btn;
	private static JButton mb_remove_book_btn;
	private static JButton mb_import_books_btn;
	private static JTable mb_table;
	private static JScrollPane mb_table_sp;
	private static JPanel ib_panel;
	private static JPanel mb_panel;
	private static JPanel mb_add_book_panel;
	private static JPanel mb_update_book_panel;
	private static JPanel mb_remove_book_panel;
	private static JPanel mm_panel;
	private static JTabbedPane jtp;
	private static JFrame library_window;
	private static GroupLayout layout;
	private static GroupLayout ib_layout;
	private static GroupLayout mb_layout;
	private static RemoveBookDialog rbd;
	private static AddBookDialog abd;

	public static void init_interface(){
		System.out.println("Initializing Interface...");
		//Initialization
		ib_member_id_label = new JLabel("Enter Member Id");
		ib_book_id_label = new JLabel("Enter Book Id");
		ib_member_id_tf = new JTextField();
		ib_book_id_tf = new JTextField();
		chooser = new JFileChooser();
		ib_btn = new JButton("Issue Book");
		mb_add_book_btn = new JButton("Add Book");
		mb_remove_book_btn = new JButton("Remove Book");
		mb_import_books_btn = new JButton("Import Books");
		mb_table = new JTable();		
		ib_panel = new JPanel();
		mb_panel = new JPanel();
		mm_panel = new JPanel();
		jtp = new JTabbedPane();
		library_window = new JFrame("Simple Library Manager");
		rbd = new RemoveBookDialog();
		abd = new AddBookDialog();

	}

	

	public static void load_interface(){
		System.out.println("Loading Interface...");
		//Buttons
		mb_add_book_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				 abd.setVisible(true);
			}
		});
		mb_import_books_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
				int returnVal = chooser.showOpenDialog(library_window);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					try{
						LibrarySystem.import_books(chooser.getSelectedFile());
					}catch(InvalidCsvFormatException e){
						System.out.println(e.getMessage());
					}
					LibrarySystem.update_table(mb_table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, LibrarySystem.books_to_array(LibrarySystem.get_books()));
				}

			}
		});
			
		mb_remove_book_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				 rbd.setVisible(true);
			}
		});	

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

		//Tabbed Pane
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
		layout = new GroupLayout(library_window.getContentPane());
		library_window.getContentPane().setLayout(layout);
		library_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		library_window.setVisible(true);
		library_window.setSize(WIDTH,HEIGHT);
		library_window.setResizable(false);	
		library_window.add(jtp);

		//Layout Configuration
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		ib_layout.setAutoCreateGaps(true);
		ib_layout.setAutoCreateContainerGaps(true);
		mb_layout.setAutoCreateGaps(true);
		mb_layout.setAutoCreateContainerGaps(true);
		//frame layout
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addComponent(jtp)
			);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(jtp)
			);
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
	}

	static class AddBookDialog extends JDialog{
		JLabel title_label;
		JLabel author_label;
		JLabel publication_label;
		JTextField title_tf;
		JTextField author_tf;
		JTextField publication_tf;
		JButton btn;
		
		GroupLayout layout;

		AddBookDialog(){
			super(library_window,"Add Book Form",false);

			title_label = new JLabel("Title:");
			author_label = new JLabel("Author:");
			publication_label = new JLabel("Publication:");
			title_tf = new JTextField();
			author_tf = new JTextField();
			publication_tf = new JTextField();
			btn = new JButton("Add Book");

			layout = new GroupLayout(getContentPane());
			getContentPane().setLayout(layout);

			btn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					Book myBook = new Book(0,title_tf.getText(),author_tf.getText(),publication_tf.getText());
					Librarian.add_book(myBook);
					LibrarySystem.update_table(mb_table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, LibrarySystem.books_to_array(LibrarySystem.get_books()));
					setVisible(false);
				}
			});

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
			
			setSize(400,180);
			add(title_label);
			add(title_tf);
			add(author_label);
			add(author_tf);
			add(publication_label);
			add(publication_tf);
			add(btn);
		}	
	}

	static class RemoveBookDialog extends JDialog{
		JLabel book_id_label;
		JTextField book_id_tf;
		JButton btn;
		GroupLayout layout;

		RemoveBookDialog(){
			super(library_window,"Remove Book Form",false);

			book_id_tf = new JTextField();
			btn = new JButton("Remove Book");
			book_id_label = new JLabel("Book Id:");
			layout = new GroupLayout(getContentPane());
			getContentPane().setLayout(layout);

			btn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					Librarian.remove_book(Integer.parseInt(book_id_tf.getText()));
					LibrarySystem.update_table(mb_table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, LibrarySystem.books_to_array(LibrarySystem.get_books()));
					setVisible(false);
				}
			});

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

			
			setSize(400,130);
			add(book_id_label);
			add(book_id_tf);
			add(btn);
		}	
	}
}

