package controller;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tradingJournal.AppConstants;
import utilities.GeneralUtility;
import view.MainView;

public class ImageAction extends AbstractAction{
	
	private int index;
	private MainView view;
	private String contextRootPath = GeneralUtility.getFirstPartOfSavePath(true);
	private String pathToSpecificSave = GeneralUtility.getSecondPartOfSavePath(false);
	
	public ImageAction() {}

	public ImageAction(int index, MainView view) {
		super();
		this.index = index;
		this.view = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String targetDestDir = contextRootPath + pathToSpecificSave;
		if(e.getSource().getClass() == JButton.class) {
			JButton button = (JButton)e.getSource();
			long currentTime = System.currentTimeMillis();
			long maxAge = getMaxAge(currentTime);
			
			if(button.getName().equalsIgnoreCase(AppConstants.SWEEP_BUTTON)) {
				Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
				String listOfPaths = prefs.get(AppConstants.PREF_SWEEP_LOCATIONS, "");
				listOfPaths = listOfPaths.replaceAll(System.lineSeparator(), ";");
				
				String fileTypeString = prefs.get(AppConstants.PREF_FILE_TYPES, "none");
				String[] fileTypes = fileTypeString.split(",");
				Map<String, String>fileTypeMap = new HashMap<>();
				for(String type : fileTypes) {
					fileTypeMap.put(type.toUpperCase(), type.toUpperCase());
				}
				
				String[] paths = listOfPaths.split(";");
				for(String path : paths) {
					path = path.trim();
					path = path.strip();
					if(path.isBlank() || path == null) {
						continue;
					}
					File file = new File(path);
					File[] files = file.listFiles();
					for(File f : files) {
						try {
							BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
							long fileCreationTime = attr.creationTime().toMillis();
							if((currentTime - fileCreationTime) > maxAge) {
								//System.out.println("File is too old, not saving it. ");
							}else {
								//Copy the file to the correct location (save it)
								if(fileTypeMap.containsKey(getFileExtension(f))) {
									Path target = f.toPath();
									String destination = targetDestDir + File.separator +"log-" + (index + 1)+ "-resources" + File.separator + f.getName();
									File destinationFile = new File(destination);
									Path destinationPath = destinationFile.toPath();
									Files.createDirectories(destinationPath);
									Files.copy(target, destinationPath,
							                 java.nio.file.StandardCopyOption.REPLACE_EXISTING,
							                 java.nio.file.StandardCopyOption.COPY_ATTRIBUTES,
							                 java.nio.file.LinkOption.NOFOLLOW_LINKS);
									f.delete();
								}else {
									System.out.println("wrongFileType");
								}

							}
						}catch(Exception exception) {
							exception.printStackTrace();
						}
					}
				}
			}else if(button.getName().equalsIgnoreCase(AppConstants.CAPTURE_BUTTON)) {
				takeScreenCapture(targetDestDir); 				
			}
		}		
	}

	private void takeScreenCapture(String targetDestDir) {
		JFrame mainFrame = new JFrame();
		mainFrame.setTitle("File Name");
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(true);
		
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
		panel.setLayout(layout);
		
		JLabel label = new JLabel("File Name");
		JTextField fileNameField = new JTextField("Name");
		JButton button = new JButton("OK");
		button.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	handleScreenCapture(fileNameField, mainFrame, targetDestDir);
		    }
		});
		
		
		panel.add(label);
		panel.add(fileNameField);
		panel.add(button);
		
		mainFrame.add(panel);
		
		mainFrame.setPreferredSize(new Dimension(500, 100));
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				handleScreenCapture(fileNameField, mainFrame, targetDestDir);
			}
		});
	}
	
	private void handleScreenCapture(JTextField fileNameField, JFrame mainFrame, String targetDestDir) {
		String fileCaptureName = fileNameField.getText();

		mainFrame.dispose();
		try {
			JFrame applicationFrame = view.getMainFrame();
			applicationFrame.setVisible(false);
			Thread.sleep(250);
			String destination = targetDestDir + File.separator +"log-" + (index + 1) + "-resources";
			File destinationFile = new File(destination);
			Path destinationPath = destinationFile.toPath();
			Files.createDirectories(destinationPath);
			
			BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(image, "png", new File(destination + File.separator + fileCaptureName +".png"));
			applicationFrame.setVisible(true);
		} catch (Exception exception) {
			exception.printStackTrace();
			//TODO better error handling. Should write to file. 
		}	
	}

	private String getFileExtension(File f) {
		String name = f.getName();
		if(name.contains(".")) {
			return name.substring(name.lastIndexOf(".") + 1).toUpperCase();
		}
		return "NoExtensionFound";
	}

	private long getMaxAge(long currentTime) {
		Long result = Long.MAX_VALUE;//set it high so if the stored value is 0 we get all files. 
		//if fileTime - currentTime is > result. File is too old. 
		Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
		int age = prefs.getInt(AppConstants.PREF_FILE_AGE, 0);
		if(age < 1) {
			return result;
		}
		
		return (age * 60000);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public MainView getView() {
		return view;
	}

	public void setView(MainView view) {
		this.view = view;
	}

}
