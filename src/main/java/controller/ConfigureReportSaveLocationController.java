package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import tradingJournal.AppConstants;
import view.ConfigureReportSaveLocation;
import view.SweepConfiguration;

public class ConfigureReportSaveLocationController {

	private ConfigureReportSaveLocation view;

	public ConfigureReportSaveLocationController() {
		ConfigureReportSaveLocation sweepConfig = new ConfigureReportSaveLocation();
		view = sweepConfig;
		addActionListners();
	}
	
	public void start() {
		JFrame frame = view.getMainFrame();
		frame.setPreferredSize(new Dimension(600, 500));
		
		Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
		String defaultPath = System.getProperty("user.home") + File.separator + AppConstants.TOP_LEVEL_DIRECTORY_NAME;
		String defaultText = prefs.get(AppConstants.PREF_REPORT_LOCATIONS, defaultPath);
		defaultText = defaultText.replaceAll(";", System.lineSeparator());
		view.getTextArea().setText(defaultText);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private void addActionListners() {
		JFrame frame = view.getMainFrame();
		JButton addEntryButton = view.getAddLocationButton();
		JTextArea textArea = view.getTextArea();
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				String text = textArea.getText();
				text = text.replaceAll(System.lineSeparator(), ";");
				
				Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
				prefs.put(AppConstants.PREF_REPORT_LOCATIONS,text);
				
				frame.dispose();
			}
		});
		
		addEntryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				// Open the save dialog
				int response = fileChooser.showSaveDialog(null);
				if (response == JFileChooser.APPROVE_OPTION) {
					textArea.append(System.lineSeparator() + fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
	}
	
	
}
