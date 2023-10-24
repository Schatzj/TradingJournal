package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tradingJournal.AppConstants;
import utilities.GeneralUtility;
import utilities.ReportGenerator;
import view.GenerateDateRangeReport;
import view.SweepConfiguration;

public class GenereateDateRangeReportController {

	private GenerateDateRangeReport view;
	private static final Logger logger = LogManager.getLogger(GenereateDateRangeReportController.class);
	
	public GenereateDateRangeReportController() {
		GenerateDateRangeReport dateRangeReport = new GenerateDateRangeReport();
		view = dateRangeReport;
		addActionListners();
	}
	
	public void start() {
		JFrame frame = view.getMainFrame();
		frame.setPreferredSize(new Dimension(600, 500));
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private void addActionListners() {
		JFrame frame = view.getMainFrame();
		JButton genReportButton = view.getGenerateReportButton();
		JTextArea tagsInput = view.getTagsInput();
		JTextField startDateField = view.getStartDate();
		JTextField endDateField = view.getEndDate();
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				
				frame.dispose();
			}
		});
		
		genReportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tags = tagsInput.getText();
				String[] individualTags = tags.split(";");
				if (individualTags.length < 1 && tags.length() > 1) {
					individualTags = new String[1];
					individualTags[0] = tags;
				}
				if(individualTags.length == 1) {
					if(individualTags[0] == null || individualTags[0].isBlank()) {
						individualTags = null;
					}
				}
				
				
				SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
				String startDateString = startDateField.getText();
				String endDateString = endDateField.getText();
				if(startDateString == null || startDateString.isBlank() || endDateString == null || endDateString.isBlank()) {
					return;
				}
				try {
					Date startDate = dateFormatter.parse(startDateString);
					Date endDate = dateFormatter.parse(endDateField.getText());
					
					String saveLocation = GeneralUtility.getFirstPartOfSavePath(false);
					ReportGenerator reportGen = new ReportGenerator();
					
					Calendar startCal = Calendar.getInstance();
					startCal.setTime(startDate);
					Calendar endCal = Calendar.getInstance();
					endCal.setTime(endDate);
					endCal.add(Calendar.DAY_OF_MONTH, 1);
					
					Set<File>filesToAddToReport = new LinkedHashSet<File>();
					while(startCal.before(endCal)) {	
//						System.out.println("startCal: " + startCal.get(Calendar.MONTH) + "/" + startCal.get(Calendar.DAY_OF_MONTH) + "/" + startCal.get(Calendar.YEAR));
//						System.out.println("endCal " + endCal.get(Calendar.MONTH) + "/" + endCal.get(Calendar.DAY_OF_MONTH) + "/" + endCal.get(Calendar.YEAR));
						String year = startCal.get(Calendar.YEAR) + "";
						String month = GeneralUtility.getNameOfMonth(startCal);
						String day = startCal.get(Calendar.DAY_OF_MONTH) + "";
						String fullPath = saveLocation + File.separator + year + File.separator + month + File.separator + day;
						startCal.add(Calendar.DAY_OF_MONTH, 1);
						
						List<File> addFiles = reportGen.getFileList(fullPath, individualTags);
						if(addFiles != null && addFiles.isEmpty() == false) {
							filesToAddToReport.addAll(addFiles);
						}		
					}
					
					reportGen.createReport(filesToAddToReport);
				}catch(Exception ex) {
					ex.printStackTrace();
					logger.error("Error getting dates for date Range report", ex);
				}				
			}
		});
	}
	
	
}
