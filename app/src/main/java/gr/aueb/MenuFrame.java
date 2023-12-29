package gr.aueb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class MenuFrame extends JFrame implements ActionListener {

  
  private static final long serialVersionUID = 1L;
  JLabel menuMess = new JLabel("MENU");
  JLabel logoMess = new JLabel("YOUR AI MOVIE SOULMATE");
  Container container = getContentPane();

  DarkButton category1 = new DarkButton("LOGIN");
  DarkButton category2 = new DarkButton("REGISTER");
  DarkButton category3 = new DarkButton("ENTER AS A GUEST");


  JLabel picLabel = new JLabel(new ImageIcon("Filmbro_Logo.png")); //IMAGE

  String username;


  public MenuFrame(String username) {
    this.username = username;
    initComponents();
  }

 public MenuFrame() {
	    initComponents();
 }
  private void initComponents() {
    setTitle("Filmbro");
    setVisible(true);
    setBounds(10, 10, 600, 600);

    setLocationRelativeTo(null); //centers the application window
    setLayoutManager();
    setLocationAndSize();

    addComponentsToContainer();

    addActionEvent();
    setFont();
    setBackground(153,153,153);
    addComponentListener(new ComponentAdapter() {
	     @Override
	     public void componentResized(ComponentEvent e) {
	         resizeComponents();
	     }
});


  }

  void setLayoutManager() {
    container.setLayout(null);
  }


  void setFont() {
    menuMess.setFont(new Font("Tahoma", 0, 16));
    menuMess.setForeground(new Color(230, 120, 50));
    logoMess.setFont(new Font("Tahoma", 0, 13));
    logoMess.setForeground(new Color(230, 120, 50));

  }



  void setBackground(int a, int b, int c) {
    container.setBackground(new Color(20,20,20));
  }


  void setLocationAndSize() {
    menuMess.setBounds(150, 50, 200, 150);
    category1.setBounds(100, 150, 150, 30);
    category2.setBounds(100, 200, 150, 30);
    category3.setBounds(100, 250, 150, 30);
    picLabel.setBounds(100, 10, 150, 90);
  }

  void addComponentsToContainer() {
    container.add(menuMess);
    container.add(category1);
    container.add(category2);
    container.add(category3);
    container.add(picLabel);
    container.add(logoMess);
  }


  void addActionEvent() {

    category1.addActionListener(this);
    category2.addActionListener(this);
    category3.addActionListener(this);
  }


  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == category1) {
		MenuBar m = new MenuBar( new LoginFrame());
    	dispose();
 	 }

    if (e.getSource() == category2) {
		MenuBar m = new MenuBar( new RegisterFrame());
       dispose();
  }


    if (e.getSource() == category3) {
		//enter as a guest
      //dispose();
  }


}


void resizeComponents() {
    menuMess.setBounds(getWidth() / 2 - 30, 100, 200, 150);
    logoMess.setBounds(getWidth() / 2 - 85, 50, 200, 150);
    category1.setBounds(getWidth() / 2 - 75, 200, 150, 30);
    category2.setBounds(getWidth() / 2 - 75, 250, 150, 30);
    category3.setBounds(getWidth() / 2 - 75, 300, 150, 30);
    picLabel.setBounds(getWidth() / 2 - 75, 20, 150, 90);
}


}