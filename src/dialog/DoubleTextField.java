package dialog;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.Common;

public class DoubleTextField extends JTextField {
	private static final long serialVersionUID = 1L;
	
	public DoubleTextField() {
		super();
		setFont(new Font("Tahoma", Font.BOLD, 14));
		setHorizontalAlignment(SwingConstants.RIGHT);
		setColumns(10);
		Common.setRestrictions(this, "double");
		this.setText("1");
	}
	
	public double getDouble() {
		return Common.parseDouble(this.getText().trim());
	}

}