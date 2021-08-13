package tech.skullprogrammer.projectmaker.proxy;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.skullprogrammer.projectmaker.model.Configuration;
import tech.skullprogrammer.projectmaker.model.ConfigurationDB;
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
            String packagePersistenceName = proprieta.getProperty("package.persistence.name");
            String endpointPackageName = proprieta.getProperty("package.endpoint.name");
            String jsonFolderPath = proprieta.getProperty("json.folder.path");
            String templatePath = proprieta.getProperty("template.path");
            String templateOutputName = proprieta.getProperty("template.output.name");
            String outputPath = proprieta.getProperty("output.path");
            Boolean daoParametrized = Boolean.parseBoolean(proprieta.getProperty("datamodel.dao.parametrized"));
            String extensionName = proprieta.getProperty("datamodel.dao.extension.name");
            String extensionPackage = proprieta.getProperty("datamodel.dao.extension.package");
            Boolean daoInterfaceParametrized = Boolean.parseBoolean(proprieta.getProperty("datamodel.dao.interface.parametrized"));
            String daoInterfaceExtensionName = proprieta.getProperty("datamodel.dao.interface.extension.name");
            String daoInterfaceExtensionPackage = proprieta.getProperty("datamodel.dao.interface.extension.package");
            String daoExceptionName = proprieta.getProperty("datamodel.dao.exception.name");
            String daoExceptionPackage = proprieta.getProperty("datamodel.dao.exception.package");
            Boolean dbProject = Boolean.parseBoolean(proprieta.getProperty("db.project"));
            String dtoPackage = proprieta.getProperty("datamodel.dto.package");
            ConfigurationDB configurationDB = dbProject ? ComponentFactory.getInstance().getConfigurationDBProxy().getConfiguration() : null;
            Configuration config = new Configuration(packageRootName, packageModelName, packagePersistenceName, templatePath, templateOutputName,
                    outputPath, jsonFolderPath, daoParametrized, extensionName, extensionPackage, daoInterfaceParametrized, daoInterfaceExtensionName,
                    daoInterfaceExtensionPackage, daoExceptionName, daoExceptionPackage, dtoPackage, endpointPackageName,
                    configurationDB);
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
