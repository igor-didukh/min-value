package dialog;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.EtchedBorder;

import data.Function;
import main.Common;
import main.Solution;

import javax.swing.JButton;

public class FunctionDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String SAVE = "SAVE";
	
	private JPanel contentPane;
	private DoubleTextField txtA, txtB;
	private JLabel lblName;
	private JComboBox<String> comboDirection;
	
	public FunctionDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		
		setTitle("Edit function");
		setBounds(0, 0, 356, 154);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 238, 238));
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtA = new DoubleTextField();
		txtA.setBounds(25, 11, 61, 26);
		txtA.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				makeName();
			}
		});
		txtA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				makeName();
			}
		});
		contentPane.add(txtA);
		
		JLabel lblX1 = new JLabel("* x1 +");
		lblX1.setHorizontalAlignment(SwingConstants.CENTER);
		lblX1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblX1.setBounds(90, 11, 55, 26);
		contentPane.add(lblX1);
		
		txtB = new DoubleTextField();
		txtB.setBounds(143, 11, 61, 26);
		txtB.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				makeName();
			}
		});
		txtB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				makeName();
			}
		});
		contentPane.add(txtB);
		
		JLabel lblX2 = new JLabel("* x2");
		lblX2.setHorizontalAlignment(SwingConstants.CENTER);
		lblX2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblX2.setBounds(206, 11, 38, 26);
		contentPane.add(lblX2);
		
		comboDirection = new JComboBox<String>();
		comboDirection.setModel(new DefaultComboBoxModel<String>(new String[] {Common.F_MIN, Common.F_MAX}));
		comboDirection.setBounds(247, 11, 80, 26);
		comboDirection.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				makeName();
			}
		});
		comboDirection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				makeName();
			}
		});
		contentPane.add(comboDirection);
		
		JPanel panelName = new JPanel();
		panelName.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panelName.setBounds(10, 41, 334, 30);
		contentPane.add(panelName);
		
		lblName = new JLabel("");
		panelName.add(lblName);
		lblName.setForeground(new Color(0, 0, 128));
		lblName.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnOK = new JButton("OK");
		btnOK.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnOK.setBounds(63, 82, 108, 33);
		btnOK.addActionListener(this);
		btnOK.setActionCommand(SAVE);
		contentPane.add(btnOK);
		
		JButton btnCancel = new JButton("Cancel");
		
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCancel.setBounds(176, 82, 104, 33);
		btnCancel.addActionListener(this);
		contentPane.add(btnCancel);
		
		initFields();
		makeName();
	}
	
	private void initFields() {
		Function fn = Solution.objectiveFunction;
		
		txtA.setText( Common.clip( fn.getA() ) );
		txtB.setText( Common.clip( fn.getB() ) );
		lblName.setText( fn.toString() );
		
		if (fn.getDirection() == "--> min") 
			comboDirection.setSelectedIndex(0);
		else
			comboDirection.setSelectedIndex(1);
	}

	private void makeName() {
		double a = txtA.getDouble();
		double b = txtB.getDouble();
		String dir = (String) comboDirection.getSelectedItem();
		lblName.setText(Common.makeBinomialName(a, b, dir));
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		double a = txtA.getDouble();
		double b = txtB.getDouble();
		
		if (ae.getActionCommand() == SAVE)
			if ((a == 0) && (b == 0))
				Common.showErrorMessage(this, "Both coefs should not be zero!");
			else {
				Solution.objectiveFunction.setA(a);
				Solution.objectiveFunction.setB(b);
				Solution.objectiveFunction.setDirection( (String) comboDirection.getSelectedItem() );
				Solution.objectiveFunction.setName( lblName.getText() );
				dispose();
			}
		else 
			dispose();
		
	}
}