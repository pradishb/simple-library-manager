package slm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RemoveMemberDialog extends JDialog implements ActionListener{
		private JLabel member_id_label;
		private JTextField member_id_tf;
		private JButton btn;
		private GroupLayout layout;
		private Librarian librarian;
		private JTable table;

		RemoveMemberDialog(Frame owner, Librarian librarian, JTable table){
			super(owner,"Remove Member Form",true);

			this.librarian = librarian;
			this.table = table;

			member_id_tf = new JTextField();
			btn = new JButton("Remove Member");
			member_id_label = new JLabel("Member Id:");
			layout = new GroupLayout(getContentPane());
			getContentPane().setLayout(layout);

			btn.addActionListener(this);

			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(member_id_label))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(member_id_tf)
					.addComponent(btn))
				);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(member_id_label)
					.addComponent(member_id_tf))
				.addComponent(btn)
				);

			
			setSize(400,110);
			setLocationRelativeTo(null);
			setResizable(false);
			add(member_id_label);
			add(member_id_tf);
			add(btn);
		}
		public void actionPerformed(ActionEvent ae){
			try{
				librarian.remove_member(Integer.parseInt(member_id_tf.getText()));
				librarian.update_table(table, new String[]{"ID","TITLE","AUTHER","PUBLICATION"}, librarian.members_to_array(librarian.get_members()));
				setVisible(false);
				dispose();
			}
			catch(NumberFormatException e){
				System.out.println("ERROR: Member id provided is not valid.");
				JOptionPane.showMessageDialog(this, "Please enter a valid member id.", "Bad Input", JOptionPane.ERROR_MESSAGE);
			}		

		}
	}