package main;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class Common {
	public static final String F_MIN = "--> min", F_MAX = "--> max", C_LOE = "<=", C_GOE = ">=", C_EQU = "=";
	public static double INF = 50;
	
	/**
	 * Show frame on the center of screen 
	 */
	public static void showFrame(Window frame) {
		int screenWidth = 0, screenHeight = 0;
		
		GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        for (GraphicsDevice graphicsDevice : screenDevices) {
            screenWidth = graphicsDevice.getDefaultConfiguration().getBounds().width;
            screenHeight = graphicsDevice.getDefaultConfiguration().getBounds().height;
        }
		
        Rectangle r = frame.getBounds();
		
		int frameWidth = r.width, frameHeight = r.height;
		int posX = (screenWidth - frameWidth) / 2;
		int posY = (screenHeight - frameHeight) / 2 - 40;
		
		frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
		frame.setBounds(posX, posY, r.width, r.height);
		
		frame.setVisible(true);
	}
	
	public static void makeFrame(JPanel panel, String title) {
		Rectangle r = panel.getBounds();
		
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(r.x, r.y, r.width, r.height);
		frame.add(panel);
		
		showFrame(frame);
	}
	
	public static int parseInt(String s) {
		int n = 0;
		try {
			n = Integer.parseInt(s);
		} catch (Exception e) {}
		return n;
	}
	
	public static double parseDouble(String s) {
		double d = 0;
		try {
			d = Double.parseDouble(s);
		} catch (Exception e) {}
		return d;
	}
	
	public static void showErrorMessage(Component cmp, String message) {
		JOptionPane.showMessageDialog(cmp, message, "Error!", JOptionPane.ERROR_MESSAGE);
	}
	
	public static int showConfirmDialog(Component cmp, String message, String title) {
		Object[] options = { "Yes", "No" };
        return JOptionPane.showOptionDialog(
        		cmp, message, title,
        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
        		null, options, options[1]
        );
	}
	
	public static double Round(double value, int digits) {
		return new BigDecimal(value).setScale(digits, RoundingMode.HALF_UP).doubleValue();
	}
	
	// for text fields with numeric values
	private static DocumentFilter getTextFilter(String filterType) {
		String bannedSymbols = filterType.toUpperCase().equals("DOUBLE") ? "[^0123456789.-]" : "[^0123456789]";
		
		return new DocumentFilter() {
		    @Override
		    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
		        string = string.replaceAll(bannedSymbols, "");
		        super.insertString(fb, offset, string, attr);
		    }

		    @Override
		    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		        text = text.replaceAll(bannedSymbols, "");
		        super.replace(fb, offset, length, text, attrs);
		    }
		};
		
	}
	
	public static void setRestrictions(JTextField field, String filterType) {
		((AbstractDocument) field.getDocument()).setDocumentFilter(getTextFilter(filterType));
	}
	
	public static String clip(double f) {
		f = Round(f,2);
		String s = String.valueOf(f);
		
		if (s.endsWith(".0"))
			s = s.substring(0, s.length()-2);
		
		return s;
	}
	
	public static String makeBinomialName(double a, double b, String dir) {
		String partX1 = "";
		String partX2 = "";
		
		if (a != 0) {
			String sign = (a<0) ? "-" : "";
			a = Math.abs(a);
			String partA = (a != 1) ? "" + clip(a) + " * " : "";
			partX1 = sign + partA + "x1";
		}
		
		if (b != 0) {
			String sign = (b<0) ? " - " : ((a==0) ? "" : " + "); 
			b = Math.abs(b);
			String partB = (b != 1) ? "" + clip(b) + " * " : "";
			partX2 = sign + partB + "x2";
		}
		
		return partX1 + partX2 + " " + dir;
	}
	
	public static String makeConstraintName(double a, double b, String dir, double c) {
		String sc = clip(c);
		//String sign = (c < 0) ? "-" : ""; 
		//sc = (Math.abs(c) == Common.INF) ? sign + "inf" : sc;
		return makeBinomialName(a, b, dir) + " " + sc;
	}

}
