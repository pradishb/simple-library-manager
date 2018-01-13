package slm;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.krysalis.barcode4j.impl.upcean.UPCEANLogicImpl;

public class TransactionsPanel extends TablePanel implements ListSelectionListener,Updatable,ItemListener,BarcodeListener{
		private Librarian librarian;
		private GroupLayout layout;
		private JScrollPane scroll;
		private JButton btn;
		private JButton retun_by_id;
		private JCheckBox reader;
		private BarcodeReader b_reader;

		TransactionsPanel(String[] cols,Librarian librarian){
			super(cols);
			this.librarian = librarian;

			btn = new JButton("Return Book");
			retun_by_id = new JButton("Return Books by Member");
			reader = new JCheckBox("Enable barcode reader", true);
			b_reader = new BarcodeReader();


			btn.setEnabled(false);

			reader.addItemListener(this);
			b_reader.addBarcodeListener(this);

			addComponentListener(new ComponentAdapter()
			{
				public void componentShown (ComponentEvent e)
				{
					if(reader.isSelected())
						b_reader.addBarcodeListener(TransactionsPanel.this);
				}

				public void componentHidden (ComponentEvent e)
				{
					if(reader.isSelected())
						b_reader.removeBarcodeListener(TransactionsPanel.this);
				}
			});

			btn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					int selectedRow = table.getSelectedRow();
				
					Transaction result = librarian.get_transaction((int)table.getValueAt(table.getSelectedRow(), 0));
					int fine = Transaction.calculate_fine(result.get_borrowed_date());
					String msg;
					if(fine<=0){
						msg = "Do you want to return the book?";
					}
					else{
						msg = "The fine amout is "+fine+" x Rs. "+Settings.price+" = Rs. "+fine*Settings.price+".\nDo you want to return the book?";
					}
					if(JOptionPane.showConfirmDialog(null,msg, "Confirm Dialog", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
						librarian.return_book(result.get_id());
						update_table();
					}
				}
			});

			retun_by_id.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					String id = JOptionPane.showInputDialog(TransactionsPanel.this,"Enter member id");
					create_dialog(id);
				}
			});

			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.getSelectionModel().addListSelectionListener(this);
			layout = new GroupLayout(this);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			setName("transactions");
			setLayout(layout);
			
			layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(input)
				.addComponent(table_sp)
				.addGroup(layout.createSequentialGroup()
					.addComponent(reader)
					.addComponent(btn)
					.addComponent(retun_by_id))
			);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(input)
				.addComponent(table_sp)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(reader)
					.addComponent(btn)
					.addComponent(retun_by_id))

			);
		}

		public void update_table(){
			update(librarian.transactions_to_array(librarian.get_transactions()));
		}

		public void valueChanged(ListSelectionEvent le){
			if(table.getSelectedRow()!=-1){
				btn.setEnabled(true);
			}
			else{
				btn.setEnabled(false);	
			}
		}

		@Override
		public void itemStateChanged(ItemEvent e){
			if(!reader.isSelected()){
				b_reader.removeBarcodeListener(this);
			}
			else{
				b_reader.addBarcodeListener(this);	
			}
		}
		@Override
		public void onBarcodeRead(String barcode){
			if(barcode.length()==8){
				char x = UPCEANLogicImpl.calcChecksum(barcode.substring(0, 7));
				if(barcode.endsWith(String.valueOf(x))){
					create_dialog(barcode.substring(0, 7));
				}
				else{
					JOptionPane.showMessageDialog(this, "Invalid check digit.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				}
			}
			else{
				JOptionPane.showMessageDialog(this, "Invalid barcode length.", "Bad Input", JOptionPane.ERROR_MESSAGE);
			}
		}

		public void create_dialog(String id){
			if(id!=null){
				try{
					ResultSet rs = DBManager.stmt.executeQuery("SELECT name FROM members WHERE id="+id);
					if(rs.next()){
						String name = rs.getString("name");
						ReturnByMemberPanel panel = new ReturnByMemberPanel(Integer.parseInt(id));
						int total_fine = panel.calculate_fine();
						JButton return_btn = new JButton("Return all books");
						JDialog dialog = new JOptionPane(new Object[]{panel,"Total Fine : "+total_fine+" x Rs. "+Settings.price+" = Rs. "+total_fine*Settings.price},JOptionPane.PLAIN_MESSAGE,JOptionPane.DEFAULT_OPTION,null,new Object[]{return_btn}).createDialog("Books borrowed by "+name);
						return_btn.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent ae){
								if(JOptionPane.showConfirmDialog(null,"The total fine amout is Rs. "+total_fine*Settings.price+".\nDo you want to return all books?", "Confirm Dialog", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
								{
									panel.return_all_books();
									update_table();
									dialog.dispose();
								}
							}
						});
						dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);
					}
					else{
						System.out.println("ERROR: Member not found in the database.");
						JOptionPane.showMessageDialog(TransactionsPanel.this, "Member not found in the database.", "Bad Input", JOptionPane.ERROR_MESSAGE);
					}
				}
				catch(NumberFormatException e){
					System.out.println("ERROR: Member id provided is not valid.");
					JOptionPane.showMessageDialog(TransactionsPanel.this, "Please enter a valid member id.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				}
				catch(SQLException e){
					System.out.println(e.toString());	
				} 
			}
		}
	}