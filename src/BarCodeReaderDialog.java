package slm;

import javax.swing.*;

public class BarCodeReaderDialog extends JDialog{
	public BarCodeReaderDialog(JFrame parent){
		super(parent,"Bar Code Reader",true);
		// System.out.println(parent);

		setSize(400,400);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}