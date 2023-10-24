package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
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
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationFactory;

import controller.ImageAction;
import logging.CustomConfigurationFactory;
import tradingJournal.AppConstants;

public class MainView {
	
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JPanel controlPanel;
	private JPanel entryPanel;
	private JScrollPane scrollPane;
	private JButton addEntryButton;
	private JMenuBar menuBar;
	private List<JTextArea> textAreas = new ArrayList<>(5);
	
	private SimpleDateFormat sdf;	

	public MainView() {
		sdf = new SimpleDateFormat("HH:mm:ss z");
		sdf.setTimeZone(Calendar.getInstance().getTimeZone());
		
		mainFrame = new JFrame();
		mainFrame.setTitle("Trading Journal");
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(true);
		
		mainPanel = new JPanel(new BorderLayout());
		
		controlPanel = new JPanel();
		addEntryButton = new JButton("Add Entry");
		controlPanel.add(addEntryButton);
		mainPanel.add(controlPanel, BorderLayout.PAGE_START);
		
		entryPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(entryPanel, BoxLayout.Y_AXIS);
//		BoxLayout boxLayout = new BoxLayout(entryPanel, BoxLayout.X_AXIS);
		entryPanel.setLayout(boxLayout);
		
		scrollPane = new JScrollPane(entryPanel,
		         JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16); //speed up the scroll speed. 
		
		addMenu();
		
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainFrame.add(mainPanel);
		
		Logger logger = LogManager.getRootLogger();
		logger.error("Test log does this work and where is it save?");
	}
	
	private void addMenu() {
		menuBar = new JMenuBar();
		JMenu menu = new JMenu(AppConstants.FILE_MENU);
		menu.setName(AppConstants.FILE_MENU);
		
		JMenuItem save = new JMenuItem(AppConstants.MENU_SAVE);
		save.setName(AppConstants.MENU_SAVE);
		menu.add(save);
		
		JMenuItem saveLocation = new JMenuItem(AppConstants.MENU_SAVE_LOCATION);
		saveLocation.setName(AppConstants.MENU_SAVE_LOCATION);
		menu.add(saveLocation);
		
		JMenuItem loadDay = new JMenuItem(AppConstants.MENU_LOAD_DAY);
		loadDay.setName(AppConstants.MENU_LOAD_DAY);
		menu.add(loadDay);
		
		JMenuItem sweepLocation = new JMenuItem(AppConstants.MENU_SWEEP_LOCATION);
		sweepLocation.setName(AppConstants.MENU_SWEEP_LOCATION);
		menu.add(sweepLocation);
		
		JMenu reportMenu = new JMenu(AppConstants.REPORT_MENU);
		reportMenu.setName(AppConstants.REPORT_MENU);
		
		JMenuItem dateRange = new JMenuItem(AppConstants.MENU_DATE_RANGE_REPORT);
		dateRange.setName(AppConstants.MENU_DATE_RANGE_REPORT);
		reportMenu.add(dateRange);
		
		JMenuItem numberOfDays = new JMenuItem(AppConstants.MENU_NUMBER_OF_DAYS_REPORT);
		numberOfDays.setName(AppConstants.MENU_NUMBER_OF_DAYS_REPORT);
		reportMenu.add(numberOfDays);
		
		JMenuItem reportSaveLocation = new JMenuItem(AppConstants.MENU_REPORT_SAVE_LOCATION);
		reportSaveLocation.setName(AppConstants.MENU_REPORT_SAVE_LOCATION);
		reportMenu.add(reportSaveLocation);
		
		menuBar.add(menu);
		menuBar.add(reportMenu);
		mainFrame.setJMenuBar(menuBar);
	}
	
	public JMenuBar getMenuBar() {
		return menuBar;
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}
	
	public JButton getAddEntryButton() {
		return addEntryButton;
	}
	
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	public JPanel getEntryPanel() {
		return entryPanel;
	}
	
	public void addTextArea(String defaultText) {		
		Dimension size = mainFrame.getSize();
		double height = size.getHeight();
		double width = size.getWidth();
		
		Double maxHeight = height * 0.3;
		Double maxWidth = width - (width * 0.2);
		
		JTextArea textArea = new JTextArea();
		textArea.setMaximumSize(new Dimension(maxWidth.intValue(), maxHeight.intValue()));
		textArea.setPreferredSize(new Dimension(maxHeight.intValue(), maxWidth.intValue()));
		textArea.setLineWrap( true );
		textArea.setWrapStyleWord( true );
		
		textArea.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            	textArea.insert(sdf.format(timestamp), textArea.getCaretPosition());
            	setTimezoneEastern();
            	textArea.insert(" (" + sdf.format(timestamp) + ") ", textArea.getCaretPosition());
            	setTimezoneSystem();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_T,KeyEvent.CTRL_DOWN_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW );
		
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		textArea.setBorder(BorderFactory.createCompoundBorder(border,
	            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		addPadding(textArea);
		
		if(defaultText != null) {
			textArea.insert(defaultText, 0);
		}		
		
		JScrollPane textScrollPane = new JScrollPane(textArea);
		textScrollPane.setMaximumSize(new Dimension(maxWidth.intValue(), maxHeight.intValue()));
		textScrollPane.setPreferredSize(new Dimension(maxHeight.intValue(), maxWidth.intValue()));
		
		textAreas.add(textArea);
		
		JPanel instancePanel = new JPanel();
		BoxLayout bl = new BoxLayout(instancePanel, BoxLayout.X_AXIS);
		instancePanel.setLayout(bl);
		instancePanel.add(textScrollPane);
		
		JPanel buttonPanel = new JPanel();
		BoxLayout buttonLayout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
		buttonPanel.setLayout(buttonLayout);
		ImageAction buttonAction = new ImageAction(textAreas.size() -1, this);
		JButton capture = new JButton("Capture");
		capture.setName(AppConstants.CAPTURE_BUTTON);
		JButton sweep = new JButton("Sweep");
		sweep.setName(AppConstants.SWEEP_BUTTON);
		capture.setAction(buttonAction);
		capture.setText("Capture");
		sweep.setAction(buttonAction);
		sweep.setText("Sweep");
		buttonPanel.add(capture);
		buttonPanel.add(sweep);
		instancePanel.add(buttonPanel);
		
		entryPanel.add(instancePanel);
//		entryPanel.add(textScrollPane);
		mainFrame.revalidate();
	}
	
	public void clearTextAreas() {
		textAreas = new ArrayList<>();
		entryPanel.removeAll();
	}
	
	public List<JTextArea> getTextAreas(){
		return textAreas;
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
	
	private void setTimezoneEastern() {
		sdf.setTimeZone(TimeZone.getTimeZone("America/New_York")); 		
	}
	
	private void setTimezoneSystem() {
		sdf.setTimeZone(Calendar.getInstance().getTimeZone());
	}
}
