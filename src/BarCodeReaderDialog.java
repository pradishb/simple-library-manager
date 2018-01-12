package slm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

interface Updatable{
	void update_table();
}

public class BarcodeReaderDialog extends JDialog implements BarcodeListener{
	JLabel msg;
	String isbn;
	private Librarian librarian;
	Updatable updater;

	public BarcodeReaderDialog(JFrame parent, Librarian librarian, Updatable updater){
		super(parent,"Bar Code Reader",true);
		this.librarian = librarian;
		this.updater = updater;
		init();
	}

	public void init(){
		BarcodeReader br = new BarcodeReader();
		br.addBarcodeListener(this);

		msg = new JLabel("Wating for barcode reader...");
		add(msg);


		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				br.removeBarcodeListener(BarcodeReaderDialog.this);
				dispose();
			}
		});
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
		setSize(500,100);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void onBarcodeRead(String isbn){
		System.out.println("Barcode Accepted");
		this.isbn = isbn;
		msg.setText("Searching for book with ISBN "+isbn+" in the internet...");
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				search();
				return null;
			}

			@Override
			protected void done() {
				msg.setText("Waiting for barcode reader...");
			}
		}.execute();
	}

	public void search(){
		System.out.println("Searching for book with ISBN "+isbn+" in the internet...");
		try{
			Book myBook = Barcode.ISBN_to_book(isbn);

			JLabel isbn_label = new JLabel("ISBN:");
			JLabel title_label = new JLabel("Title:");
			JLabel author_label = new JLabel("Author:");
			JLabel publication_label = new JLabel("Publication:");
			JLabel copies_label = new JLabel("No. of copies:");
			JTextField isbn_tf = new JTextField(myBook.get_isbn());
			JTextField title_tf = new JTextField(myBook.get_title());
			JTextField author_tf = new JTextField(myBook.get_author());	
			JTextField publication_tf = new JTextField(myBook.get_publication());
			JTextField copies_tf = new JTextField("1");

			Object[] ob = {isbn_label,isbn_tf,title_label,title_tf,author_label,author_tf,publication_label,publication_tf,copies_label,copies_tf};
			int result = JOptionPane.showConfirmDialog(this, ob,"Add Book Form",JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION){
				if(title_tf.getText().equals("") || author_tf.getText().equals("") || publication_tf.getText().equals("")){
					JOptionPane.showMessageDialog(this, "Form is not complete.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				}
				else{
					try{
						myBook.update_data(0,isbn_tf.getText(),title_tf.getText(),author_tf.getText(),publication_tf.getText(),Integer.parseInt(copies_tf.getText()));
						librarian.add_book(myBook);
						updater.update_table();
						JOptionPane.showMessageDialog(this, "Book has been added.");
					}
					catch(NumberFormatException e){
						JOptionPane.showMessageDialog(this, "No. of copies is not a number.", "Bad Input", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		catch(BookNotFoundException e){
			JOptionPane.showMessageDialog(this, "Book having ISBN "+isbn+" not found in google books.", "Not Found", JOptionPane.ERROR_MESSAGE);
		}
		catch(ConnectionException e){
			JOptionPane.showMessageDialog(this, "Cannot connect to google book database server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}