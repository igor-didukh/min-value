package dialog;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.Common;

public class IntTextField extends JTextField {
	private static final long serialVersionUID = 1L;
	
	public IntTextField() {
		super();
		setHorizontalAlignment(SwingConstants.CENTER);
		setColumns(10);
		Common.setRestrictions(this, "int");
		this.setText("0");
	}
	
	public int getInt() {
		return Common.parseInt(this.getText().trim());
	}

}