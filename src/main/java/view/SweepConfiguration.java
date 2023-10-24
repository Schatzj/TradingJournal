package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import controller.ImageAction;
import tradingJournal.AppConstants;

public class SweepConfiguration {
	
	private JFrame mainFrame;
	private JButton addLocationButton;
	private JTextArea textArea;
	private JFormattedTextField ageField;
	private JTextField fileTypes;
	
	public SweepConfiguration() {
		
		mainFrame = new JFrame();
		mainFrame.setTitle("Sweep locations");
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(true);
//		mainFrame.setPreferredSize(new Dimension(600, 500));
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
//		Dimension size = mainFrame.getSize();
		double height = 600;//size.getHeight();
		double width = 500;//size.getWidth();
		
		Double maxHeight = height * 0.3;
		Double maxWidth = width - (width * 0.2);
		
		textArea = new JTextArea();
		textArea.setMaximumSize(new Dimension(maxWidth.intValue(), maxHeight.intValue()));
		textArea.setPreferredSize(new Dimension(maxHeight.intValue(), maxWidth.intValue()));
		textArea.setLineWrap( true );
		textArea.setWrapStyleWord( true );
		
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		textArea.setBorder(BorderFactory.createCompoundBorder(border,
	            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		addPadding(textArea);	
		
		JScrollPane textScrollPane = new JScrollPane(textArea);
		textScrollPane.setMaximumSize(new Dimension(maxWidth.intValue(), maxHeight.intValue()));
		textScrollPane.setPreferredSize(new Dimension(maxHeight.intValue(), maxWidth.intValue()));
		
		addLocationButton = new JButton("Add Location");
		
		mainPanel.add(addLocationButton, BorderLayout.PAGE_END);
		
		JLabel lifespanLabel = new JLabel("Max age in minutes");
		NumberFormat longFormat = NumberFormat.getIntegerInstance();

		NumberFormatter numberFormatter = new NumberFormatter(longFormat);
		numberFormatter.setValueClass(Long.class); //optional, ensures you will always get a long value
		numberFormatter.setAllowsInvalid(false); //this is the key!!
		numberFormatter.setMinimum(0l); //Optional

		ageField = new JFormattedTextField(numberFormatter);
		JPanel agePanel = new JPanel();
		BoxLayout ageLayout = new BoxLayout(agePanel, BoxLayout.X_AXIS);
		agePanel.setLayout(ageLayout);
		agePanel.add(lifespanLabel);
		agePanel.add(ageField);
		
		fileTypes = new JTextField(3);
		JPanel fileTypesPanel = new JPanel();
		BoxLayout fileTypesLayout = new BoxLayout(fileTypesPanel, BoxLayout.X_AXIS);
		fileTypesPanel.setLayout(fileTypesLayout);
		fileTypesPanel.add(new JLabel("File Types:"));
		fileTypesPanel.add(fileTypes);
		
		JPanel configPanel = new JPanel();
		BoxLayout configLayout = new BoxLayout(configPanel, BoxLayout.Y_AXIS);
		configPanel.setLayout(configLayout);
		
		configPanel.add(agePanel);
		configPanel.add(fileTypesPanel);
		
		mainPanel.add(configPanel, BorderLayout.PAGE_START);
		mainPanel.add(textScrollPane, BorderLayout.CENTER);
		mainFrame.add(mainPanel);
	}

	public JFormattedTextField getAgeField() {
		return ageField;
	}

	public void setAgeField(JFormattedTextField ageField) {
		this.ageField = ageField;
	}

	public JTextField getFileTypes() {
		return fileTypes;
	}

	public void setFileTypes(JTextField fileTypes) {
		this.fileTypes = fileTypes;
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public JButton getAddLocationButton() {
		return addLocationButton;
	}

	public void setAddLocationButton(JButton addLocationButton) {
		this.addLocationButton = addLocationButton;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	private void addPadding(JTextArea textArea) {
		Border current = textArea.getBorder();
		Border empty = new EmptyBorder(5, 0, 5, 0); // top, left, bottom right
		if (current == null) {
			textArea.setBorder(empty);
		} else {
			textArea.setBorder(new CompoundBorder(empty, current));
		}
	}
	
}
