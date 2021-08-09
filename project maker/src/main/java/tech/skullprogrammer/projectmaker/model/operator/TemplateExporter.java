package tech.skullprogrammer.projectmaker.model.operator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.skullprogrammer.projectmaker.Constants;
import tech.skullprogrammer.projectmaker.error.ExportException;
import tech.skullprogrammer.projectmaker.model.fm.DAOClass;
import tech.skullprogrammer.projectmaker.model.fm.DAODataModels;
import tech.skullprogrammer.projectmaker.utility.Utility;

public class TemplateExporter {

    private Configuration cfg;
    private static Logger logger = LoggerFactory.getLogger(TemplateExporter.class);

    public TemplateExporter() throws ExportException {
        try {
            cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setDirectoryForTemplateLoading(new Utility().getFileFromResource("templates"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
        } catch (URISyntaxException | IOException ex) {
            throw new ExportException(ex);
        }
    }

    public void exportFMDAOTemplateToFile(DAODataModels daoDataModel, Path sourceCodePath, Path resourcesPath, String persistencePackage) throws ExportException {
        Path packagePath = Utility.fromPackageToPath(persistencePackage);
        Path completePath = sourceCodePath.resolve(packagePath);
        try {
            for (String daoName : daoDataModel.getDaosRoots().keySet()) {
                Map<String, Object> root = daoDataModel.getDaosRoots().get(daoName);
                DAOClass daoClass = (DAOClass) root.get(Constants.DAO_DATA_MODEL);
                Files.createDirectories(completePath);
                writeTemplateDAO("DAOEntity.ftlh", completePath, daoClass.getName(), root);
                writeTemplateDAO("IDAOEntity.ftlh", completePath, daoClass.getInterfaceName(), root);
            }
            writeTemplateConfiguration(daoDataModel.getConfigurationRoot(), resourcesPath);
        } catch (IOException | TemplateException ex) {
            throw new ExportException(ex);
        }
    }

    private void writeTemplateDAO(String templateName, Path completePath, String elementName, Map<String, Object> root) throws TemplateException, IOException {
        if (elementName == null || elementName.isEmpty()) {
            logger.debug("No name for element. Skip operation, don't write template");
        }
        Template template = cfg.getTemplate(templateName);
        Path elementFilePath = completePath.resolve(elementName + ".java");
        Writer out = Files.newBufferedWriter(elementFilePath, Charset.forName("UTF-8"));
        //Writer out = new OutputStreamWriter(System.out);
        template.process(root, out);
        logger.debug("Created file for template: {}", elementFilePath.toAbsolutePath());
    }

    private void writeTemplateConfiguration(Map<String, Object> root, Path resourcesPath) throws TemplateException, IOException {
        Template template = cfg.getTemplate("hibernate.cfg.ftlh");
        Path elementFilePath = resourcesPath.resolve("hibernate.cfg.xml");
        Writer out = Files.newBufferedWriter(elementFilePath, Charset.forName("UTF-8"));      
        template.process(root, out);
        logger.debug("Created file for template: {}", elementFilePath.toAbsolutePath());
    }
}
