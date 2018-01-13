package slm;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;

public class TransactionsPanel extends TablePanel implements ListSelectionListener,Updatable{
		private Librarian librarian;
		private GroupLayout layout;
		private JScrollPane scroll;
		private JButton btn;
		private JButton retun_by_id;

		TransactionsPanel(String[] cols,Librarian librarian){
			super(cols);
			this.librarian = librarian;

			btn = new JButton("Return Book");
			retun_by_id = new JButton("Return Books by Member");

			btn.setEnabled(false);

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
						msg = "The fine amout is "+fine+" x "+Settings.price+" = Rs. "+fine*Settings.price+".\nDo you want to return the book?";
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

					if(id!=null){
						try{
							ReturnByMemberPanel panel = new ReturnByMemberPanel(Integer.parseInt(id));
							int total_fine = panel.calculate_fine();
							JButton return_btn = new JButton("Return all books");
							JDialog dialog = new JOptionPane(new Object[]{panel,"Total Fine : "+total_fine},JOptionPane.PLAIN_MESSAGE,JOptionPane.DEFAULT_OPTION,null,new Object[]{return_btn}).createDialog("Books borrowed by member "+id);
							return_btn.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent ae){
									if(JOptionPane.showConfirmDialog(null,"The total fine amout is Rs. "+total_fine+".\nDo you want to return all books?", "Confirm Dialog", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
									{
										panel.return_all_books();
										dialog.dispose();
									}
								}
							});
							dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
							dialog.setVisible(true);

						}
						catch(NumberFormatException e){
							System.out.println("ERROR: Member id provided is not valid.");
							JOptionPane.showMessageDialog(TransactionsPanel.this, "Please enter a valid member id.", "Bad Input", JOptionPane.ERROR_MESSAGE);
						} 
					}
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
					.addComponent(btn)
					.addComponent(retun_by_id))
			);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(input)
				.addComponent(table_sp)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
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
	}