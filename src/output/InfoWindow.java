package output;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.JTextPane;

public class InfoWindow extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	public InfoWindow(String info) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Result:");
		//setModal(true);
		
		setBounds(0, 0, 455, 397);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 238, 238));
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setText(info);
		contentPane.add(textPane, BorderLayout.CENTER);
	}
}