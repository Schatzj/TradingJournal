package utilities;

import java.io.File;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

import tradingJournal.AppConstants;

public class GeneralUtility {
	
	public static String getNameOfMonth(Calendar cal) {
		return getMonthForInt(cal.get(Calendar.MONTH));
	}
	
	public static String getFullFileSavePath() {
//		Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
//		String saveLocation = prefs.get(AppConstants.PREF_SAVE_LOCATION, System.getProperty("user.home"));
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(new Date(System.currentTimeMillis()));
//		String year = cal.get(Calendar.YEAR) + "";
//		String month = GeneralUtility.getNameOfMonth(cal);
//		String day = cal.get(Calendar.DAY_OF_MONTH) + "";
//		String path = saveLocation + File.separator + AppConstants.TOP_LEVEL_DIRECTORY_NAME + File.separator + year
//				+ File.separator + month + File.separator + day;
//		return path;
		return getFirstPartOfSavePath(true) + getSecondPartOfSavePath(false);
	}
	
	public static String getFirstPartOfSavePath(boolean includEndingFileSeperator) {
		Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
		String saveLocation = prefs.get(AppConstants.PREF_SAVE_LOCATION, System.getProperty("user.home"));
		String path = saveLocation + File.separator + AppConstants.TOP_LEVEL_DIRECTORY_NAME;
		if(includEndingFileSeperator) {
			path = path + File.separator;
		}
		return path;
	}
	
	public static String getSecondPartOfSavePath(boolean includStartingFileSeperator) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(System.currentTimeMillis()));
		String year = cal.get(Calendar.YEAR) + "";
		String month = GeneralUtility.getNameOfMonth(cal);
		String day = cal.get(Calendar.DAY_OF_MONTH) + "";
		
		String path = "";
		if(includStartingFileSeperator) {
			path = File.separator;
		}

		path = path + File.separator + year + File.separator + month + File.separator + day;
		
		return path;
	}
	

	private static String getMonthForInt(int num) {
	    String month = "wrong";
	    DateFormatSymbols dfs = new DateFormatSymbols();
	    String[] months = dfs.getMonths();
	    if (num >= 0 && num <= 11) {
	        month = months[num];
	    }
	    return month;
	}
}
