import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class account extends JFrame {
	private JTextField textField;
	private final String trainer;
    public account(String trainerUsername) {
        trainer = trainerUsername;
        String trainerEmail = null;
        try {
        sqlMethods sql = new sqlMethods();
        ResultSet email = sql.getTrainerEmail(trainer);
       
        while(email.next()){
            trainerEmail = email.getString(1);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
		setTitle("MetabolixLite: Account Settings");
		getContentPane().setBackground(new Color(255, 235, 205));
		getContentPane().setLayout(null);
		setMinimumSize(new Dimension(335, 415));
		setMaximumSize(new Dimension(335, 415));
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(account.class.getResource("/images/logo.png")));
		label.setBounds(69, 0, 185, 180);
		getContentPane().add(label);
		
		JLabel label_1 = new JLabel("New Password:");
		label_1.setFont(new Font("Cambria Math", Font.PLAIN, 13));
		label_1.setBounds(23, 294, 92, 14);
		getContentPane().add(label_1);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(125, 292, 155, 20);
		getContentPane().add(textField);
		
		JLabel label_2 = new JLabel("");
		label_2.setForeground(Color.RED);
		label_2.setBounds(172, 350, 124, 14);
		getContentPane().add(label_2);
		
		JButton button = new JButton("Change");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = textField.getText();
				sqlMethods sql = new sqlMethods();
				String trainerEmail = null;
		        try {
		        sqlMethods sql2 = new sqlMethods();
		        ResultSet email = sql2.getTrainerEmail(trainer);
		       
		        while(email.next()){
		            trainerEmail = email.getString(1);
		            }
		        } catch(Exception ex){
		            ex.printStackTrace();
		        }

					sql.resetPass(trainerEmail, s);
					JOptionPane.showMessageDialog(null, "Password changed.");
					setVisible(false);
			}
		});
		button.setBounds(106, 330, 109, 23);
		getContentPane().add(button);
		
		JLabel lblNewLabel = new JLabel("Change Password:");
		lblNewLabel.setFont(new Font("Cambria Math", Font.PLAIN, 15));
		lblNewLabel.setBounds(10, 264, 126, 20);
		getContentPane().add(lblNewLabel);
		
		JLabel lblTrainerInformation = new JLabel("Trainer Information:");
		lblTrainerInformation.setFont(new Font("Cambria Math", Font.PLAIN, 15));
		lblTrainerInformation.setBounds(10, 174, 138, 20);
		getContentPane().add(lblTrainerInformation);
		
		JLabel label_4 = new JLabel("Email:");
		label_4.setFont(new Font("Cambria Math", Font.PLAIN, 13));
		label_4.setBounds(23, 211, 46, 14);
		getContentPane().add(label_4);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Cambria Math", Font.PLAIN, 13));
		lblUsername.setBounds(23, 239, 65, 14);
		getContentPane().add(lblUsername);
		
	    JLabel emailVar = new JLabel(trainerEmail);
	    emailVar.setBounds(122, 206, 234, 14);
	    getContentPane().add(emailVar);
	       
	    JLabel usernameVar = new JLabel(trainer);
	    usernameVar.setBounds(122, 237, 221, 14);
	    getContentPane().add(usernameVar);
	}
}
