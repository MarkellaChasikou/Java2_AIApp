package gr.aueb;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;


public class ChatFrame extends JFrame implements ActionListener{

  String category;
  JFrame frame = new JFrame();
  JLabel chatMess1 = new JLabel("YOU ARE IN CHATROOM");
  JLabel prevMess = new JLabel("PREVIOUS MESSAGES");
  Container container = getContentPane();
  DarkButton  sentButton = new DarkButton ("SEND");
  DarkButton  likeMessButton = new DarkButton ("I like the message ");
  JTextArea textArea = new JTextArea(" Type your message here!");
  DarkButton backButton = new DarkButton ("BACK");
  static JTextPane showMess = new JTextPane();
  //Chatroom chat = new Chatroom("2");
  //ArrayList<String> messages = chat.getUnseenMessages();
  ArrayList<String> messages = new ArrayList<String>();
  JScrollPane jsp = new JScrollPane(showMess, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  JLabel picLabel = new JLabel(new ImageIcon("logo.png"));


  public ChatFrame() {
    initComponents();
  }


  private void initComponents() {
    setTitle("Filmbro");
    setVisible(true);
     setBounds(10, 10, 600, 600);
    setLocationRelativeTo(null);
    setLayoutManager();
    resizeComponents();
    addComponentsToContainer();
    addActionEvent();
    setFont();
    showMess.setEditable(false);
    setBackground();
        addComponentListener(new ComponentAdapter() {
			     @Override
			     public void componentResized(ComponentEvent e) {
			         resizeComponents();
			     }
	});

  }


  private void setFont() {
	chatMess1.setFont(new Font("Tahoma", 0, 16));
    chatMess1.setForeground(new Color(230, 120, 50));
    textArea.setForeground(new Color(230, 120, 50));
    showMess.setForeground(new Color(230, 120, 50));
    prevMess.setFont(new Font("Tahoma", 0, 13));
    prevMess.setForeground(new Color(230, 120, 50));
  }



  private void setBackground() {
    container.setBackground(new Color(20,20,20));
    showMess.setBackground(new Color(20,20,20));
    textArea.setBackground(new Color(20,20,20));
  }


  public void setLayoutManager() {
    container.setLayout(null);
  }


  public void resizeComponents() {
    int width = this.getWidth();
    int centerOffset = width / 4;

    chatMess1.setBounds(centerOffset + 40, -30, 250, 150);
    sentButton.setBounds(centerOffset + 200, 430, 80, 30);
    prevMess.setBounds(centerOffset + 60, 95, 240, 150);
    picLabel.setBounds(centerOffset + 50, 55, 150, 90);
    jsp.setBounds(centerOffset - 15, 185,300, 210);
    scroll.setBounds(centerOffset - 15, 420, 200, 50);
    likeMessButton.setBounds(centerOffset - 15, 475, 200, 20);
    backButton.setBounds(20, 490, 80, 30);
}


  public void addComponentsToContainer() {
    container.add(chatMess1);
    container.add(sentButton);
    container.add(backButton);
    container.add(scroll);
    container.add(prevMess);
    container.add(jsp);
    container.add(likeMessButton);
    container.add(picLabel);
  }


  public void addActionEvent() {

   // obj.selectmessages();

    textArea.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        if (" Type your message here!".equals(textArea.getText())) {
              textArea.setText("");
        }
      }
    });
    sentButton.addActionListener(this);
    backButton.addActionListener(this);
    likeMessButton.addActionListener(this);
  }

  public void actionPerformed(ActionEvent e) {


    if (e.getSource() == backButton) {
      new MenuBar(new MenuFrame());
      dispose();
    }

    if (e.getSource() == sentButton) {
		String message = textArea.getText();
		 if (!message.isEmpty()) {
		 	messages.add(message);
		 	//chat.addMessage(id,message)
		    displayMessages();
		    textArea.setText("");
            }
    }

	if (e.getSource() == likeMessButton) {
	  textArea.setText("I like the message ");
    }
  }


private void displayMessages() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < messages.size(); i++) {

        String cleanedMessage = messages.get(i).replaceAll("[\\[\\]//,]", "");
        sb.append(i + 1).append(". ").append(cleanedMessage).append("\n");
    }
    showMess.setText(sb.toString());
}

}
