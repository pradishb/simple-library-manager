package slm;

import javax.swing.*;
import java.awt.event.*;

public class MyMenuBar extends JMenuBar implements ItemListener{
	JMenu about, file;
	JMenuItem export,credits;
	JFileChooser chooser;

	JCheckBox books;
	JCheckBox members;

	JButton btn;
	public MyMenuBar(){
		file = new JMenu("File");
		export = new JMenuItem("Export CSV");
		about = new JMenu("About");
		credits = new JMenuItem("Credits");
		btn = new JButton("Export");

		books = new JCheckBox("Books", true);
		members = new JCheckBox("Members", true);
		chooser = new JFileChooser();
		chooser.setDialogTitle("Select Export Folder");
    	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    	books.addItemListener(this);
    	members.addItemListener(this);

		file.add(export);
		about.add(credits);
		add(file);
		add(about);

		export.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a){
				Object[] ob = {books,members};
				Object[] options = {btn,"Cancel"};
				int result = JOptionPane.showOptionDialog((JFrame) SwingUtilities.getWindowAncestor(MyMenuBar.this),ob,"Export CSV",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,null);

				if(result==0){
					int returnVal = chooser.showOpenDialog((JFrame) SwingUtilities.getWindowAncestor(MyMenuBar.this));
					if(returnVal == JFileChooser.APPROVE_OPTION){
						if(books.isSelected())
							CSVExporter.export_books(chooser.getSelectedFile().toString());
						if(members.isSelected())
							CSVExporter.export_members(chooser.getSelectedFile().toString());
					}
				}
			}
		});

		credits.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a){
				JOptionPane.showMessageDialog((JFrame) SwingUtilities.getWindowAncestor(MyMenuBar.this),"Created by:\nPradish Bijukchhe\nSrijan Tamarakar\nBiraj Rajbhandari\nBatch : BESE2015\nSupervisor : Er. Mahesh Shakya","Credits",JOptionPane.PLAIN_MESSAGE);
			}
		});
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
       if(!books.isSelected() && !members.isSelected()){
       		btn.setEnabled(false);
       }
       else{
       		btn.setEnabled(true);
       }
    }

}