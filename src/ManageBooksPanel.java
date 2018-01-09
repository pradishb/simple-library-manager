package slm;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class ManageBooksPanel extends JPanel implements ListSelectionListener,ActionListener{
	private JButton add_book_btn;
	private JButton remove_book_by_id_btn;
	private JButton remove_book_btn;
	private JButton update_book_btn;
	private JButton import_books_btn;
	private JTable table;
	private JScrollPane table_sp;
	private GroupLayout layout;
	private JFileChooser chooser;
	private Librarian librarian;
	private Object[][] data;
	ManageBooksPanel(Librarian librarian){
		setName("manage_books");

		this.librarian = librarian;

		add_book_btn = new JButton("Add Book");
		remove_book_by_id_btn = new JButton("Remove Book By Id");
		remove_book_btn = new JButton("Remove Book");
		update_book_btn = new JButton("Update Book");
		import_books_btn = new JButton("Import Books");

		table = new JTable();
		table_sp = new JScrollPane(table);
		chooser = new JFileChooser();

		add(table_sp);
		add(add_book_btn);
		add(import_books_btn);
		add(remove_book_by_id_btn);

		add_book_btn.addActionListener(this);
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
			.addComponent(table_sp)
			.addGroup(layout.createSequentialGroup()
				.addComponent(add_book_btn)
				.addComponent(import_books_btn)
				.addComponent(update_book_btn)
				.addComponent(remove_book_by_id_btn)
				.addComponent(remove_book_btn))
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
			.addComponent(table_sp)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(add_book_btn)
				.addComponent(import_books_btn)
				.addComponent(update_book_btn)
				.addComponent(remove_book_by_id_btn)
				.addComponent(remove_book_btn))
		);
	}

	public void update_table(){
		librarian.update_table(table, new String[]{"ID","TITLE","AUTHER","PUBLICATION","COPIES"}, librarian.books_to_array(librarian.get_books()));
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
						update_table();
					}
					catch(NumberFormatException e){
						JOptionPane.showMessageDialog(this, "No. of copies is not a number.", "Bad Input", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		else if(ae.getSource()==update_book_btn){
			int id = (int)table.getValueAt(table.getSelectedRow(), 0);
			Book myBook = librarian.get_book(id);

			JLabel title_label = new JLabel("Title:");
			JLabel author_label = new JLabel("Author:");
			JLabel publication_label = new JLabel("Publication:");
			JLabel copies_label = new JLabel("No. of copies:");
			JTextField title_tf = new JTextField(myBook.get_title());
			JTextField author_tf = new JTextField(myBook.get_author());	
			JTextField publication_tf = new JTextField(myBook.get_publication());
			JTextField copies_tf = new JTextField(Integer.toString(myBook.get_copies()));

			Object[] ob = {title_label,title_tf,author_label,author_tf,publication_label,publication_tf,copies_label,copies_tf};
			int result = JOptionPane.showConfirmDialog(this, ob,"Update Book Form",JOptionPane.OK_CANCEL_OPTION);

			if(result == JOptionPane.OK_OPTION){
				if(title_tf.getText().equals("") || author_tf.getText().equals("") || publication_tf.getText().equals("")){
					JOptionPane.showMessageDialog(this, "Form is not complete.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				}
				else{
					try{
						myBook.update_data(myBook.get_id(),title_tf.getText(),author_tf.getText(),publication_tf.getText(),Integer.parseInt(copies_tf.getText()));
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
			data = librarian.books_to_array(librarian.get_books());
			int[] selectedRows = table.getSelectedRows();
			int result = JOptionPane.showConfirmDialog(this, "Do you really want to remove "+selectedRows.length+" book(s)?","Confirm",JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION){
				for(int x:selectedRows){
					librarian.remove_book((int)data[x][0]);
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
					librarian.import_books(chooser.getSelectedFile());
				}catch(InvalidCsvFormatException e){
					System.out.println(e.getMessage());
					JOptionPane.showMessageDialog(this, "Some errors occured while import the CSV file.", "Bad Input File", JOptionPane.ERROR_MESSAGE);
				}
				update_table();
			}
		}
	}
}
