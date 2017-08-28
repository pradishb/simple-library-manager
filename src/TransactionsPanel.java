package slm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TransactionsPanel extends JPanel implements ActionListener{
		private GroupLayout layout;
		private JScrollPane table_sp;
		public JTable table;

		TransactionsPanel(){
			table = new JTable();		
			table.setEnabled(false);
			table_sp = new JScrollPane(table);

			layout = new GroupLayout(this);
			setName("transactions");
			setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			
			layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addComponent(table_sp)
			);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(table_sp)
			);
		}
		public void actionPerformed(ActionEvent ae){

		}
	}