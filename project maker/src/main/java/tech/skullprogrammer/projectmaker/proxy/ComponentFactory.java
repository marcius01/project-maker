package tech.skullprogrammer.projectmaker.proxy;

import java.io.File;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.skullprogrammer.projectmaker.persistence.DAOException;

public class ComponentFactory {

    private static final Logger logger = LoggerFactory.getLogger(ComponentFactory.class);

    public static String CONFIGURATION_FILENAME = "/config.properties";
    public static String CONFIGURATION_DB_FILENAME = "/config_db.properties";

    private static final ComponentFactory singleton = new ComponentFactory();

    public static ComponentFactory getInstance() {
        return singleton;
    }

    private ConfigurationProxy configurationProxy;
    private ConfigurationDBProxy configurationDBProxy;

    public void configurePaths(String configurationFilename, String configurationDBFilename) {
        CONFIGURATION_FILENAME = configurationFilename;
        CONFIGURATION_DB_FILENAME = configurationDBFilename;
        this.configurationProxy = new ConfigurationProxy(CONFIGURATION_FILENAME);
        this.configurationDBProxy = new ConfigurationDBProxy(CONFIGURATION_DB_FILENAME);
    }

    private ComponentFactory() {
        try {
        File fileConfiguration =  new File (this.getClass().getResource(CONFIGURATION_FILENAME).toURI());
        File fileConfigurationDB =  new File (this.getClass().getResource(CONFIGURATION_DB_FILENAME).toURI());
        this.configurationProxy = new ConfigurationProxy(fileConfiguration.getAbsolutePath());
        this.configurationDBProxy = new ConfigurationDBProxy(fileConfigurationDB.getAbsolutePath());
        } catch (URISyntaxException e) {
            throw new DAOException("Impossible to load configuration file " + e.getLocalizedMessage());
        }
    }

    public ConfigurationProxy getConfigurationProxy() {
        return configurationProxy;
    }

    public ConfigurationDBProxy getConfigurationDBProxy() {
        return configurationDBProxy;
    }

}
