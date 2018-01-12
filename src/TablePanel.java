package slm;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;


public class TablePanel extends JPanel{
	protected JTextField input;
	protected JTable table;
	protected String[] cols;
	protected DefaultTableModel tableModel;
	protected TableRowSorter<TableModel> rowSorter;
	protected JScrollPane table_sp;

	public TablePanel(String cols[]){
		this.cols =  cols;
		input = new JTextField();
		table = new JTable();
		rowSorter = new TableRowSorter<>();
		table_sp = new JScrollPane(table);
		tableModel = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		input.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = input.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = input.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });

		table.setRowSorter(rowSorter);
	}

	public void reset(){
		table.requestFocus();
	}

	public void update(Object[][] data){
		tableModel.setDataVector(data, cols);
		table.setModel(tableModel);
		table.getColumn("ID").setMaxWidth(30);
		rowSorter.setModel(table.getModel());
	}
}