package controller;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.FileTags;
import models.TagData;
import tradingJournal.AppConstants;
import utilities.GeneralUtility;
import view.GenerateNumberOfdaysReport;
import view.MainView;

public class MainController {

	private MainView view;
	private TagData tagData = new TagData();
	
	String contextRootPath = GeneralUtility.getFirstPartOfSavePath(true);
	String pathToSpecificSave = GeneralUtility.generateSecondPartOfSavePath(false);
	
	private static final Logger logger = LogManager.getLogger(MainController.class);

	public MainController(MainView view) {
		this.view = view;
	}

	public void startMainView() {
		JFrame frame = view.getMainFrame();
		frame.setPreferredSize(new Dimension(600, 500));
		frame.pack();
		frame.setVisible(true);
		
		String contextRootPath = GeneralUtility.getFirstPartOfSavePath(true);
		String pathToSpecificSave = GeneralUtility.generateSecondPartOfSavePath(false);
		
		loadEntriesForSpecificDay(contextRootPath + pathToSpecificSave);

		addActionListners();
	}

	private void addActionListners() {
		JFrame frame = view.getMainFrame();

		JButton addEntryButton = view.getAddEntryButton();
		addEntryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.addTextArea(null);
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				saveEntries();

				frame.dispose();
				System.exit(0);
			}
		});

		JMenuBar menuBar = view.getMenuBar();
		for(int i = 0; i< menuBar.getMenuCount(); i++) {
			JMenu menu = menuBar.getMenu(i);
			if (menu.getName().equalsIgnoreCase(AppConstants.FILE_MENU)) {
				Component[] comps = menu.getMenuComponents();
				for (Component comp : comps) {
					if (comp.getName().equalsIgnoreCase(AppConstants.MENU_SAVE_LOCATION)) {
						JMenuItem item = (JMenuItem) comp;
						item.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								JFileChooser fileChooser = new JFileChooser();
								fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

								// Open the save dialog
								int response = fileChooser.showSaveDialog(null);
								if (response == JFileChooser.APPROVE_OPTION) {
									Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
									prefs.put(AppConstants.PREF_SAVE_LOCATION,
											fileChooser.getSelectedFile().getAbsolutePath());
								}
							}
						});
					}
				}
			}

			if (menu.getName().equalsIgnoreCase(AppConstants.FILE_MENU)) {
				Component[] comps = menu.getMenuComponents();
				for (Component comp : comps) {
					if (comp.getName().equalsIgnoreCase(AppConstants.MENU_LOAD_DAY)) {
						JMenuItem item = (JMenuItem) comp;
						item.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								saveEntries();
								
								String path = GeneralUtility.getFullFileSavePath();
								JFileChooser fileChooser = new JFileChooser(path);
								fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

								int response = fileChooser.showSaveDialog(null);
								if (response == JFileChooser.APPROVE_OPTION) {
									loadEntriesForSpecificDay(fileChooser.getSelectedFile().getAbsolutePath());								
								}
							}
						});
					}
				}
			}
			
			if (menu.getName().equalsIgnoreCase(AppConstants.FILE_MENU)) {
				Component[] comps = menu.getMenuComponents();
				for (Component comp : comps) {
					if (comp.getName().equalsIgnoreCase(AppConstants.MENU_SAVE)) {
						JMenuItem item = (JMenuItem) comp;
						item.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								saveEntries();
							}
						});
					}
				}
			}
			
			if (menu.getName().equalsIgnoreCase(AppConstants.FILE_MENU)) {
				Component[] comps = menu.getMenuComponents();
				for (Component comp : comps) {
					if (comp.getName().equalsIgnoreCase(AppConstants.MENU_SWEEP_LOCATION)) {
						JMenuItem item = (JMenuItem) comp;
						item.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								SweepConfigurationController sc = new SweepConfigurationController();
								sc.start();
							}
						});
					}
				}
			}
			
			if (menu.getName().equalsIgnoreCase(AppConstants.REPORT_MENU)) {
				Component[] comps = menu.getMenuComponents();
				for (Component comp : comps) {
					if (comp.getName().equalsIgnoreCase(AppConstants.MENU_DATE_RANGE_REPORT)) {
						JMenuItem item = (JMenuItem) comp;
						item.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								GenereateDateRangeReportController reportController = new GenereateDateRangeReportController();
								reportController.start();
							}
						});
					}
				}
			}
			
			if (menu.getName().equalsIgnoreCase(AppConstants.REPORT_MENU)) {
				Component[] comps = menu.getMenuComponents();
				for (Component comp : comps) {
					if (comp.getName().equalsIgnoreCase(AppConstants.MENU_NUMBER_OF_DAYS_REPORT)) {
						JMenuItem item = (JMenuItem) comp;
						item.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								GenereateNumberOfDaysReportController reportController = new GenereateNumberOfDaysReportController();
								reportController.start();
							}
						});
					}
				}
			}
			
			if (menu.getName().equalsIgnoreCase(AppConstants.REPORT_MENU)) {
				Component[] comps = menu.getMenuComponents();
				for (Component comp : comps) {
					if (comp.getName().equalsIgnoreCase(AppConstants.MENU_REPORT_SAVE_LOCATION)) {
						JMenuItem item = (JMenuItem) comp;
						item.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								ConfigureReportSaveLocationController reportController = new ConfigureReportSaveLocationController();
								reportController.start();
							}
						});
					}
				}
			}
		}
		
	}
	
	private void saveEntries() {
		try {
			String path = contextRootPath + pathToSpecificSave;
			
			Path filePath = Paths.get(path);
			Files.createDirectories(filePath);
			int index = 1;

			List<JTextArea> textAreas = view.getTextAreas();
			for (JTextArea ta : textAreas) {
				String filename = "log-" + index + ".md";
				extractTags(ta.getText(), filename);
				BufferedWriter writer = null;
				try {
					if (ta.getText() == null || ta.getText().isBlank()) {
						continue;
					}
					File file = new File(path + File.separator + filename);
					file.createNewFile();
					writer = new BufferedWriter(new FileWriter(file));
					writer.write(ta.getText());

					index++;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (writer != null) {
						writer.close();
					}
				}
			}
			
			ObjectMapper objectMapper = new ObjectMapper();
			File file = new File(path + File.separator + AppConstants.TAG_FILENAME + ".json");
			objectMapper.writeValue(file, tagData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void extractTags(String text, String filename) {
		FileTags fileTags = new FileTags();
		fileTags.setFileName(filename);
		//when you get the matches. be sure to replace # with "", and toLower everything. 
		Pattern headerPattern = Pattern.compile("((#+\\s+).*)");
		Matcher headerMatcher = headerPattern.matcher(text);
		while(headerMatcher.find()) {
			String group = headerMatcher.group();
			group = group.replace("#", "");
			group = group.trim();
			if(group.isBlank()) {
				continue;
			}
			fileTags.addTag(group);
		}
		
		Pattern tagPattern = Pattern.compile("(#[^\\s]*)");
		Matcher tagMatcher = tagPattern.matcher(text);
		while(tagMatcher.find()) {
			String group = tagMatcher.group();
			group = group.replace("#", "");
			group = group.trim();
			if(group.isBlank()) {
				continue;
			}
			fileTags.addTag(group);
		}
		
		tagData.addFileTag(fileTags);
	}
	
	private void loadEntriesForSpecificDay(String path) {
//		String contextRootPath = GeneralUtility.getFirstPartOfSavePath(true);
//		String pathToSpecificSave = GeneralUtility.getSecondPartOfSavePath(false);
		
		view.clearTextAreas();
		File directory = new File(path);
		path = path.substring(path.lastIndexOf(AppConstants.TOP_LEVEL_DIRECTORY_NAME) + AppConstants.TOP_LEVEL_DIRECTORY_NAME.length() + 1);
		pathToSpecificSave = path;
		Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
		prefs.put(AppConstants.PATH_TO_CURRENT_ENTRIES, pathToSpecificSave);
		
		File[] files = directory.listFiles();
		if(files != null) {
			for (File file : files) {
				if (file.getName().contains("log")) {
					if(file.isFile()) {
						try (BufferedReader br = new BufferedReader(new FileReader(file))) {
							StringBuilder builder = new StringBuilder();
							String st;
							while ((st = br.readLine()) != null) {
								builder.append(st);
								builder.append(System.lineSeparator());
							}
							
							view.addTextArea(builder.toString());
							
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}					
				}
			}
		}		
	}

//	public void startMainView() {
//		SwingUtilities.invokeLater(view.getMainFrame());
//	}

}
