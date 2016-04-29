package inlupp2_2016;



import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DescribedPlaceForm extends JPanel {

	private JTextField nameField = new JTextField(20);
	private JTextField descriptionField = new JTextField(30);

	public DescribedPlaceForm() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel rad1 = new JPanel();
		rad1.add(new JLabel("Namn "));
		rad1.add(nameField);
		add(rad1);
		JPanel rad2 = new JPanel();
		rad2.add(new JLabel("Desription: "));
		rad2.add(descriptionField);
		add(rad2);
	}
	
	public String getName(){
		return nameField.getText();
	}

	public String getDescription(){
		return descriptionField.getText();
	}
	
	public String toString (){
		return super.toString() +" " +getDescription();
	}
}
