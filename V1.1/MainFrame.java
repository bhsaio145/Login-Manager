import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private String dataPath = "data.txt";

	//Jframe formatting created with WindowBuilder
	public MainFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 803, 497);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 767, 346);
		contentPane.add(scrollPane);
		
		//Contents of the Table	
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Username", "Password", "Note"
			}
		));
		table.getTableHeader().setReorderingAllowed(false);
		
		DefaultTableModel tblModel = (DefaultTableModel) table.getModel();
		
		//Contents of the "Add" Button
		JButton addButton = new JButton("Add Password");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//creates 3 input prompts asking for information to be input into each of the 3 columns
				String inputUN = JOptionPane.showInputDialog("Enter Username:");
				String inputPW = JOptionPane.showInputDialog("Enter Password:");
				String inputNote = JOptionPane.showInputDialog("Enter Notes:");
				Object[] data = {inputUN, inputPW, inputNote};
				tblModel.addRow(data);
			}
		});
		addButton.setBounds(21, 389, 130, 43);
		contentPane.add(addButton);
		
		//Contents of the "Remove" Button
		JButton removeButton = new JButton("Remove Password");
		removeButton.addActionListener(new ActionListener() {
			//on action listen of the button press. Gets int of selected row and removes the row from the table model.
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() != -1) {
					tblModel.removeRow(table.getSelectedRow());
				}
			}
		});
		removeButton.setBounds(175, 389, 130, 43);
		contentPane.add(removeButton);
		
		//Contents of the "Export" Button
		JButton exportButton = new JButton("Export Data");
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportData(tblModel);
			}
		});
		exportButton.setBounds(633, 389, 130, 43);
		contentPane.add(exportButton);
		
		//Importing Data from a saved data.txt
		importData(tblModel);
		
		//Action listener for the closing of JFrame to perform an automated export
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exportData(tblModel);
				System.exit(0);
			}
		});
	}
	
	//import Function that scans a text file and stores all information scanned into the parameter table model
	private void importData(DefaultTableModel model) {
		//setting table row count to 0, to delete any previous displaying information in the table
		model.setRowCount(0);
		FileReader fReader;
		try {
			//opening Scanner and Buffer to read form the text file
			fReader = new FileReader(dataPath);
			BufferedReader bfReader = new BufferedReader(fReader);
			Scanner filedata = new Scanner(bfReader);
			Scanner scan;
			String nxtLine, inDomain, inPass, inNote;
			//while the text file has another line of text. Splits the line into entries based on delimiter and insert into the table.
			while(filedata.hasNext()) {
				nxtLine = filedata.nextLine();
				scan = new Scanner(nxtLine);
				scan.useDelimiter(" , ");
				inDomain = scan.next();
				inPass = scan.next();
				inNote = scan.next();
				Object[] data = {inDomain,inPass,inNote};
				model.addRow(data);
			}
			fReader.close();
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Data file not found");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	//import Function that goes through the contents of the parameter table model and writes them into a text document with delimiters
	private void exportData(DefaultTableModel model) {
		//using Writers to write information from the table to the output text file
		FileWriter outputWriter;
		try {
			outputWriter = new FileWriter(dataPath);
			BufferedWriter outFile = new BufferedWriter(outputWriter);
			int rows = model.getRowCount();
			//nested for loop to read each element in the table in order
			for(int i = 0 ; i < rows ; i++) {
				for(int j = 0 ; j < 3 ; j++) {
					outFile.write(String.valueOf(model.getValueAt(i, j)));
					//creating delimiters and nextlines to allow for easier reading of the information on "import"
					if(j != 2) {
						outFile.write(" , ");
					}
					else {
						outFile.write("\n");
					}
				}
			}
			outFile.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
