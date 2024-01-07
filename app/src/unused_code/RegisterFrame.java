package  gr.aueb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class RegisterFrame extends JFrame implements ActionListener {

  static String username;
  JFrame frm = new JFrame("Filmbro");
  JLabel welcomeMess = new JLabel("Sign up");
  Container container = getContentPane();
  JLabel userLabel = new JLabel("Username:");
  JLabel passwordLabel = new JLabel("Password:");
  JLabel confLabel = new JLabel("Confirm:");
  JPasswordField confField = new JPasswordField();
  JTextField userTextField = new JTextField(" e.g. nikosDask1992");
  JPasswordField passwordField = new JPasswordField();
  DarkButton loginButton = new DarkButton("REGISTER");
  DarkButton backButton = new DarkButton("BACK");
  //JCheckBox showPassword = new JCheckBox("Show Password");
  JLabel picLabel = new JLabel(new ImageIcon("logo.png"));  //image
  JMenuBar mb = new JMenuBar();


  RegisterFrame() {
    initComponents();
  }


  private void initComponents() {
    setTitle("Java");
    setBounds(10, 10, 600, 600);
    frm.setJMenuBar(mb);
    setLocationRelativeTo(null); // center the application window
    setVisible(true);
    setLayoutManager();
    setLocationAndSize();
    addComponentsToContainer();
    addActionEvent();
    setBackground(20,20,20);
    setFont();
     addComponentListener(new ComponentAdapter() {
		     @Override
		     public void componentResized(ComponentEvent e) {
		         resizeComponents();
		     }
});
  }


  public void setLayoutManager() {
    container.setLayout(null);
  }


  private void setBackground(int a, int b, int c) {
    container.setBackground(new Color(a, b, c));
    //showPassword.setBackground(new Color(a, b, c));
  }


  private void setFont() {
    welcomeMess.setFont(new Font("Tahoma", 0, 16));
    welcomeMess.setForeground(new Color(230, 120, 50));
    userLabel.setForeground(new Color(230, 120, 50));
    passwordLabel.setForeground(new Color(230, 120, 50));
    confLabel.setForeground(new Color(230, 120, 50));
  }


  public void setLocationAndSize() {
    userLabel.setBounds(50, 145, 100, 30);
    userTextField.setBounds(150, 145, 150, 30);
    passwordLabel.setBounds(50, 180, 100, 30);
    passwordField.setBounds(150, 180, 150, 30);
 	confLabel.setBounds(50, 215, 150, 30);
 	confField.setBounds(150, 215, 150, 30);
    //showPassword.setBounds(150, 250, 150, 30);
    loginButton.setBounds(125, 300, 100, 30);
    welcomeMess.setBounds(140, 50, 250, 150);
    picLabel.setBounds(100, 10, 150, 90);
    backButton.setBounds(20, 490, 80, 30);
  }

  public void addComponentsToContainer() {
    container.add(userLabel);
    container.add(welcomeMess);
    container.add(passwordLabel);
    container.add(userTextField);
    container.add(passwordField);
    //container.add(showPassword);
    container.add(loginButton);
    container.add(picLabel);
    container.add(backButton);
    container.add(confLabel);
    container.add(confField);
    container.add(confLabel);
  }


  public void addActionEvent() {
    loginButton.addActionListener(this);
    backButton.addActionListener(this);
    //showPassword.addActionListener(this);
    userTextField.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        userTextField.setText("");
      }
    });
  }



  @Override
    public void actionPerformed(ActionEvent e) {

	if (e.getSource() == backButton) {
		MenuBar m = new MenuBar(new MenuFrame());
		dispose();
	}
  }


  void resizeComponents() {
    int width = this.getWidth();
    int centerOffset = width / 4;

    userLabel.setBounds(centerOffset, 145, 100, 30);
    userTextField.setBounds(centerOffset + 100, 145, 150, 30);
    passwordLabel.setBounds(centerOffset, 180, 100, 30);
    passwordField.setBounds(centerOffset + 100, 180, 150, 30);
    confLabel.setBounds(centerOffset, 215, 150, 30);
    confField.setBounds(centerOffset + 100, 215, 150, 30);


    loginButton.setBounds(centerOffset + 74, 270, 140, 30);
    welcomeMess.setBounds(centerOffset + 105, 50, 250, 150);
    picLabel.setBounds(centerOffset + 60, 10, 150, 90);
    backButton.setBounds(20, 490, 80, 30);



}

}
