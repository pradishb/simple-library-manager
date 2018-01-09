package slm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


public class SettingsPanel extends JPanel implements ActionListener{
	private GroupLayout layout;
	private JLabel thresholdlb;
	private JLabel duelb;
	private JLabel pricelb;
	private JLabel passlb;
	private JSpinner threshold;
	private JSpinner due;
	private JSpinner price;
	private JTextField pass;
	private JButton apply;
	private DBManager db_manager;
	private SecurityManager security_manager;

	SettingsPanel(DBManager db_manager,SecurityManager security_manager){
		this.db_manager = db_manager;
		this.security_manager = security_manager;
		setName("settings");

		//initialize components
		thresholdlb = new JLabel("Maximum Threshold (Per Member)");
		duelb = new JLabel("Overdue Duration (Days)");
		pricelb = new JLabel("Fine per day (Rs.)");
		passlb = new JLabel("New Password");
		threshold = new JSpinner(new SpinnerNumberModel(5,1,100,1));
		due = new JSpinner(new SpinnerNumberModel(30,1,10000,1));
		price = new JSpinner(new SpinnerNumberModel(1,0,1000,1));
		pass = new JPasswordField("");
		apply = new JButton("Apply");

		apply.addActionListener(this);

		update_panel();

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
			layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(thresholdlb)
				.addComponent(duelb)
				.addComponent(pricelb)
				.addComponent(passlb))
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(threshold, GroupLayout.PREFERRED_SIZE, 70,GroupLayout.PREFERRED_SIZE)
				.addComponent(due, GroupLayout.PREFERRED_SIZE, 70,GroupLayout.PREFERRED_SIZE)
				.addComponent(price, GroupLayout.PREFERRED_SIZE, 70,GroupLayout.PREFERRED_SIZE)
				.addComponent(pass, GroupLayout.PREFERRED_SIZE, 200,GroupLayout.PREFERRED_SIZE)
				.addComponent(apply))
			);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(thresholdlb)
				.addComponent(threshold))
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

	public void actionPerformed(ActionEvent ae){
		apply_settings();
		Settings.update(db_manager);
	}

	public void apply_settings(){
		if(pass.getText().equals("")){
			try{
				db_manager.stmt.execute("UPDATE settings SET id=1,threshold="+threshold.getValue().toString()+",overdue_duration="+due.getValue().toString()+",fine_per_day="+price.getValue().toString());
				JOptionPane.showMessageDialog(this, "Settings Applied", "Sucess", JOptionPane.INFORMATION_MESSAGE);
			}
			catch(Exception se){
				System.out.println("ERROR: Error while updating settings to database.");
				System.out.println("Details:");
				System.out.println(se.toString());
			}
		}
		else{
			try{
				db_manager.stmt.execute("UPDATE settings SET id=1,password=\""+security_manager.sha1(pass.getText())+"\",threshold="+threshold.getValue().toString()+",overdue_duration="+due.getValue().toString()+",fine_per_day="+price.getValue().toString());
				JOptionPane.showMessageDialog(this, "Settings Applied", "Sucess", JOptionPane.INFORMATION_MESSAGE);
			}
			catch(Exception se){
				System.out.println("ERROR: Error while updating settings to database.");
				System.out.println("Details:");
				System.out.println(se.toString());
			}
		}
	}

	public void update_panel(){
		Settings.update(db_manager);
		threshold.setValue(Settings.threshold);
		due.setValue(Settings.due);
		price.setValue(Settings.price);
		pass.setText("");
	}
}
