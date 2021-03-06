import java.awt.Color;
import java.awt.Dimension;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class client extends JFrame{
	private JTable girthTable;
	private JTable fitnessPlanTable;
	private JTable skinTable;
	private JTable weekTable;
	private int clientID;
	public client(int client) {
	
	clientID = client;
		setupTables();
		updateTables(clientID);
		generateFitnessPlans();
		
	System.out.println(clientID);
	// might need a re-work to better lay it out 
	setTitle("MetabolixLite: Client Manager");
	getContentPane().setBackground(new Color(255, 235, 205));
	getContentPane().setLayout(null);
	
	JScrollPane mealPlan = new JScrollPane();
	mealPlan.setBounds(12, 377, 605, 71);
	getContentPane().add(mealPlan);
	
	fitnessPlanTable = new JTable();
	fitnessPlanTable.setModel(fitnessPlanTableModel);
	fitnessPlanTable.getColumn("Date").setMinWidth(75);
	fitnessPlanTable.getColumn("Date").setMaxWidth(75);
	mealPlan.setViewportView(fitnessPlanTable);
	
	JLabel weekDataLabel = new JLabel("Progress Data:");
	weekDataLabel.setBounds(12, 114, 116, 14);
	getContentPane().add(weekDataLabel);
	
	JLabel lblAssessmentTst = new JLabel("Assessment Tests:");
	lblAssessmentTst.setBounds(12, 223, 160, 14);
	getContentPane().add(lblAssessmentTst);
	
	JLabel lblMealPlan = new JLabel("Fitness Plans:");
	lblMealPlan.setBounds(12, 351, 111, 14);
	getContentPane().add(lblMealPlan);
	
	JButton btnNewButton = new JButton("New Week");
	btnNewButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			addWeek aw = new addWeek(clientID);
			aw.setBounds(400,0,450,600);
			aw.setVisible(true);
		}
	});
	btnNewButton.setBounds(58, 34, 117, 23);
	getContentPane().add(btnNewButton);

	JTabbedPane assessmentTestTabPane = new JTabbedPane(JTabbedPane.TOP);
	assessmentTestTabPane.setBounds(12, 248, 605, 92);
	getContentPane().add(assessmentTestTabPane);
	
	JScrollPane fold = new JScrollPane();
	assessmentTestTabPane.addTab("Skin Fold", null, fold, null);
	
	skinTable = new JTable();
	skinTable.setModel(skinTableModel);
	skinTable.getColumn("Date").setMinWidth(75);
	skinTable.getColumn("Date").setMaxWidth(75);
	fold.setViewportView(skinTable);
	
	JScrollPane girth = new JScrollPane();
	assessmentTestTabPane.addTab("Girth", null, girth, null);
	
	girthTable = new JTable();
	girthTable.setModel(girthTableModel);
	girthTable.getColumn("Date").setMinWidth(75);
	girthTable.getColumn("Date").setMaxWidth(75);
	girth.setViewportView(girthTable);
	
	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBounds(12, 140, 605, 71);
	getContentPane().add(scrollPane);
	
	weekTable = new JTable();
	weekTable.setModel(weekTableModel);
	weekTable.getColumn("Date").setMinWidth(75);
	weekTable.getColumn("Date").setMaxWidth(75);
	scrollPane.setViewportView(weekTable);
	
	JButton sendFormBtn = new JButton("Send Forms");
	sendFormBtn.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			sendForm sf = new sendForm(clientID);
			sf.setVisible(true);
			
		}
	});
	sendFormBtn.setBounds(224, 33, 117, 25);
	getContentPane().add(sendFormBtn);
	
	
	JButton deleteClient = new JButton("Delete Client");
	deleteClient.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			weekTable.removeAll();
			deleteClient(clientID);
			dispose();
			
		}
	});
	deleteClient.setBounds(388, 33, 117, 25);
	getContentPane().add(deleteClient);
	
	
	setPreferredSize(new Dimension(650,650));
	setLocationRelativeTo(null);
	addWindowFocusListener(new WindowAdapter() {
	    public void windowGainedFocus(WindowEvent e) {
	    	//Update Client Table on refocus in case of added clients
	    	updateTables(clientID);
	    	generateFitnessPlans();
	    	validate();
	    	repaint();
	    }
	});

	pack();

	}
	
	
	protected void deleteClient(int client_ID) {
		// TODO Auto-generated method stub
		if(JOptionPane.showConfirmDialog(null, "Continue? If you delete this client you will no longer be able "
				+ "to select this client.\nAll associated information with this client will be deleted."
				+ "\n Click 'YES' to CONFIRM DELETION this is permanent!","Warning",JOptionPane.YES_OPTION)==JOptionPane.YES_OPTION)
		{
			sqlMethods sql = new sqlMethods();
			boolean success = sql.deleteClientData(client_ID);
			if(success)
			{
				JOptionPane.showMessageDialog(null, "Client successfully deleted.");
			}
			else
			{
				System.out.println("ERROR DELETING CLIENT - DEBUG deleteClient() - client.java");
			}   
		}
	}


	private void generateFitnessPlans()
	{
		int weeks = weekTableModel.getRowCount();
		System.out.println(weeks + " weeks");
		for(int i = 0; i < weeks; i++)
		{
			fitness_weight = (int) weekTableModel.getValueAt(i, 4);
			fitness_height = (int) weekTableModel.getValueAt(i, 3);
			int age = (int) weekTableModel.getValueAt(i, 2);
			double sf = ((double) skinTableModel.getValueAt(i, 8)) + ((double) skinTableModel.getValueAt(i, 5)) + (double) skinTableModel.getValueAt(i, 7) ;
			double density = (1.10938 - (0.0008267*sf) + 0.0000016*(sf*sf) - (0.0002574*age));
			bf = ((4.95/density) - 4.5)*100;
			double fw = fitness_weight * (bf/100);
			lbw = fitness_weight - fw; // Lean body weight
			rmr = (66+(13.75*(fitness_weight*0.45359237))+(5*(fitness_height*2.54))-(6.8*age));
			exmr = rmr*1.375;
			protein = ((((rmr+exmr)/2)/4)*0.3);
			carbs = ((rmr*0.4)/9);
			fats = ((rmr*0.3)/9);
			int calories = (int) (rmr+200);
			/*"Date", "Weight", "LBW", "FW", "RMR", "EX MR",
			"Protein (g)", "Carbs (g)", "Fats (g)"*/
			fitnessPlanTableModel.addRow(new Object[] { weekTableModel.getValueAt(i, 0), fitness_weight, lbw, fw, rmr, exmr, protein, carbs, fats, calories}); 
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void updateTables(final int ClientID)
	{
		weekTableModel.setRowCount(0);
		skinTableModel.setRowCount(0);
		girthTableModel.setRowCount(0);
		fitnessPlanTableModel.setRowCount(0);

		Thread runner = new Thread() 
		{
			public void run()
			{
				try {
					sqlMethods sql = new sqlMethods();
					ResultSet queryResult1;
					ResultSet queryResult2;
					queryResult1 = sql.getClientWeekData(ClientID);
					queryResult2 = sql.getClientPastData(ClientID);
					Date date = null;
					int wt = 0;
					queryResult1.next();
					while(queryResult2.next())
					{
							String name = queryResult1.getString(2);
							int age = queryResult1.getInt(3);
							int ht = queryResult1.getInt(5);
							date = queryResult2.getDate(2);	
							wt = queryResult2.getInt(3);
							
							
							
							
							
							weekTableModel.addRow(new Object[] { date, name, age, ht, wt });

							System.out.println("added week");
					}
					//Client(Client_ID, Client_Fname, Client_Lname, Client_Age, Date_Entered (date), Height (int), Weight(int), Trainer_Uname)

					queryResult1.close();
					queryResult2.close();
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}






		};

		
		Thread runner2 = new Thread() 
		{
			public void run()
			{
				try {
					sqlMethods sql = new sqlMethods();
					ResultSet queryResult2;
					
					queryResult2 = sql.getClientSkinData(ClientID);
					
					while(queryResult2.next())
					{
						//Skinfold(S_Date, Arm, Waist, Calf, Hips, S_Thigh, Neck, S_Chest, S_Abdom, C_IDs)
							Date date = queryResult2.getDate(1);		
							arm = queryResult2.getFloat(2);
							waist = queryResult2.getFloat(3);
							calf = queryResult2.getFloat(4);
							hips = queryResult2.getFloat(5);
							skin_thigh = queryResult2.getFloat(6);
							neck = queryResult2.getFloat(7);
							skin_chest = queryResult2.getFloat(8);
							skin_abdom = queryResult2.getFloat(9);
							skinTableModel.addRow(new Object[] { date, arm, waist, calf, hips, skin_thigh, neck, skin_chest, skin_abdom });
					}
					
					queryResult2.close();
				//Skinfold(S_Date, Arm, Waist, Calf, Hips, S_Thigh, Neck, S_Chest, S_Abdom, C_IDs)
				//"Date", "Arm", "Waist", "Calf", "Hips", "Thigh", "Neck", "Chest", "Abdom"
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}






		};
		
		
		Thread runner3 = new Thread() 
		{
			public void run()
			{
				try {
					sqlMethods sql = new sqlMethods();
					ResultSet queryResult3;
					
					queryResult3 = sql.getClientGirthData(ClientID);
					
					while(queryResult3.next())
					{

						Date date = queryResult3.getDate(1);		
						midax = queryResult3.getFloat(2);
					    subscap = queryResult3.getFloat(3);
						tricep = queryResult3.getFloat(4);
						kidney = queryResult3.getFloat(5);
						supra = queryResult3.getFloat(6);
						girth_chest = queryResult3.getFloat(7);
						girth_thigh = queryResult3.getFloat(8);
						girth_abdom = queryResult3.getFloat(9);
						girthTableModel.addRow(new Object[] { date, midax, subscap, tricep, kidney, supra, girth_chest, girth_thigh, girth_abdom });
						System.out.println("added girth");
					}
					//Girth(G_date, MidAx, Subscap, Triceps, Kidney, Supra, G_Chest, G_Thigh, G_Abdom, C_IDg)
					//"Date", "Mid Ax", "Subscap", "Triceps", "Kidney", "Supra", "Chest", "Thigh", "Abdom"
					queryResult3.close();
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		};

		
		runner.run();
		runner2.run();
		runner3.run();





	}
	private void setupTables()
	{
		//Client(Client_ID, Client_Fname, Client_Lname, Client_Age, Date_Entered, Height, Weight, Trainer_Uname)
		fitnessPlanTableModel = new DefaultTableModel() {
			Class[] columnTypes = new Class[] {
					Object.class, Object.class, Object.class, Object.class, Object.class,
					Object.class, Object.class, Object.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
		};
		fitnessPlanTableModel.setColumnCount(10);
		fitnessPlanTableModel.setColumnIdentifiers(new String[] {
				"Date", "Weight", "LBW", "FW", "RMR", "EX MR",
				"Protein (g)", "Carbs (g)", "Fats (g)", "Calories"
		});
		/*
		 * 
		 *  weekly data table
		 */
		weekTableModel = new DefaultTableModel() {
			Class[] columnTypes = new Class[] {
					Object.class, Object.class, Object.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		};
		weekTableModel.setColumnCount(5);
		weekTableModel.setColumnIdentifiers(new String[] {
				"Date", "Name", "Age", "Height", "Weight"
		});
		
		/*
		 * 
		 *  girth assessment data table
		 *  Girth(G_date, MidAx, Subscap, Triceps, Kidney, Supra, G_Chest, G_Thigh, G_Abdom, C_IDg)
	 *	

		 */	
		
		girthTableModel = new DefaultTableModel() {
			Class[] columnTypes = new Class[] {
					Object.class, Object.class, Object.class, Object.class, Object.class,
					Object.class, Object.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		};
		girthTableModel.setColumnCount(9);
		girthTableModel.setColumnIdentifiers(new String[] {
				"Date", "Mid Ax", "Subscap", "Triceps", "Kidney", "Supra", "Chest", "Thigh", "Abdom"
		});
		
		/*
		 * 
		 *  skin fold assessment data table
		 *  
	 *	Skinfold(S_Date, Arm, Waist, Calf, Hips, S_Thigh, Neck, S_Chest, S_Abdom, C_IDs)

		 */	
	
		skinTableModel = new DefaultTableModel() {
			Class[] columnTypes = new Class[] {
					Object.class, Object.class, Object.class, Object.class, Object.class,
					Object.class, Object.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		};
		skinTableModel.setColumnCount(5);
		skinTableModel.setColumnIdentifiers(new String[] {
				"Date", "Arm", "Waist", "Calf", "Hips", "Thigh", "Neck", "Chest", "Abdom"
		});
	
		
	}
	private DefaultTableModel weekTableModel;
	private DefaultTableModel skinTableModel;
	private DefaultTableModel girthTableModel;
	private DefaultTableModel fitnessPlanTableModel;
	private int ClientID;
	private double midax;
	private double subscap;
	private double tricep;
	private double kidney;
	private double supra;
	private double girth_chest;
	private double girth_thigh;
	private double girth_abdom;
	private double arm;
	private double waist;
	private double calf;
	private double hips;
	private double neck;
	private double skin_thigh;
	private double skin_chest;
	private double skin_abdom;
	
	private int fitness_weight;
	private int fitness_height;
	private double bf;
	private double lbw;
	private double fw;
	private double rmr;
	private double exmr;
	private double protein;
	private double carbs;
	private double fats;
	
	
}

