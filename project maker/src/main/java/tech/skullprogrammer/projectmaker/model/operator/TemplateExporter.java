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
import java.util.Collections;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.skullprogrammer.projectmaker.Constants;
import tech.skullprogrammer.projectmaker.error.ExportException;
import tech.skullprogrammer.projectmaker.model.fm.DAOClass;
import tech.skullprogrammer.projectmaker.model.fm.FMDataModels;
import tech.skullprogrammer.projectmaker.model.fm.IClass;
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
            cfg.setAutoEscapingPolicy(Configuration.DISABLE_AUTO_ESCAPING_POLICY);
        } catch (URISyntaxException | IOException ex) {
            throw new ExportException(ex);
        }
    }

    private void exportFMTemplateToFile(Map<String, Map<String, Object>> dataModel, Path sourceCodePath, String templateFile, String dataModelKey) throws ExportException {
        try {
            for (String dtoName : dataModel.keySet()) {
                Map<String, Object> root = dataModel.get(dtoName);
                IClass dtoClass = (IClass) root.get(dataModelKey);
                Path packagePath = Utility.fromPackageToPath(dtoClass.getPackageName());
                Path completePath = sourceCodePath.resolve(packagePath);
                Files.createDirectories(completePath);
                writeTemplateDAO(templateFile, completePath, dtoClass.getName(), root);
            }
        } catch (IOException | TemplateException ex) {
            throw new ExportException(ex);
        }
    }

    public void exportFMEndpointTemplateToFile(FMDataModels daoDataModel, Path sourceCodePath) throws ExportException {
        exportFMTemplateToFile(daoDataModel.getEndpoints(), sourceCodePath, "RestEndpoint_RESTLET_ProjectSpecific.ftlh", Constants.ENDPOINT_DATA_MODEL);
    }

    public void exportFMDTOTemplateToFile(FMDataModels daoDataModel, Path sourceCodePath) throws ExportException {
        exportFMTemplateToFile(daoDataModel.getEndpoints(), sourceCodePath, "DTO.ftlh", Constants.DTO_DATA_MODEL);
    }

    public void exportFMDAOTemplateToFile(FMDataModels daoDataModel, String projectName, Path projectPath, Path sourceCodePath, Path resourcesPath, Path persistencePath) throws ExportException {
        Path completePath = sourceCodePath.resolve(persistencePath);
        try {
            for (String daoName : daoDataModel.getDaosRoots().keySet()) {
                Map<String, Object> root = daoDataModel.getDaosRoots().get(daoName);
                DAOClass daoClass = (DAOClass) root.get(Constants.DAO_DATA_MODEL);
                Files.createDirectories(completePath);
                writeTemplateDAO("DAOEntity.ftlh", completePath, daoClass.getName(), root);
                writeTemplateDAO("IDAOEntity.ftlh", completePath, daoClass.getInterfaceName(), root);
            }
            writeTemplateConfiguration("hibernate.cfg.ftlh", "hibernate.cfg.xml", daoDataModel.getConfigurationRoot(), resourcesPath);
            writeTemplateConfiguration("db.properties.ftlh", "db.properties", daoDataModel.getConfigurationRoot(), projectPath);
            writeTemplate("settings.gradle.ftlh", "settings.gradle", Collections.singletonMap("projectName", projectName), projectPath);
        } catch (IOException | TemplateException ex) {
            throw new ExportException(ex);
        }
    }

    private void writeTemplateDAO(String templateName, Path completePath, String elementName, Map<String, Object> root) throws TemplateException, IOException {
        if (elementName == null || elementName.isEmpty()) {
            logger.debug("No name for element. Skip operation, don't write template");
        }
        writeTemplate(templateName, elementName + ".java", root, completePath);
    }

    private void writeTemplateConfiguration(String templateName, String fileName, Map<String, Object> root, Path resourcesPath) throws TemplateException, IOException {
        if (root.get(Constants.DB_DATA_MODEL) == null) {
            logger.debug("No db configuration. Skip operation, don't write template");
            return;
        }
        writeTemplate(templateName, fileName, root, resourcesPath);
    }

    private void writeTemplate(String templateName, String fileName, Map<String, Object> root, Path resourcesPath) throws IOException, TemplateException {
        Template template = cfg.getTemplate(templateName);
        Path elementFilePath = resourcesPath.resolve(fileName);
        //Writer out = new OutputStreamWriter(System.out);
        Writer out = Files.newBufferedWriter(elementFilePath, Charset.forName("UTF-8"));
        template.process(root, out);
        logger.debug("Created file for template: {}", elementFilePath.toAbsolutePath());
    }
}
