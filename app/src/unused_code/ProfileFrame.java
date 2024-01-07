package gr.aueb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ProfileFrame extends JFrame implements ActionListener {


	JFrame frm = new JFrame();
    Container container = getContentPane();
    JLabel profileInfo = new JLabel("PROFILE:");
    JLabel nameLabel = new JLabel("Name:");
    JLabel passwordLabel = new JLabel("Password:");

    JTextField nameTextField = new JTextField();
    JTextField passwordTextField = new JTextField();
    DarkButton updateButton = new DarkButton("Update");
    DarkButton backButton = new DarkButton("BACK");
    JLabel picLabel = new JLabel(new ImageIcon("logo.png"));
    JMenuBar mb = new JMenuBar();


    ProfileFrame() {
        initComponents();
    }

    ProfileFrame(User user) {
		nameTextField.setText(user.getUsername());
		passwordTextField.setText(user.getPassword());
	    initComponents();

	}


    private void initComponents() {
		 setTitle("Filmbro");
		 setVisible(true);
		    setBounds(10, 10, 600, 600);
		   // frm.setJMenuBar(mb);
		    setLocationRelativeTo(null); // center the application window

		    setLayoutManager();
			resizeComponents();
		    addComponentsToContainer();
		    addActionEvent();
		    setBackground(20,20,20);
		    setFont();
		    nameTextField.setEditable(false);
		    passwordTextField.setEditable(false);


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
    nameTextField.setBackground(new Color(a, b, c));
    passwordTextField.setBackground(new Color(a, b, c));
  }

    private void setFont() {
	profileInfo.setFont(new Font("Tahoma",0, 16));
	profileInfo.setForeground(new Color(230, 120, 50));
	passwordLabel.setForeground(new Color(230, 120, 50));
	nameLabel.setForeground(new Color(230, 120, 50));
    nameTextField.setForeground(new Color(230, 120, 50));
    passwordTextField.setForeground(new Color(230, 120, 50));
  }


    private void addComponentsToContainer() {
        container.add(profileInfo);
        container.add(nameLabel);
        container.add(passwordLabel);
        container.add(nameTextField);
        container.add(passwordTextField);
        container.add(updateButton);
        container.add(backButton);
        container.add(picLabel);
    }


    private void addActionEvent() {
        updateButton.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            // Κώδικας για την ενημέρωση των πληροφοριών του προφίλ

        }
    }


     public void resizeComponents() {
	    int width = this.getWidth();
	    int centerOffset = width / 4;
	    profileInfo.setBounds(centerOffset + 100, -40, 250, 150);
	    picLabel.setBounds(centerOffset + 50, 45, 150, 90);
		nameLabel.setBounds(centerOffset, 180, 120, 30);
        passwordLabel.setBounds(centerOffset,  230, 100, 30);
        nameTextField.setBounds(centerOffset + 100, 180, 150, 30);
        passwordTextField.setBounds(centerOffset + 100, 230, 150, 30);
	    updateButton.setBounds(centerOffset +15, 275, 200, 20);
	    backButton.setBounds(20, 490, 80, 30);
}

}