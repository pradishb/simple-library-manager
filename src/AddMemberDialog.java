package slm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddMemberDialog extends JDialog implements ActionListener{
		private JLabel name_lb;
		private JLabel email_lb;
		private JLabel sem_lb;
		private JTextField name;
		private JTextField email;
		private JTextField sem;
		private JButton btn;
		private GroupLayout layout;
		private Librarian librarian;
		private JTable table;

		AddMemberDialog(Frame owner, Librarian librarian, JTable table){
			super(owner,"Add Member Form",true);

			this.librarian = librarian;
			this.table = table;

			name_lb = new JLabel("Name:");
			email_lb = new JLabel("Email:");
			sem_lb = new JLabel("Semester:");
			name = new JTextField();
			email = new JTextField();
			sem = new JTextField();
			btn = new JButton("Add Member");

			layout = new GroupLayout(getContentPane());
			getContentPane().setLayout(layout);

			btn.addActionListener(this);

			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(name_lb)
					.addComponent(email_lb)
					.addComponent(sem_lb))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(name)
					.addComponent(email)
					.addComponent(sem)
					.addComponent(btn))
				);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(name_lb)
					.addComponent(name))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(email_lb)
					.addComponent(email))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(sem_lb)
					.addComponent(sem))
				.addComponent(btn)
				);

			
			setSize(400,165);
			setResizable(false);
			add(name_lb);
			add(email_lb);
			add(sem_lb);
			add(name);
			add(email);
			add(sem);
			add(btn);
		}
		public void actionPerformed(ActionEvent ae){
			if(name.getText().equals("") || email.getText().equals("") || sem.getText().equals("")){
				System.out.println("ERROR: Some fields are empty in add member form.");
				JOptionPane.showMessageDialog(this, "Form is not complete.", "Bad Input", JOptionPane.ERROR_MESSAGE);
			}
			else{
				try{
					Member myMember = new Member(0,name.getText(),email.getText(),Integer.parseInt(sem.getText()),0);
					librarian.add_member(myMember);
					librarian.update_table(table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, librarian.members_to_array(librarian.get_members()));
					setVisible(false);
					dispose();
				}
				catch(NumberFormatException e){
					System.out.println("ERROR: Semester provided is not valid.");
					JOptionPane.showMessageDialog(this, "Please enter a valid member id.", "Bad Input", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}