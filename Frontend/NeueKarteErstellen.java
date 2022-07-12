
/**
 * Beschreiben Sie hier die Klasse GUI.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */



import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
public class NeueKarteErstellen
{
   public static void main(String [] args)
   {   JPanel panel = new JPanel(); 
       JFrame frame = new JFrame();
       //frame.setSize(1920,1080); 
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
       frame.setVisible(true); 
       frame.add(panel); 
       frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
       String name[] = {"Abhi","Adam","Alex","Ashkay"}; 


       panel.setLayout(null);

       JLabel Vorne = new JLabel("Kartenvorderseite"); 
       Vorne.setBounds(500,250,500,65);
       Vorne.setFont(new Font("Serif", Font.BOLD, 50));;
       panel.add(Vorne); 

       JLabel Hinten = new JLabel("Kartenrückseite"); 
       Hinten.setBounds(500,700,500,65);
       Hinten.setFont(new Font("Serif", Font.BOLD, 50));;
       panel.add(Hinten); 

       JTextField fronttext = new JTextField(20); 
       fronttext.setBounds(500,350,600,150);
       panel.add(fronttext); 

       JTextField backtext = new JTextField(20); 
       backtext.setBounds(500,800,600,150);
       panel.add(backtext); 

       JButton Erstellbutton =  new JButton("Karte erstellen"); 
       Erstellbutton.setBounds(500,1000,165,30);
       Erstellbutton.setBackground(Color.BLUE);
       Erstellbutton.setForeground(Color.WHITE);
       panel.add(Erstellbutton);

       JComboBox choice = new JComboBox<>(); 

       JComboBox jc = new JComboBox(name);	//initialzing combo box with list of name.
       panel.add(jc);				//adding JComboBox to frame.
       //frame.setSize(100, 80);
       //frame.setVisible(true);

 



   }

   
}
