package gr.aueb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;



public class BonusFrame extends JFrame implements ActionListener {


	static String username;
	JFrame frm = new JFrame();
    Container container = getContentPane();
    JLabel bonusLabel = new JLabel("BONUS CONTENT:");
    String bonus[] = {"FUN", "CAT2"};
    DarkComboBox bn = new DarkComboBox(bonus);
    DarkButton backButton = new DarkButton("BACK");
    JLabel picLabel = new JLabel(new ImageIcon("logo.png"));
    JMenuBar mb = new JMenuBar();
	JLabel bonusContentLabel = new JLabel();

    BonusFrame() {
        initComponents();
    }

    private void initComponents() {
		 setTitle("Filmbro");
		    setBounds(10, 10, 600, 600);
		    frm.setJMenuBar(mb);
		    setLocationRelativeTo(null); // center the application window
		    setVisible(true);
		    setLayoutManager();
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
  }

    private void setFont() {
	bonusContentLabel.setFont(new Font("Tahoma",0, 16));
	bonusContentLabel.setForeground(new Color(230, 120, 50));
	bonusLabel.setForeground(new Color(230, 120, 50));

  }

    private void addComponentsToContainer() {
        container.add(backButton);
        container.add(picLabel);
        container.add(bonusContentLabel);
        container.add(bn);
        container.add(bonusLabel);

    }


    private void addActionEvent() {
        bn.addActionListener(this);
    }


    @Override
       public void actionPerformed(ActionEvent e) {
           if (e.getSource() == bn) {
               String selectedBonus = (String) bn.getSelectedItem();
               switch (selectedBonus) {
                   case "FUN":
                       bonusContentLabel.setText("In Titanic, Jack accidentally telling Rose to lie on the “bed” instead of the “couch” before he draws apparently wasn’t in the script.");
                       break;
                   case "":
                       bonusContentLabel.setText("Bonus Content: MPLA1 Bonus Content");
                       break;
                   default:
                       bonusContentLabel.setText("No Bonus Content Available");
                       break;
               }
               changeLineInBonusContent();
           }
       }



     public void resizeComponents() {
	    int width = this.getWidth();
	    int centerOffset = width / 4;


	    picLabel.setBounds(centerOffset + 50, 45, 150, 90);
	    bn.setBounds(centerOffset +15, 275, 200, 30);
	     bonusContentLabel.setBounds(centerOffset -75, 300, 400, 60);
	    backButton.setBounds(20, 490, 80, 30);
	    bonusLabel.setBounds(centerOffset +15, 245, 200, 30);

}

private void changeLineInBonusContent() {
    String currentContent = bonusContentLabel.getText();
    String[] words = currentContent.split("\\s+");

    int wordsPerLine = 7; // Ο αριθμός των λέξεων που θέλετε σε κάθε γραμμή

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < words.length; i++) {
        sb.append(words[i]).append(" ");
        if ((i + 1) % wordsPerLine == 0) {
            sb.append("\n");
        }
    }

    bonusContentLabel.setText(sb.toString().trim());
}

}