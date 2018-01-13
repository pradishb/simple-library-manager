package slm;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.sql.SQLException;

public class ManageMembersPanel extends TablePanel implements ListSelectionListener,ActionListener,Updatable{
	private JButton add_member_btn;
	private JButton remove_member_by_id_btn;
	private JButton remove_btn;
	private JButton update_btn;
	private JButton import_members_btn;
	private JButton end_session_btn;
	private JFileChooser chooser;
	private GroupLayout layout;
	private Librarian librarian;
	private Object[][] data;

	ManageMembersPanel(String[] cols,Librarian librarian){
		super(cols);
		setName("manage_memberships");

		this.librarian = librarian;

		add_member_btn = new JButton("Add Member");
		remove_member_by_id_btn = new JButton("Remove Member By Id");
		remove_btn = new JButton("Remove");
		update_btn = new JButton("Update Member");
		import_members_btn = new JButton("Import Members");
		end_session_btn = new JButton("End Session");

		chooser = new JFileChooser();

		add_member_btn.addActionListener(this);
		remove_member_by_id_btn.addActionListener(this);
		import_members_btn.addActionListener(this);
		remove_btn.addActionListener(this);
		update_btn.addActionListener(this);
		end_session_btn.addActionListener(this);
		table.getSelectionModel().addListSelectionListener(this);
		remove_btn.setEnabled(false);	
		update_btn.setEnabled(false);	

		layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
		layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(input)
			.addComponent(table_sp)
			.addGroup(layout.createSequentialGroup()
				.addComponent(add_member_btn)
				.addComponent(import_members_btn)
				.addComponent(update_btn)
				.addComponent(remove_member_by_id_btn)
				.addComponent(remove_btn)
				.addComponent(end_session_btn))
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
			.addComponent(input)
			.addComponent(table_sp)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(add_member_btn)
				.addComponent(import_members_btn)
				.addComponent(update_btn)
				.addComponent(remove_member_by_id_btn)
				.addComponent(remove_btn)
				.addComponent(end_session_btn))
		);
	}
	public void update_table(){
		update(librarian.members_to_array(librarian.get_members()));
	}
	public void valueChanged(ListSelectionEvent le){
		if(table.getSelectedRow()!=-1){
			remove_btn.setEnabled(true);
		}
		else{
			remove_btn.setEnabled(false);	
		}

		if(table.getSelectedRowCount()==1){
			update_btn.setEnabled(true);
		}
		else{
			update_btn.setEnabled(false);	
		}
	}
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==add_member_btn){
			JLabel name_lb = new JLabel("Name:");
			JLabel email_lb = new JLabel("Email:");
			JLabel sem_lb = new JLabel("Semester:");
			JTextField name = new JTextField();
			JTextField email = new JTextField();
			JTextField sem = new JTextField();

			int result;
			do{
				name.setText("");
				email.setText("");
				sem.setText("");
				Object[] ob = {name_lb,name,email_lb,email,sem_lb,sem};
				Object[] options = {"Add","Add & Continue","Cancel"};
				result = JOptionPane.showOptionDialog(this,ob,"Add Member Form",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,null);

				if(result == 0 || result == 1){
					if(name.getText().equals("") || email.getText().equals("") || sem.getText().equals("")){
						System.out.println("ERROR: Some fields are empty in add member form.");
						JOptionPane.showMessageDialog(this, "Form is not complete.", "Bad Input", JOptionPane.ERROR_MESSAGE);
					}
					else{
						try{
							Member myMember = new Member(0,name.getText(),email.getText(),Integer.parseInt(sem.getText()));
							librarian.add_member(myMember);
							update_table();
							JOptionPane.showMessageDialog(this, "Member has been added.");
						}
						catch(NumberFormatException e){
							System.out.println("ERROR: Semester provided is not valid.");
							JOptionPane.showMessageDialog(this, "Please enter a valid semester.", "Bad Input", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
			while(result==1);
		}
		else if(ae.getSource()==update_btn){
			int id = (int)table.getValueAt(table.getSelectedRow(), 0);
			Member myMember = librarian.get_member(id);

			JLabel name_lb = new JLabel("Name:");
			JLabel email_lb = new JLabel("Email:");
			JLabel sem_lb = new JLabel("Semester:");
			JTextField name = new JTextField(myMember.get_name());
			JTextField email = new JTextField(myMember.get_email());
			JTextField sem = new JTextField(Integer.toString(myMember.get_semester()));
			Object[] ob = {name_lb,name,email_lb,email,sem_lb,sem};
			int result = JOptionPane.showConfirmDialog(this, ob,"Update Member Form",JOptionPane.OK_CANCEL_OPTION);

			if(result == JOptionPane.OK_OPTION){
				if(name.getText().equals("") || email.getText().equals("") || sem.getText().equals("")){
				JOptionPane.showMessageDialog(this, "Form is not complete.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				}
				else{
					try{
						myMember.update_data(myMember.get_id(),name.getText(),email.getText(),Integer.parseInt(sem.getText()));
						librarian.update_member(myMember);
						update_table();
					}
					catch(NumberFormatException e){
						JOptionPane.showMessageDialog(this, "No. of copies is not a number.", "Bad Input", JOptionPane.ERROR_MESSAGE);
					}
				}
			}

		}
		else if(ae.getSource()==remove_member_by_id_btn){
			String inputValue = JOptionPane.showInputDialog("Enter member id");
			if(inputValue!=null){
				try{
					librarian.remove_member(Integer.parseInt(inputValue));
					update_table();
				}
				catch(NumberFormatException e){
					System.out.println("ERROR: Member id provided is not valid.");
					JOptionPane.showMessageDialog(this, "Please enter a valid member id.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				} 
			}
		}
		else if(ae.getSource()==remove_btn){
			data = librarian.members_to_array(librarian.get_members());
			int[] selectedRows = table.getSelectedRows();
			int result = JOptionPane.showConfirmDialog(this, "Do you really want to remove "+selectedRows.length+" member(s)?","Confirm",JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION){
				for(int x:selectedRows){
					librarian.remove_member((int)data[x][0]);
				}
				update_table();
				System.out.println(selectedRows.length + " member(s) removed from database.");
			}
		}
		else if(ae.getSource()==end_session_btn){
			int result = JOptionPane.showConfirmDialog(this,"You are manipulating the semester value of all members. Do you want to continue?","End Session",JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_OPTION){
				try{
					DBManager.stmt.execute("UPDATE members SET semester=semester+1");
					update_table();
					JOptionPane.showMessageDialog(this, "The semester value of all members are incresed.");
				}
				catch(SQLException e){
					System.out.println(e.toString());	
				}
			}
		}
		else{
			chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				try{
					librarian.import_members(chooser.getSelectedFile());
				}catch(InvalidCsvFormatException e){
					System.out.println(e.getMessage());
					JOptionPane.showMessageDialog(this, "The following columns were not found in the CSV file :\n"+String.join(", ", e.get_missing_cols()), "Bad Input File", JOptionPane.ERROR_MESSAGE);
				}
				update_table();
			}
		}
	}
}