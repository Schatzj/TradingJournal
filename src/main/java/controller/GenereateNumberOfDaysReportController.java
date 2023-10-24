package controller;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import models.FileTags;
import models.TagData;
import tradingJournal.AppConstants;
import utilities.GeneralUtility;
import utilities.ReportGenerator;
import view.GenerateDateRangeReport;
import view.GenerateNumberOfdaysReport;
import view.SweepConfiguration;

public class GenereateNumberOfDaysReportController {

	private GenerateNumberOfdaysReport view;

	public GenereateNumberOfDaysReportController() {
		GenerateNumberOfdaysReport report = new GenerateNumberOfdaysReport();
		view = report;
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
		JButton genReportbutton = view.getGenerateReportButton();
		JFormattedTextField numOfDays = view.getNumberOfDays();
		JTextArea tagsInput = view.getTagsInput();
		
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				
				frame.dispose();
			}
		});
		
		genReportbutton.addActionListener(new ActionListener() {
			//render markdown: https://www.makeuseof.com/md-block-render-markdown-web-page/
			public void actionPerformed(ActionEvent e) {
				String numberOfDays = numOfDays.getText();
				if(numberOfDays == null || numberOfDays.isEmpty()) {
					numberOfDays = "1";
				}
				Integer numOfDays = Integer.parseInt(numberOfDays);
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
				
				String saveLocation = GeneralUtility.getFirstPartOfSavePath(false);
				ReportGenerator reportGen = new ReportGenerator();
				
				Calendar cal = Calendar.getInstance();
				Set<File>filesToAddToReport = new LinkedHashSet<File>();
				for(int i = 0; i<numOfDays; i++) {					
					String year = cal.get(Calendar.YEAR) + "";
					String month = GeneralUtility.getNameOfMonth(cal);
					String day = cal.get(Calendar.DAY_OF_MONTH) + "";
					String fullPath = saveLocation + File.separator + year + File.separator + month + File.separator + day;
					cal.add(Calendar.DAY_OF_MONTH, -1);
					
					List<File> addFiles = reportGen.getFileList(fullPath, individualTags);
					if(addFiles != null && addFiles.isEmpty() == false) {
						filesToAddToReport.addAll(addFiles);
					}		
				}
				
				
				reportGen.createReport(filesToAddToReport);
			}

		});
	}
	
	
	
	
}
