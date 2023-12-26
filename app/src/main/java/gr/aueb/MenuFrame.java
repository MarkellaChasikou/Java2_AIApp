
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MenuFrame extends JFrame implements ActionListener {

  private static final long serialVersionUID = 1L;
  JLabel menuMess = new JLabel("MENU");
  Container container = getContentPane();

  JButton category1 = new JButton("LOGIN");
  JButton category2 = new JButton("REGISTER");
  JButton category3 = new JButton("ENTER AS A GUEST");


  JLabel picLabel = new JLabel(new ImageIcon()); //IMAGE

  String username;


  public MenuFrame(String username) {
    this.username = username;
    initComponents();
  }

 public MenuFrame() {
	    initComponents();
 }
  private void initComponents() {
    setTitle("Java");
    setVisible(true);
    setBounds(10, 10, 370, 600);

    setLocationRelativeTo(null); //centers the application window
    setLayoutManager();
    setLocationAndSize();

    addComponentsToContainer();

    addActionEvent();
    setFont();
     setBackground(251, 174, 210);


  }

  void setLayoutManager() {
    container.setLayout(null);
  }


  void setFont() {
    menuMess.setFont(new Font("Tahoma", 0, 16));
  }



  void setBackground(int a, int b, int c) {
    container.setBackground(new Color(241, 156, 187));
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
  }


  void addActionEvent() {

    category1.addActionListener(this);
    category2.addActionListener(this);
    category3.addActionListener(this);
  }


  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == category1) {
		LoginFrame a = new LoginFrame();
    	dispose();
 	 }

    if (e.getSource() == category2) {
		RegisterFrame a = new RegisterFrame();
      dispose();
  }


    if (e.getSource() == category3) {
		//enter as a guest
      //dispose();
  }


}
}