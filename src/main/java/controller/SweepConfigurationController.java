package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import tradingJournal.AppConstants;
import view.SweepConfiguration;

public class SweepConfigurationController {

	private SweepConfiguration view;

	public SweepConfigurationController() {
		SweepConfiguration sweepConfig = new SweepConfiguration();
		view = sweepConfig;
		addActionListners();
	}
	
	public void start() {
		JFrame frame = view.getMainFrame();
		frame.setPreferredSize(new Dimension(600, 500));
		
		Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
		String defaultText = prefs.get(AppConstants.PREF_SWEEP_LOCATIONS, "");
		defaultText = defaultText.replaceAll(";", System.lineSeparator());
		view.getTextArea().setText(defaultText);
		
		Integer fileAge = prefs.getInt(AppConstants.PREF_FILE_AGE, 0);
		String fileTypes = prefs.get(AppConstants.PREF_FILE_TYPES, "jpg,png,gif");
		
		view.getAgeField().setValue(fileAge);
		view.getFileTypes().setText(fileTypes);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private void addActionListners() {
		JFrame frame = view.getMainFrame();
		JButton addEntryButton = view.getAddLocationButton();
		JTextArea textArea = view.getTextArea();
		JFormattedTextField ageField = view.getAgeField();
		JTextField fileTypes = view.getFileTypes();
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				String text = textArea.getText();
				text = text.replaceAll(System.lineSeparator(), ";");
				
				Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
				prefs.put(AppConstants.PREF_SWEEP_LOCATIONS,text);
				
				prefs.put(AppConstants.PREF_FILE_AGE, ageField.getText());
				prefs.put(AppConstants.PREF_FILE_TYPES, fileTypes.getText());
				
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
