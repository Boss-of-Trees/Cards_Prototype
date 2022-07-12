
/**
 * Beschreiben Sie hier die Klasse GUI.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */



import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
public class NeueBenutzerSeite
{
   public static void main(String [] args)
   {   JPanel panel = new JPanel(); 
       JFrame frame = new JFrame();
       frame.setSize(350,200); 
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
       frame.setVisible(true); 
       frame.add(panel); 

       panel.setLayout(null);

       JLabel userlabel = new JLabel("New User"); 
       userlabel.setBounds(10,20,80,25);
       panel.add(userlabel); 

       JTextField textfield = new JTextField(20); 
       textfield.setBounds(100,20,165,25);
       panel.add(textfield); 

       JLabel passwordtext = new JLabel("Passwort"); 
       passwordtext.setBounds(10,60,80,25);
       panel.add(passwordtext); 

       JPasswordField passwordfield = new JPasswordField();
       passwordfield.setBounds(100,60,165,25);
       panel.add(passwordfield); 

       JButton createAccount =  new JButton("Create Account"); 
       createAccount.setBounds(10,100,165,30);
       createAccount.setBackground(Color.BLACK);
       createAccount.setForeground(Color.LIGHT_GRAY);
       panel.add(createAccount);



   }
}
