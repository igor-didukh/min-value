package main;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.border.EtchedBorder;

import data.Constraint;
import data.Function;
import dialog.ConstraintDialog;
import dialog.FunctionDialog;
import dialog.IntTextField;
import output.DrawConstraint;

public class Solution extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final String EDIT_FUNCTION = "EDIT_FUNCTION", ADD_CONSTRAINT = "ADD_CONSTRAINT", EDIT_CONSTRAINT = "EDIT_CONSTRAINT", DELETE_CONSTRAINT = "DELETE_CONSTRAINT", 
			DRAW_CONSTRAINT = "SHOW_CONSTRAINT", SOLVE = "SOLVE", EXIT = "EXIT";
	
	public static Constraint constraintFromDialog;
	public static Function objectiveFunction;
	
	private JPanel contentPane, panelMain, panelName;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JLabel lblFunction, lblSolution;
	private JButton btnAddConstraint, btnEditConstraint, btnDeleteConstraint, btnSolve; 
	
	private final DefaultListModel<Constraint> listModel = new DefaultListModel<Constraint>();
	private JList<Constraint> listConstraints;
	private IntTextField txtInf;
	
	public Solution() {
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				closeFrame(evt);
			}
		});
		
		setTitle("Solving of the linear programming problem");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setBounds(100, 100, 567, 353);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 238, 238));
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		menuBar = new JMenuBar();
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		mnFile = new JMenu("File");
		
		JMenuItem mntmShowSelected = new JMenuItem("Show constraint");
		mntmShowSelected.setActionCommand(DRAW_CONSTRAINT);
		mntmShowSelected.addActionListener(this);
		mnFile.add(mntmShowSelected);
		
		JMenuItem mntmSolve = new JMenuItem("Solve");
		mntmSolve.setActionCommand(SOLVE);
		mntmSolve.addActionListener(this);
		mnFile.add(mntmSolve);
		
		mnFile.addSeparator();
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setActionCommand(EXIT);
		mntmExit.addActionListener(this);
		mnFile.add(mntmExit);
		
		menuBar.add(mnFile);
		
		panelMain = new JPanel();
		contentPane.add(panelMain);
		panelMain.setLayout(null);
		
		panelName = new JPanel();
		panelName.setBounds(10, 11, 539, 58);
		panelName.setLayout(new BorderLayout(0, 0));
		panelMain.add(panelName);
		
		JLabel lblNewLabel = new JLabel("(graphical method)");
		lblNewLabel.setForeground(new Color(0, 0, 128));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panelName.add(lblNewLabel, BorderLayout.SOUTH);
		
		JLabel lblName = new JLabel("Solving of the linear programming problem");
		lblName.setVerticalAlignment(SwingConstants.CENTER);
		lblName.setForeground(new Color(0, 0, 128));
		lblName.setFont(new Font("Arial", Font.PLAIN, 28));
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		panelName.add(lblName, BorderLayout.CENTER);
		
		JPanel panelFunction = new JPanel();
		panelFunction.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelFunction.setBounds(10, 80, 539, 34);
		panelFunction.setLayout(new BorderLayout(0, 0));
		panelMain.add(panelFunction);
		
		JLabel lblFName = new JLabel(" Objective function:");
		lblFName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFName.setHorizontalAlignment(SwingConstants.CENTER);
		panelFunction.add(lblFName, BorderLayout.WEST);
		
		lblFunction = new JLabel("New label");
		lblFunction.setForeground(new Color(128, 0, 0));
		lblFunction.setHorizontalAlignment(SwingConstants.CENTER);
		lblFunction.setFont(new Font("Tahoma", Font.BOLD, 14));
		panelFunction.add(lblFunction, BorderLayout.CENTER);
		
		JButton btnEditFunction = new JButton("Edit...");
		btnEditFunction.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnEditFunction.setActionCommand(EDIT_FUNCTION);
		btnEditFunction.addActionListener(this);
		panelFunction.add(btnEditFunction, BorderLayout.EAST);
		
		JPanel panelSystem = new JPanel();
		panelSystem.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		panelSystem.setBounds(55, 125, 204, 161);
		panelMain.add(panelSystem);
		panelSystem.setLayout(new BorderLayout(0, 0));
		
		JLabel lblSystemTitle = new JLabel("Constraint system:");
		lblSystemTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSystemTitle.setHorizontalAlignment(SwingConstants.CENTER);
		panelSystem.add(lblSystemTitle, BorderLayout.NORTH);
		
		JPanel panelActions = new JPanel();
		panelSystem.add(panelActions, BorderLayout.EAST);
		panelActions.setLayout(new GridLayout(4, 1, 0, 0));
		
		btnAddConstraint = new JButton("+");
		btnAddConstraint.setActionCommand(ADD_CONSTRAINT);
		btnAddConstraint.addActionListener(this);
		panelActions.add(btnAddConstraint);
		
		btnEditConstraint = new JButton("*");
		btnEditConstraint.setActionCommand(EDIT_CONSTRAINT);
		btnEditConstraint.addActionListener(this);
		panelActions.add(btnEditConstraint);
		
		btnDeleteConstraint = new JButton("X");
		btnDeleteConstraint.setActionCommand(DELETE_CONSTRAINT);
		btnDeleteConstraint.addActionListener(this);
		panelActions.add(btnDeleteConstraint);
		
		listConstraints = new JList<>(listModel);
		listConstraints.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelSystem.add(listConstraints, BorderLayout.CENTER);
		
		JButton btnShowConstraint = new JButton("Show selected constraint");
		btnShowConstraint.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnShowConstraint.setBounds(293, 174, 229, 50);
		btnShowConstraint.setActionCommand(DRAW_CONSTRAINT);
		btnShowConstraint.addActionListener(this);
		panelMain.add(btnShowConstraint);
		
		btnSolve = new JButton("Solve");
		btnSolve.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnSolve.setActionCommand("SHOW_AREA");
		btnSolve.setBounds(293, 236, 229, 50);
		btnSolve.setActionCommand(SOLVE);
		btnSolve.addActionListener(this);
		panelMain.add(btnSolve);
		
		lblSolution = new JLabel();
		lblSolution.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSolution.setForeground(new Color(0, 0, 128));
		lblSolution.setHorizontalAlignment(SwingConstants.CENTER);
		lblSolution.setBounds(20, 297, 505, 34);
		panelMain.add(lblSolution);
		
		JLabel lblInf = new JLabel("\"Infinite\" boundary:");
		lblInf.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInf.setBounds(293, 136, 132, 25);
		panelMain.add(lblInf);
		
		txtInf = new IntTextField();
		txtInf.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtInf.setBounds(420, 136, 50, 25);
		txtInf.setText("30");
		panelMain.add(txtInf);
		txtInf.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				makeInf();
			}
		});
		txtInf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				makeInf();
			}
		});
		
		makeInf();
		addTaskData();
		checkButtons();
	}
	
	private void makeInf() {
		Common.INF = txtInf.getInt();
	}
	
	private void addTaskData() {
		objectiveFunction = new Function(0, 1, Common.F_MIN);
		lblFunction.setText( objectiveFunction.toString() );
		
		///*
		listModel.addElement( new Constraint(-3, 1, Common.C_LOE, -15) );
		listModel.addElement( new Constraint(0.2f, -1, Common.C_GOE, -5) );
		listModel.addElement( new Constraint(3, 1, Common.C_GOE, 21) );
		listModel.addElement( new Constraint(0, 1, Common.C_GOE, 0) );
		listModel.addElement( new Constraint(1, 0, Common.C_GOE, 0) );
		//*/
		
		/*
		listModel.addElement( new Constraint(1, 1, Common.C_LOE, 7) );
		listModel.addElement( new Constraint(0, 1, Common.C_GOE, 0) );
		listModel.addElement( new Constraint(1, 0, Common.C_GOE, 5) );
		*/
		
		listConstraints.setSelectedIndex(0);
	}
	
	private void checkButtons() {
		int listSize = listModel.getSize();
		btnEditConstraint.setEnabled(listSize != 0);
		btnDeleteConstraint.setEnabled(listSize != 0);
		btnSolve.setEnabled(listSize >= 2);
	}
	
	private void closeFrame(java.awt.AWTEvent evt) {
		if (Common.showConfirmDialog(this, "You really want to exit?", "Exit") == JOptionPane.YES_OPTION)
			System.exit(0);
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		lblSolution.setText("");
		
		switch (action) {
		
		case EDIT_FUNCTION:
			Common.showFrame(new FunctionDialog());
			lblFunction.setText( objectiveFunction.toString() );
			break;
		
		case ADD_CONSTRAINT:
			Common.showFrame(new ConstraintDialog());
			
			if (constraintFromDialog != null) {
				listModel.addElement(constraintFromDialog);
				listConstraints.setSelectedIndex(listModel.getSize()-1);
				checkButtons();
			}
        	
			break;
			
		case EDIT_CONSTRAINT:
			Constraint selectedConstraint = listModel.get(listConstraints.getSelectedIndex());
			Common.showFrame(new ConstraintDialog(selectedConstraint));
			
			if (constraintFromDialog != null)
				listModel.set(listConstraints.getSelectedIndex(), constraintFromDialog);
        	
			break;
        	
		case DELETE_CONSTRAINT:
			selectedConstraint = listModel.get(listConstraints.getSelectedIndex());
			
			if (Common.showConfirmDialog(this, "You really want to delete constraint: " + selectedConstraint.toString() + "?", "Delete") == JOptionPane.YES_OPTION) {
				listModel.removeElementAt(listConstraints.getSelectedIndex());
				if (listModel.getSize() != 0)
					listConstraints.setSelectedIndex(0);
				checkButtons();
			}
        	
			break;
        	
        case DRAW_CONSTRAINT:
        	selectedConstraint = listModel.get(listConstraints.getSelectedIndex());
        	new DrawConstraint(selectedConstraint);
        	break;
        	
        case SOLVE:
        	Calculating.solve(listModel);
            break;
            
        case EXIT:
        	closeFrame(e);
            break;
		}
	}
}
