package tradingJournal;

import org.apache.logging.log4j.core.config.ConfigurationFactory;

import controller.MainController;
import logging.CustomConfigurationFactory;
import view.MainView;

public class Application {

    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        SwingUtilities.invokeLater(() -> {
//            new Application();
//        });
    	
    	//property/config files location: https://stackoverflow.com/questions/1570757/storing-a-file-in-the-users-directory-in-cross-platform-java
//    	Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
    	
    	ConfigurationFactory.setConfigurationFactory(new CustomConfigurationFactory());
    	
    	MainView view = new MainView();
    	MainController controller = new MainController(view);
    	controller.startMainView();
    }

}