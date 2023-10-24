package logging;

import java.net.URI;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;

@Plugin(name = "CustomConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(1)
public class CustomConfigurationFactory extends ConfigurationFactory {
	
//	//https://logging.apache.org/log4j/2.x/manual/customconfig.html
//	//https://www.baeldung.com/log4j2-programmatic-config
	//https://stackoverflow.com/questions/14862770/log4j2-assigning-file-appender-filename-at-runtime 
		//possible solution. but irritating you can't get this to work. 

    static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);
        builder.setStatusLevel(Level.ERROR);
        builder.add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL).
            addAttribute("level", Level.DEBUG));
        
        AppenderComponentBuilder file = builder.newAppender("log", "File"); 
        file.addAttribute("fileName", System.getProperty("user.home") + "/tradingJournalLog.log");
        builder.add(file);

		LayoutComponentBuilder standard = builder.newLayout("PatternLayout");
//		standard.addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable");
		standard.addAttribute("pattern", "%msg%n%throwable");

		file.add(standard);
		
		RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.DEBUG);
		rootLogger.add(builder.newAppenderRef("log"));
		builder.add(rootLogger);
		
		LoggerComponentBuilder logger = builder.newLogger("controller", Level.DEBUG);
		logger.add(builder.newAppenderRef("log"));
		logger.addAttribute("additivity", false);

		builder.add(logger);
        
        return builder.build();
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] {"*"};
    }
}