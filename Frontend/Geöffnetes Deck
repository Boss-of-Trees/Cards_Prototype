import javax.swing.*;  
import java.awt.*;
import java.awt.geom.Ellipse2D;
class Deckgeoeffnet  
{  
  JMenu m_menu, m_submenu;  
  JMenuItem menu_i1, menu_i2, menu_i3, menu_i4, menu_i5;  
  
  
  Deckgeoeffnet()
  {  
    JFrame menu_f= new JFrame("Dein Deck");  
    JMenuBar menu_mb=new JMenuBar();  
    m_menu=new JMenu("Menu");  
    m_submenu=new JMenu("Sub Menu");  
    menu_i1=new JMenuItem("Red");  
    menu_i2=new JMenuItem("Pink");  
    menu_i3=new JMenuItem("Black");  
    menu_i4=new JMenuItem("Green");  
    menu_i5=new JMenuItem("White");  
    m_menu.add(menu_i1); 
    m_menu.add(menu_i2); 
    m_menu.add(menu_i3);  
    m_submenu.add(menu_i4); 
    m_submenu.add(menu_i5);  
    m_menu.add(m_submenu);  
    menu_mb.add(m_menu);
    
    JLabel label_l1;  
    label_l1=new JLabel("Deckname1");  
    label_l1.setBounds(90,65, 200,30);   
    menu_f.add(label_l1);
    
    JPanel panel3 = new JPanel();
    panel3.setBackground(Color.white);
    panel3.setBounds(50,50, 200,30);
    panel3.setSize( 150, 60 );
    menu_f.add(panel3);
    
    JButton bt3 = new JButton("Abfragen");
    bt3.setBackground(Color.white); 
    bt3.setBounds(700,50, 200,30); 
    bt3.setSize( 150, 60 );
    menu_f.add(bt3);
    
    
    JButton bt2 = new JButton("Neue Card");
    bt2.setBackground(Color.gray); 
    bt2.setBounds(50,150, 200,30);              //adding Yes button to frame.
    menu_f.add(bt2);
    
    JButton bt6 = new JButton("Card löschen");
    bt6.setBackground(Color.gray); 
    bt6.setBounds(50,200, 200,30);              //adding Yes button to frame.
    menu_f.add(bt6);
    
    JButton bt7 = new JButton("Deck löschen");
    bt7.setBackground(Color.gray); 
    bt7.setBounds(50,350, 200,30);              //adding Yes button to frame.
    menu_f.add(bt7);
    
    menu_f.setJMenuBar(menu_mb);
    menu_f.setSize(1000,700);
    
    menu_f.setLayout(null);  
    menu_f.setVisible(true);  
  }  
  
  public static void main(String args[])  
  {  
    new Deckgeoeffnet();  
  }
}
