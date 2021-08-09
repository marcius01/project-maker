package tech.skullprogrammer.projectmaker.proxy;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.skullprogrammer.projectmaker.model.ConfigurationDB;
import tech.skullprogrammer.projectmaker.persistence.DAOException;
import tech.skullprogrammer.projectmaker.utility.Utility;

public class ConfigurationDBProxy {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationDBProxy.class);

    private final String configurationFilename;
    private Date lastFileConsultationDate = new Date(0);
    private ConfigurationDB configuration;

    public ConfigurationDBProxy(String configurationFilename) {
        this.configurationFilename = configurationFilename;
    }

    public void checkUpdatesInFile() {
        Date lastFileUpdateDate = Utility.checkUpdates(configurationFilename, lastFileConsultationDate);
        if (lastFileUpdateDate != null) {
            logger.info("Loading Configuration: {}", configurationFilename);
            this.configuration = loadConfiguration(configurationFilename);
            lastFileConsultationDate = lastFileUpdateDate;
        }
    }

    public String getConfigurationFilename() {
        return configurationFilename;
    }

    public Date getLastFileConsultationDate() {
        return lastFileConsultationDate;
    }

    public ConfigurationDB getConfiguration() {
        checkUpdatesInFile();
        return configuration;
    }

    private ConfigurationDB loadConfiguration(String fileName) {
        InputStream stream = null;
        stream = this.getClass().getResourceAsStream(fileName);
        if (stream == null) {
            throw new DAOException("Impossible to load configuration file");
        }
        try {
            stream = this.getClass().getResourceAsStream(fileName);
            Properties proprieta = new Properties();
            proprieta.load(stream);
            String dbType = proprieta.getProperty("db.type");
            String dbHost = proprieta.getProperty("db.host");
            String dbPort = proprieta.getProperty("db.port");
            String dbTmpDb = proprieta.getProperty("db.tmpDb");
            String dbDriver = proprieta.getProperty("db.driver");
            String dbSchemaGeneratorClass = proprieta.getProperty("db.schemaGeneratorClass");
            String dbName = proprieta.getProperty("db.name");
            String dbUsername = proprieta.getProperty("db.username");
            String dbPassword = proprieta.getProperty("db.password");
            String dbDialect = proprieta.getProperty("db.dialect");
            ConfigurationDB config = new ConfigurationDB(dbType, dbHost, dbPort, dbTmpDb, dbDriver, dbSchemaGeneratorClass,
                    dbName, dbUsername, dbPassword, dbDialect); ;
            logger.info("Configuration DB loaded: {}", config);
            return config;
        } catch (Exception e) {
            logger.error(e.toString());
            throw new DAOException("Impossible to load configuration file " + e.getLocalizedMessage());
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
