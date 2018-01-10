package slm;

import javax.swing.*;
import java.awt.event.*;

public class MyMenuBar extends JMenuBar{
	JMenu menu, submenu;
	JMenuItem menuItem;
	public MyMenuBar(){

		menu = new JMenu("About");
		menuItem = new JMenuItem("Credits");

		menu.add(menuItem);
		add(menu);

		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a){
				JOptionPane.showMessageDialog((JFrame) SwingUtilities.getWindowAncestor(MyMenuBar.this),"Created by:\nPradish Bijukchhe\nSrijan Tamarakar\nBiraj Rajbhandari\nBatch : BESE2015\nSupervisor : Er. Mahesh Shakya","Credits",JOptionPane.PLAIN_MESSAGE);
			}
		});
	}

}