
/**
 * Beschreiben Sie hier die Klasse GUI.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */


import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.plaf.FontUIResource;
public class AbfrageSeite
{
  
/**
 * @param args
 */
public static void main(String [] args)
   {   JPanel panel = new JPanel(); 
       JFrame frame = new JFrame();
       //Tutorial t = new Tutorial(); 
       //frame.setSize(350,200); 
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
       frame.setVisible(true); 
       frame.add(panel); 
       frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 

       panel.setLayout(null);

       JLabel Überschrift = new JLabel("Lernmodus"); 
       Überschrift.setBounds(910,100,500,65);
       Überschrift.setFont(new FontUIResource("Serif", Font.BOLD, 50)); 
       panel.add(Überschrift); 

       JButton CardRectangle =  new JButton("Klicke für die Lösung"); 
       CardRectangle.setBounds(800,500,500,500);
       CardRectangle.setBackground(Color.BLACK);
       CardRectangle.setForeground(Color.LIGHT_GRAY);
       panel.add(CardRectangle);

       JButton Before =  new JButton("Davor"); 
       Before.setBounds(310,500,165,30);
       Before.setBackground(Color.BLUE);
       Before.setForeground(Color.LIGHT_GRAY);
       panel.add(Before);

       JButton Next =  new JButton("Danach"); 
       Next.setBounds(1500,500,165,30);
       Next .setBackground(Color.BLACK);
       Next .setForeground(Color.LIGHT_GRAY);
       panel.add(Next );

       
       //t.paint(null); 

       



   }

}
