package slm;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class ManageMembersPanel extends JPanel implements ListSelectionListener,ActionListener{
	private JButton add_member_btn;
	private JButton remove_member_by_id_btn;
	private JButton remove_btn;
	private JButton import_members_btn;
	private JFileChooser chooser;
	private JTable table;
	private JScrollPane table_sp;
	private GroupLayout layout;
	private Librarian librarian;
	private Object[][] data;

	ManageMembersPanel(Librarian librarian){
		setName("manage_memberships");

		this.librarian = librarian;

		add_member_btn = new JButton("Add Member");
		remove_member_by_id_btn = new JButton("Remove Member By Id");
		remove_btn = new JButton("Remove");
		import_members_btn = new JButton("Import Members");

		chooser = new JFileChooser();
		table = new JTable();
		table_sp = new JScrollPane(table);

		add(table_sp);
		add(add_member_btn);
		add(import_members_btn);
		add(remove_member_by_id_btn);

		add_member_btn.addActionListener(this);
		remove_member_by_id_btn.addActionListener(this);
		import_members_btn.addActionListener(this);
		remove_btn.addActionListener(this);
		table.getSelectionModel().addListSelectionListener(this);
		remove_btn.setEnabled(false);	

		layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
		layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(table_sp)
			.addGroup(layout.createSequentialGroup()
				.addComponent(add_member_btn)
				.addComponent(import_members_btn)
				.addComponent(remove_member_by_id_btn)
				.addComponent(remove_btn))
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
			.addComponent(table_sp)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(add_member_btn)
				.addComponent(import_members_btn)
				.addComponent(remove_member_by_id_btn)
				.addComponent(remove_btn))
		);
	}
	public void update_table(){
		librarian.update_table(table, new String[]{"ID","NAME","EMAIL","SEMESTER"}, librarian.members_to_array(librarian.get_members()));
	}
	public void valueChanged(ListSelectionEvent le){
		if(table.getSelectedRow()!=-1){
			remove_btn.setEnabled(true);
		}
		else{
			remove_btn.setEnabled(false);	
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
			Object[] ob = {name_lb,name,email_lb,email,sem_lb,sem};
			int result = JOptionPane.showConfirmDialog(this, ob,"Add Member Form",JOptionPane.OK_CANCEL_OPTION);

			if(result == JOptionPane.OK_OPTION){
				if(name.getText().equals("") || email.getText().equals("") || sem.getText().equals("")){
				System.out.println("ERROR: Some fields are empty in add member form.");
				JOptionPane.showMessageDialog(this, "Form is not complete.", "Bad Input", JOptionPane.ERROR_MESSAGE);
			}
			else{
				try{
					Member myMember = new Member(0,name.getText(),email.getText(),Integer.parseInt(sem.getText()));
					librarian.add_member(myMember);
					update_table();
				}
				catch(NumberFormatException e){
					System.out.println("ERROR: Semester provided is not valid.");
					JOptionPane.showMessageDialog(this, "Please enter a valid semester.", "Bad Input", JOptionPane.ERROR_MESSAGE);
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
				System.out.println(selectedRows.length + " book(s) removed from database.");
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
					JOptionPane.showMessageDialog(this, "Some errors occured while import the CSV file.", "Bad Input File", JOptionPane.ERROR_MESSAGE);
				}
				update_table();
			}
		}
	}
}