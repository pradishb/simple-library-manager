package slm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SettingsPanel extends JPanel{
		private GroupLayout layout;
		private JLabel maxlb;
		private JLabel duelb;
		private JLabel pricelb;
		private JLabel passlb;
		private JSpinner max;
		private JSpinner due;
		private JSpinner price;
		private JTextField pass;
		private JButton apply;


		SettingsPanel(){
			//initialize components
			maxlb = new JLabel("Maximum Threshold (Per Member)");
			duelb = new JLabel("Overdue Duration (Days)");
			pricelb = new JLabel("Fine per day (Rs.)");
			passlb = new JLabel("New Password");
			max = new JSpinner(new SpinnerNumberModel(5,1,100,1));
			due = new JSpinner(new SpinnerNumberModel(30,1,10000,1));
			price = new JSpinner(new SpinnerNumberModel(1,0,1000,1));
			pass = new JPasswordField("");
			apply = new JButton("Apply");

			layout = new GroupLayout(this);
			setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);

			layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(maxlb)
					.addComponent(duelb)
					.addComponent(pricelb)
					.addComponent(passlb))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(max, GroupLayout.PREFERRED_SIZE, 70,GroupLayout.PREFERRED_SIZE)
					.addComponent(due, GroupLayout.PREFERRED_SIZE, 70,GroupLayout.PREFERRED_SIZE)
					.addComponent(price, GroupLayout.PREFERRED_SIZE, 70,GroupLayout.PREFERRED_SIZE)
					.addComponent(pass)
					.addComponent(apply))
				);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(maxlb)
					.addComponent(max))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(duelb)
					.addComponent(due))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(pricelb)
					.addComponent(price))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(passlb)
					.addComponent(pass))
				.addComponent(apply)
				);
		}
	}
