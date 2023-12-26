import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class LoginFrame extends JFrame implements ActionListener {

  static String username;
  JFrame frm = new JFrame("Java");
  JLabel welcomeMess = new JLabel("Hello, log in to your account.");
  Container container = getContentPane();
  JLabel userLabel = new JLabel("Username:");
  JLabel passwordLabel = new JLabel("Password:");
  JTextField userTextField = new JTextField(" e.g. Username");
  JPasswordField passwordField = new JPasswordField();
  JButton loginButton = new JButton("LOGIN");
  JButton backButton = new JButton("BACK");
  JCheckBox showPassword = new JCheckBox("Show Password");
  JLabel picLabel = new JLabel(new ImageIcon());  // an theloume na baloyme eikona
  JMenuBar mb = new JMenuBar();

  LoginFrame() {
    initComponents();
  }


  private void initComponents() {
    setTitle("Java");
    setBounds(10, 10, 370, 600);
    frm.setJMenuBar(mb);

    setLocationRelativeTo(null); // center the application window
    setVisible(true);
    setLayoutManager();
    setLocationAndSize();
    addComponentsToContainer();
    addActionEvent();
    setBackground(241, 156, 187);
    setFont();
  }

  public void setLayoutManager() {
    container.setLayout(null);
  }

  private void setBackground(int a, int b, int c) {
    container.setBackground(new Color(a, b, c));
    showPassword.setBackground(new Color(a, b, c));
  }

  private void setFont() {
    welcomeMess.setFont(new Font("Tahoma", 0, 16));
  }

  public void setLocationAndSize() {
    userLabel.setBounds(50, 150, 100, 30);
    passwordLabel.setBounds(50, 200, 100, 30);
    userTextField.setBounds(150, 150, 150, 30);
    passwordField.setBounds(150, 200, 150, 30);
    showPassword.setBounds(150, 240, 150, 30);
    loginButton.setBounds(125, 300, 100, 30);
    welcomeMess.setBounds(70, 50, 230, 150);
    picLabel.setBounds(100, 10, 150, 90);
    backButton.setBounds(20, 490, 80, 30);
  }

  public void addComponentsToContainer() {
    container.add(userLabel);
    container.add(welcomeMess);
    container.add(passwordLabel);
    container.add(userTextField);
    container.add(passwordField);
    container.add(showPassword);
    container.add(loginButton);
    container.add(picLabel);
    container.add(backButton);
  }


  public void addActionEvent() {
    loginButton.addActionListener(this);
    showPassword.addActionListener(this);
    backButton.addActionListener(this);
    userTextField.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        userTextField.setText("");
      }
    });
  }



  @Override
    public void actionPerformed(ActionEvent e) {


		if (e.getSource() == backButton) {
		      MenuFrame a = new MenuFrame();
		      dispose();
		    }

    	if (e.getSource() == showPassword) {
      		if (showPassword.isSelected()) {
        		passwordField.setEchoChar((char) 0);
      		}else {
        		passwordField.setEchoChar('*');
      	}
    }
  }
}