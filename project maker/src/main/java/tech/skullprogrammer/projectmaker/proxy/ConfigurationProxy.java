package tech.skullprogrammer.projectmaker.proxy;


import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.skullprogrammer.projectmaker.model.Configuration;
import tech.skullprogrammer.projectmaker.persistence.DAOException;
import tech.skullprogrammer.projectmaker.utility.Utility;

public class ConfigurationProxy {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationProxy.class);

    private final String configurationFilename;
    private Date lastFileConsultationDate = new Date(0);
    private Configuration configuration;

    public ConfigurationProxy(String configurationFilename) {
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

    public Configuration getConfiguration() {
        checkUpdatesInFile();
        return configuration;
    }

    private Configuration loadConfiguration(String fileName) {
        InputStream stream = null;
        stream = this.getClass().getResourceAsStream(fileName);
        if (stream == null) {
            throw new DAOException("Impossible to load configuration file");
        }
        try {
            stream = this.getClass().getResourceAsStream(fileName);
            Properties proprieta = new Properties();
            proprieta.load(stream);
            String packageRootName = proprieta.getProperty("package.root.name");
            String packageModelName = proprieta.getProperty("package.model.name");
            String jsonFolderPath = proprieta.getProperty("json.folder.path");
            String templatePath = proprieta.getProperty("template.path");
            String templateOutputName = proprieta.getProperty("template.output.name");
            String outputPath = proprieta.getProperty("output.path");
            Configuration config = new Configuration(packageRootName, packageModelName, templatePath, templateOutputName, outputPath, jsonFolderPath);
            logger.info("Configuration loaded: {}", config);
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
