package slm;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class ManageBooksPanel extends TablePanel implements ListSelectionListener,ActionListener,Updatable{
	private JButton add_book_btn;
	private JButton add_using_barcode_btn;
	private JButton remove_book_by_id_btn;
	private JButton remove_book_btn;
	private JButton update_book_btn;
	private JButton import_books_btn;
	private GroupLayout layout;
	private JFileChooser chooser;
	private Librarian librarian;
	ManageBooksPanel(String[] cols,Librarian librarian){
		super(cols);
		setName("manage_books");

		this.librarian = librarian;

		add_book_btn = new JButton("Add Book");
		add_using_barcode_btn = new JButton("Add Using Barcode");
		remove_book_by_id_btn = new JButton("Remove Book By Id");
		remove_book_btn = new JButton("Remove Book");
		update_book_btn = new JButton("Update Book");
		import_books_btn = new JButton("Import Books");

		chooser = new JFileChooser();

		add(table_sp);
		add(add_book_btn);
		add(import_books_btn);
		add(remove_book_by_id_btn);

		add_book_btn.addActionListener(this);
		add_using_barcode_btn.addActionListener(this);
		remove_book_by_id_btn.addActionListener(this);
		import_books_btn.addActionListener(this);
		remove_book_btn.addActionListener(this);
		update_book_btn.addActionListener(this);
		table.getSelectionModel().addListSelectionListener(this);
		remove_book_btn.setEnabled(false);	
		update_book_btn.setEnabled(false);	

		layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
		layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addComponent(input))
			.addComponent(table_sp)
			.addGroup(layout.createSequentialGroup()
				.addComponent(add_book_btn)
				.addComponent(add_using_barcode_btn)
				.addComponent(import_books_btn)
				.addComponent(update_book_btn)
				.addComponent(remove_book_by_id_btn)
				.addComponent(remove_book_btn))
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(input))
			.addComponent(table_sp)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(add_book_btn)
				.addComponent(add_using_barcode_btn)
				.addComponent(import_books_btn)
				.addComponent(update_book_btn)
				.addComponent(remove_book_by_id_btn)
				.addComponent(remove_book_btn))
		);
	}

	@Override
	public void update_table(){
		update(librarian.books_to_array(librarian.get_books()));
	}

	public void valueChanged(ListSelectionEvent le){
		if(table.getSelectedRow()!=-1){
			remove_book_btn.setEnabled(true);
		}
		else{
			remove_book_btn.setEnabled(false);	
		}

		if(table.getSelectedRowCount()==1){
			update_book_btn.setEnabled(true);
		}
		else{
			update_book_btn.setEnabled(false);	
		}
	}
	
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==add_book_btn){
	 		JLabel title_label = new JLabel("Title:");
	 		JLabel isbn_label = new JLabel("ISBN:");
			JLabel author_label = new JLabel("Author:");
			JLabel publication_label = new JLabel("Publication:");
			JLabel copies_label = new JLabel("No. of copies:");
			JTextField isbn_tf = new JTextField(20);
			JTextField title_tf = new JTextField(20);
			JTextField author_tf = new JTextField(20);	
			JTextField publication_tf = new JTextField(20);
			JTextField copies_tf = new JTextField(20);
			int result;
			do{
				isbn_tf.setText("");
				title_tf.setText("");
				author_tf.setText("");
				publication_tf.setText("");
				copies_tf.setText("");
				Object[] ob = {isbn_label,isbn_tf,title_label,title_tf,author_label,author_tf,publication_label,publication_tf,copies_label,copies_tf};
				Object[] options = {"Add","Add & Continue","Cancel"};
				result = JOptionPane.showOptionDialog(this,ob,"Add Book Form",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,null);

				if(result == 0 || result == 1){
					if(isbn_tf.getText().equals("") || title_tf.getText().equals("") || author_tf.getText().equals("") || publication_tf.getText().equals("")){
						JOptionPane.showMessageDialog(this, "Form is not complete.", "Bad Input", JOptionPane.ERROR_MESSAGE);
					}
					else{
						try{
							Book myBook = new Book(0,isbn_tf.getText(),title_tf.getText(),author_tf.getText(),publication_tf.getText(),Integer.parseInt(copies_tf.getText()));
							librarian.add_book(myBook);
							update_table();
							JOptionPane.showMessageDialog(this, "Book has been added.");
						}
						catch(NumberFormatException e){
							JOptionPane.showMessageDialog(this, "No. of copies is not a number.", "Bad Input", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
			while(result==1);
		}
		else if(ae.getSource()==add_using_barcode_btn){
	 		BarcodeReaderDialog dialog = new BarcodeReaderDialog((JFrame) SwingUtilities.getWindowAncestor(this),librarian,this);
		}
		else if(ae.getSource()==update_book_btn){
			int id = (int)table.getValueAt(table.getSelectedRow(), 0);
			Book myBook = librarian.get_book(id);

			JLabel isbn_label = new JLabel("ISBN:");
			JLabel title_label = new JLabel("Title:");
			JLabel author_label = new JLabel("Author:");
			JLabel publication_label = new JLabel("Publication:");
			JLabel copies_label = new JLabel("No. of copies:");
			JTextField isbn_tf = new JTextField(myBook.get_isbn());
			JTextField title_tf = new JTextField(myBook.get_title());
			JTextField author_tf = new JTextField(myBook.get_author());	
			JTextField publication_tf = new JTextField(myBook.get_publication());
			JTextField copies_tf = new JTextField(Integer.toString(myBook.get_copies()));

			Object[] ob = {isbn_label,isbn_tf,title_label,title_tf,author_label,author_tf,publication_label,publication_tf,copies_label,copies_tf};
			int result = JOptionPane.showConfirmDialog(this, ob,"Update Book Form",JOptionPane.OK_CANCEL_OPTION);

			if(result == JOptionPane.OK_OPTION){
				if(isbn_tf.getText().equals("") || title_tf.getText().equals("") || author_tf.getText().equals("") || publication_tf.getText().equals("")){
					JOptionPane.showMessageDialog(this, "Form is not complete.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				}
				else{
					try{
						myBook.update_data(myBook.get_id(),isbn_tf.getText(),title_tf.getText(),author_tf.getText(),publication_tf.getText(),Integer.parseInt(copies_tf.getText()));
						librarian.update_book(myBook);
						update_table();
					}
					catch(NumberFormatException e){
						JOptionPane.showMessageDialog(this, "No. of copies is not a number.", "Bad Input", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		else if(ae.getSource()==remove_book_by_id_btn){
			String inputValue = JOptionPane.showInputDialog("Enter book id");
			if(inputValue!=null){
				try{
					librarian.remove_book(Integer.parseInt(inputValue));
					update_table();
				}
				catch(NumberFormatException e){
					System.out.println("ERROR: Book id provided is not valid.");
					JOptionPane.showMessageDialog(this, "Please enter a valid book id.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				} 
			}
		}
		else if(ae.getSource()==remove_book_btn){
			int[] selectedRows = table.getSelectedRows();
			int result = JOptionPane.showConfirmDialog(this, "Do you really want to remove "+selectedRows.length+" book(s)?","Confirm",JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION){
				for(int x:selectedRows){
					int id = (int)table.getValueAt(x, 0);
					librarian.remove_book(id);
				}
				update_table();
				System.out.println(selectedRows.length + " book(s) removed from database.");
			}
		}
		else{
			chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				try{
					int imported = librarian.import_books(chooser.getSelectedFile());
					if(imported!=0){
						update_table();
						JOptionPane.showMessageDialog(this, imported+" book(s) has been added.");
					}
				}catch(InvalidCsvFormatException e){
					System.out.println(e.getMessage());
					JOptionPane.showMessageDialog(this, "The following columns were not found in the CSV file :\n"+String.join(", ", e.get_missing_cols()), "Bad Input File", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
