package inlupp2_2016;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NamedPlaceForm extends JPanel{
	private JTextField nameField = new JTextField(20);

	public NamedPlaceForm() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel rad1 = new JPanel();
		rad1.add(new JLabel("Namn "));
		rad1.add(nameField);
		add(rad1);
	}
	
	public String getName(){
		return nameField.getText();
	}
	

}
