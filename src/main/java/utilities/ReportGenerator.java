package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import controller.GenereateNumberOfDaysReportController;
import models.FileTags;
import models.TagData;
import tradingJournal.AppConstants;

public class ReportGenerator {

	private static final Logger logger = LogManager.getLogger(ReportGenerator.class);
	
	public void createReport(Set<File> filesToAddToReport) {
		StringBuilder builder = new StringBuilder();
		builder.append("<html><head><title>Report</title> <script type=\"module\" src=\"https://md-block.verou.me/md-block.js\"></script>");
		builder.append("<style> .log{ margin: auto;border: 1px solid #ccc!important;border-radius: 16px;padding-left: 10px;} </style>");
		builder.append("</head><body>");
		for(File file : filesToAddToReport) {
			if(file.isFile()) {
				builder.append("<div class=\"log\"> <p style='font-size: 10'>" + file.getAbsolutePath() + "</p> <md-block>");
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					StringBuilder fileBuilder = new StringBuilder();
					String st;
					while ((st = br.readLine()) != null) {
						fileBuilder.append(st);
						fileBuilder.append(System.lineSeparator());
					}
					
					builder.append(fileBuilder.toString());
					builder.append("</md-block>");
					processImages(file, builder);
					builder.append("</div>");
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
		
		Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
		String listOfPaths = prefs.get(AppConstants.PREF_REPORT_LOCATIONS, "");
		listOfPaths = listOfPaths.replaceAll(System.lineSeparator(), ";");
		String[] paths = listOfPaths.split(";");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(System.currentTimeMillis()));
		String year = cal.get(Calendar.YEAR) + "";
		String month = GeneralUtility.getNameOfMonth(cal);
		String day = cal.get(Calendar.DAY_OF_MONTH) + "";
		String filename = month + "-" + day + "-" + year + "-" + System.currentTimeMillis() + ".html";
		try {
			for(String path : paths) {
				BufferedWriter writer = null;
				try {
					File file = new File(path + File.separator + filename);
					file.createNewFile();
					writer = new BufferedWriter(new FileWriter(file));
					writer.write(builder.toString());
	
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (writer != null) {
						writer.close();
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("error generating report", e);
		}
	}
	
	private void processImages(File file, StringBuilder builder) {
		String fileName = file.getAbsolutePath();
		fileName = fileName.substring(0, fileName.length() -3) + "-resources";
		File resources = new File(fileName);
		File[] images = resources.listFiles();
		
		if(images != null) {
			List<File> sortedListOfFiles = Arrays.asList(images);
			sortedListOfFiles.sort(new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					Date date1 = new Date(o1.lastModified());
					Date date2 = new Date(o2.lastModified());
					return (date1.compareTo(date2));
				}						
			});
			
			int index = 0; 
			for(File image : sortedListOfFiles) {
				if(index == 0) {
					builder.append("<table style='width:100%;'><tr>");
				}else if(index % 2 == 0) {
					builder.append("<tr/><tr>");
				}
				builder.append("<td style='border: 1px solid;' title='" + image.getName() + "'><img style='width:100%; display: block;' src='" + image.getAbsoluteFile() + "'><td/>");
				index++;
			}
			
			if(index > 0) {
				builder.append("<tr/></td></table>");
			}
		}		
	}

	public List<File> getFileList(String fullPath, String[] individualTags) {
		List<File>filesToAddToReport = new  ArrayList<>();
		if(individualTags != null && individualTags.length > 0) {
			TagData tagData = readAndProcessTagsFile(fullPath);
			if(tagData == null) {
				return null;
			}
			List<FileTags> tagList = tagData.getTags();
			for(FileTags fileTags : tagList) {
				List<String> currentTags = fileTags.getTags();
				for(String curTag : currentTags) {
					for(int k = 0; k<individualTags.length; k++) {
						if(individualTags[k].trim().equalsIgnoreCase(curTag.trim())) {
							File matchingLog = new File(fullPath + File.separator + fileTags.getFileName());
							filesToAddToReport.add(matchingLog);
						}
					}
				}
			}
		}else {
			File directory = new File(fullPath);
			File[] files = directory.listFiles();
			if(files != null) {
				for (File file : files) {
					if (file.getName().contains("log")) {
						if(file.isFile()) {
							filesToAddToReport.add(file);
						}					
					}
				}
			}
		}
		return filesToAddToReport;
	}
	
	private TagData readAndProcessTagsFile(String fullPath) {
		File file = new File(fullPath + File.separator + "tags.json");
		if(file.isFile()) {
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				StringBuilder builder = new StringBuilder();
				String st;
				while ((st = br.readLine()) != null) {
					builder.append(st);
					builder.append(System.lineSeparator());
				}
				
				ObjectMapper mapper = new ObjectMapper();
				TagData tagdata = mapper.readValue(builder.toString(), TagData.class);
				return tagdata;
				
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		return null;	
		
	}
}
