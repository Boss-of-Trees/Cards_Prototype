import javax.swing.*;  
import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
class NeuesDeck  
{  
  
    
  NeuesDeck()
  {  
    JFrame menu_f= new JFrame("Dein Deck");  
    
    
    JLabel label_l1,label_l2;  
    label_l1=new JLabel("Neues Deck erstellen");  
    label_l1.setBounds(50,50, 200,30);  
    label_l2=new JLabel("Name des Decks:");  
    label_l2.setBounds(50,100, 200,30); 
    menu_f.add(label_l1); 
    menu_f.add(label_l2);
    
    
    JButton bt2 = new JButton("Erstellen");
    bt2.setBackground(Color.gray); 
    bt2.setBounds(50,150, 200,30);              //adding Yes button to frame.
    menu_f.add(bt2);
    
    JTextField jtf = new JTextField(20);
    jtf.setBounds(150,100, 200,30); 
    menu_f.add(jtf); 
    
    
    menu_f.setSize(1000,700);  
    menu_f.setLayout(null);  
    menu_f.setVisible(true);  
  }  
  
  public static void main(String args[])  
  {  
    new NeuesDeck();  
  }
}
