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

public class GenerateNumberOfdaysReport {
	
	private JFrame mainFrame;
	private JButton generateReportButton;
	private JTextArea tagsInput;
	private JFormattedTextField numberOfDays;
	
	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public JButton getGenerateReportButton() {
		return generateReportButton;
	}

	public void setGenerateReportButton(JButton generateReportButton) {
		this.generateReportButton = generateReportButton;
	}

	public JTextArea getTagsInput() {
		return tagsInput;
	}

	public void setTagsInput(JTextArea tagsInput) {
		this.tagsInput = tagsInput;
	}

	public GenerateNumberOfdaysReport() {
		
		mainFrame = new JFrame();
		mainFrame.setTitle("Date Range Report Generator");
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(true);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
//		Dimension size = mainFrame.getSize();
		double height = 600;//size.getHeight();
		double width = 500;//size.getWidth();
		
		Double maxHeight = height * 0.3;
		Double maxWidth = width - (width * 0.2);
		
		tagsInput = new JTextArea();
		tagsInput.setMaximumSize(new Dimension(maxWidth.intValue(), maxHeight.intValue()));
		tagsInput.setPreferredSize(new Dimension(maxHeight.intValue(), maxWidth.intValue()));
		tagsInput.setLineWrap( true );
		tagsInput.setWrapStyleWord( true );
		
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		tagsInput.setBorder(BorderFactory.createCompoundBorder(border,
	            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		addPadding(tagsInput);	
		
		JScrollPane textScrollPane = new JScrollPane(tagsInput);
		textScrollPane.setMaximumSize(new Dimension(maxWidth.intValue(), maxHeight.intValue()));
		textScrollPane.setPreferredSize(new Dimension(maxHeight.intValue(), maxWidth.intValue()));
		
		generateReportButton = new JButton("Generate Report");
		
		mainPanel.add(generateReportButton, BorderLayout.PAGE_END);
		
		JLabel startDateLabel = new JLabel("Number of days");
		NumberFormat longFormat = NumberFormat.getIntegerInstance();

		NumberFormatter numberFormatter = new NumberFormatter(longFormat);
		numberFormatter.setValueClass(Long.class); //optional, ensures you will always get a long value
		numberFormatter.setAllowsInvalid(false); //this is the key!!
		numberFormatter.setMinimum(0l); //Optional

		numberOfDays = new JFormattedTextField(numberFormatter);
		
		JPanel startDatePanel = new JPanel();
		BoxLayout startDateLayout = new BoxLayout(startDatePanel, BoxLayout.X_AXIS);
		startDatePanel.setLayout(startDateLayout);
		startDatePanel.add(startDateLabel);
		startDatePanel.add(numberOfDays);
		
		JPanel configPanel = new JPanel();
		BoxLayout configLayout = new BoxLayout(configPanel, BoxLayout.Y_AXIS);
		configPanel.setLayout(configLayout);
		
		configPanel.add(startDatePanel);
		configPanel.add(new JLabel("Tags (semi-colon [;] seperated list):"));
		
		mainPanel.add(configPanel, BorderLayout.PAGE_START);
		mainPanel.add(textScrollPane, BorderLayout.CENTER);
		mainFrame.add(mainPanel);
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

	public JFormattedTextField getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(JFormattedTextField numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	
}
