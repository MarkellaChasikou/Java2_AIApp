package gr.aueb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



public class LoginFrame extends JFrame implements ActionListener {

  static String username;
  JFrame frm = new JFrame("Filmbro");
  JLabel welcomeMess = new JLabel("Hello, log in to your account.");
  Container container = getContentPane();
  JLabel userLabel = new JLabel("Username:");
  JLabel passwordLabel = new JLabel("Password:");
  JTextField userTextField = new JTextField(" e.g. Username");
  JPasswordField passwordField = new JPasswordField();
  DarkButton loginButton = new DarkButton("LOGIN");
  DarkButton backButton = new DarkButton("BACK");
  JCheckBox showPassword = new JCheckBox("Show Password");
  JLabel picLabel = new JLabel(new ImageIcon("logo.png"));  // an theloume na baloyme eikona
  JMenuBar mb = new JMenuBar();

  LoginFrame() {
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
    showPassword.setBackground(new Color(a, b, c));
  }

  private void setFont() {
    welcomeMess.setFont(new Font("Tahoma", 0, 16));
    welcomeMess.setForeground(new Color(230, 120, 50));
    userLabel.setForeground(new Color(230, 120, 50));
    passwordLabel.setForeground(new Color(230, 120, 50));
    showPassword.setForeground(new Color(230, 120, 50));


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
		      MenuBar m = new MenuBar(new MenuFrame());
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

public void resizeComponents() {
 int width = this.getWidth();
    int centerOffset = width / 4;  // Προσαρμόζετε τον παράγοντα ανάλογα με την πόσο πρέπει να μετακινηθούν τα στοιχεία.

    userLabel.setBounds(centerOffset, 150, 100, 30);
    passwordLabel.setBounds(centerOffset,  200, 100, 30);
    userTextField.setBounds(centerOffset + 100, 150, 150, 30);
    passwordField.setBounds(centerOffset + 100, 200, 150, 30);
    showPassword.setBounds(centerOffset + 100, 240, 150, 30);
    loginButton.setBounds(centerOffset +85, 300, 140, 30);  // Προσαρμόστε ανάλογα για την ευθυγράμμιση.
    welcomeMess.setBounds(centerOffset, 50, 230, 150);
    picLabel.setBounds(centerOffset + 50, 10, 150, 90);
    backButton.setBounds(20, 490, 80, 30);
}

}