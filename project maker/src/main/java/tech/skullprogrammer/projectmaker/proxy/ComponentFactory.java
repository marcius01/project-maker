package tech.skullprogrammer.projectmaker.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentFactory {

    private static final Logger logger = LoggerFactory.getLogger(ComponentFactory.class);

    public static final String CONFIGURATION_FILENAME = "/config.properties";
    public static final String CONFIGURATION_DB_FILENAME = "/config_db.properties";

    private static final ComponentFactory singleton = new ComponentFactory();

    public static ComponentFactory getInstance() {
        return singleton;
    }

    private final ConfigurationProxy configurationProxy;
    private final ConfigurationDBProxy configurationDBProxy;

    private ComponentFactory() {        
        this.configurationProxy = new ConfigurationProxy(CONFIGURATION_FILENAME);
        this.configurationDBProxy = new ConfigurationDBProxy(CONFIGURATION_DB_FILENAME);
    }

    public ConfigurationProxy getConfigurationProxy() {
        return configurationProxy;
    }

    public ConfigurationDBProxy getConfigurationDBProxy() {
        return configurationDBProxy;
    }       

}
