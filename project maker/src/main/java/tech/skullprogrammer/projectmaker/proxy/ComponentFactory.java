package tech.skullprogrammer.projectmaker.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.skullprogrammer.projectmaker.proxy.ConfigurationProxy;

public class ComponentFactory {

    private static final Logger logger = LoggerFactory.getLogger(ComponentFactory.class);

    public static final String CONFIGURATION_FILENAME = "/config.properties";

    private static final ComponentFactory singleton = new ComponentFactory();

    public static ComponentFactory getInstance() {
        return singleton;
    }

    private final ConfigurationProxy configurationProxy;

    private ComponentFactory() {        
        this.configurationProxy = new ConfigurationProxy(CONFIGURATION_FILENAME);
    }

    public ConfigurationProxy getConfigurationProxy() {
        return configurationProxy;
    }

}
