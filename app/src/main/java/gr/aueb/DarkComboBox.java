package gr.aueb;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComboBox;

public class DarkComboBox extends JComboBox<String> implements FocusListener {
    Font defaultFont = new Font("Gill Sans MT", Font.BOLD, 14);
    Color textColor = Color.decode("#000000");
    Color backgroundColor = Color.decode("#e57e35");
    Color hoverColor = Color.decode("#9e5c15");
    Color focusColor = Color.decode("#9e5c15"); // Color when JComboBox has focus

    public DarkComboBox(String[] items) {
        super(items);
        customizeComboBox();
    }

    private void customizeComboBox() {
        this.setFont(defaultFont);
        this.setForeground(textColor);
        this.setBackground(backgroundColor);
        this.addFocusListener(this);
        //this.setUI(new DarkComboBox()); // Assuming you have a custom ComboBox UI to handle visual effects
    }

    @Override
    public void focusGained(FocusEvent e) {
        this.setBackground(focusColor);
    }

    @Override
    public void focusLost(FocusEvent e) {
        this.setBackground(backgroundColor);
    }
}