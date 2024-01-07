package gr.aueb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuBar extends JMenuBar implements ActionListener {

  JMenuBar mb;
  JMenu exit;
  JMenu  help;
  JMenuItem about;
  JMenuItem exitItem;
  JMenuItem logoutItem;

  MenuBar(JFrame frame) {
	exitItem = new JMenuItem("Exit");
    exitItem.addActionListener(this);
    logoutItem = new JMenuItem("Log out");
    logoutItem.addActionListener(this);
    about = new JMenuItem("About");
    about.addActionListener(this);
    mb = new JMenuBar();
    exit = new JMenu("Exit");
    help = new JMenu("Help");
    help.add(about);
    exit.add(exitItem);
    exit.add( logoutItem);
    mb.add(exit);
    mb.add(help);
	exit.addActionListener(this);
    frame.setJMenuBar(mb);

  }
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == exitItem) {
      int result = JOptionPane.showConfirmDialog(null,"Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
      if (result == JOptionPane.YES_OPTION)	{
        System.exit(0);
      }
    } else if (e.getSource() == about) {
      JOptionPane.showMessageDialog(null, "");
    } else if (e.getSource() == logoutItem){
		int result2 = JOptionPane.showConfirmDialog(null,"Do you want to log out?", "", JOptionPane.YES_NO_OPTION);
		      if (result2 == JOptionPane.YES_OPTION)	{
		        MenuFrame a = new MenuFrame();

      			}
	}
  }


}