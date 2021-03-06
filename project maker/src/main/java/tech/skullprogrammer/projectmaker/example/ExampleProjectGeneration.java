package tech.skullprogrammer.projectmaker.example;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JPackage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import tech.skullprogrammer.projectmaker.error.ExportException;
import tech.skullprogrammer.projectmaker.model.Configuration;
import tech.skullprogrammer.projectmaker.model.fm.FMDataModels;
import tech.skullprogrammer.projectmaker.model.operator.FMDataModelGenerator;
import tech.skullprogrammer.projectmaker.model.operator.FMEntityGenerator;
import tech.skullprogrammer.projectmaker.model.operator.JModelCleaner;
import tech.skullprogrammer.projectmaker.model.operator.OperatorException;
import tech.skullprogrammer.projectmaker.model.operator.PojoExporter;
import tech.skullprogrammer.projectmaker.model.operator.TemplateExporter;
import tech.skullprogrammer.projectmaker.proxy.ComponentFactory;
import tech.skullprogrammer.projectmaker.utility.Utility;

public class ExampleProjectGeneration {

    public static void main(String[] args) throws OperatorException, IOException, ExportException {
        new ExampleProjectGeneration().execute();
    }

    private void execute() throws OperatorException, IOException, ExportException {

//        String configFilename = "/Temp/Development/project maker/src/main/resources/config.properties";
//        String configDBFilename = "/Temp/Development/project-maker/project maker/src/main/resources/config_db.properties";
//        ComponentFactory.getInstance().configurePaths(configFilename, configDBFilename);
        Configuration configuration = ComponentFactory.getInstance().getConfigurationProxy().getConfiguration();
        
        Path templatePath = Paths.get(configuration.getTemplatePath());
        Path outputPath = Paths.get(configuration.getOutputPath());
//        Utility.unzipFolderZip4j(templatePath, outputPath, configuration.getTemplateOutputName(), true);
        Utility.unzipFolder(templatePath, outputPath, configuration.getTemplateOutputName(), true);
        PojoExporter pojoExporter = new PojoExporter();
        TemplateExporter templateExporter = new TemplateExporter();
        String modelPackage = Utility.createPackage(configuration.getPackageRootName(), configuration.getPackageModelName());
        Path projectFolder = Paths.get(configuration.getOutputPath(), configuration.getTemplateOutputName());
        Path sourceFolder = projectFolder.resolve("src").resolve("main").resolve("java");
        Path resourcesFolder = projectFolder.resolve("src").resolve("main").resolve("resources");
        Path webFolder = projectFolder.resolve("src").resolve("main").resolve("webapp").resolve("WEB-INF");
        String persistencePackage = Utility.createPackage(configuration.getPackageRootName(), configuration.getPackagePersistenceName());
        Path persistenceFolder = Utility.fromPackageToPath(persistencePackage);
//        pojoExporter.exportFromJsonToPojoFile(configuration.getJsonFolderPath(), modelPackage, sourceFolder.toString());
        List<JCodeModel> models = pojoExporter.exportFromJsonToCodeModel(configuration.getJsonFolderPath(), modelPackage);
        System.out.println("Creation Completed!");
        FMEntityGenerator fmEntityGenerator = new FMEntityGenerator();
        FMDataModelGenerator fmDataModelGenerator = new FMDataModelGenerator();
        JModelCleaner jModelCleaner = new JModelCleaner();
//        fmGenerator.generatesEntitiesFromCodeModel(models);
        Map<String, Pair<JDefinedClass, JPackage>> cleanedStructure = jModelCleaner.createStructureWithNoDuplicatesAndClean(models);
        System.out.println("Creation of JCodeModel Structure with no duplicates - Completed!");
        fmEntityGenerator.modifyModelForHibernate(cleanedStructure);
        System.out.println("Modification CodeModels - Completed!");
        ArrayList<String> managedEntities = new ArrayList<String>(cleanedStructure.keySet());
        FMDataModels fmDataModel = fmDataModelGenerator.generateDAODataModels(models, configuration, managedEntities);
        System.out.println("Generation DAOs DataModels - Completed!");
        pojoExporter.exportJCodeModelToFile(models, sourceFolder.toString());
        System.out.println("Export Entities - Completed!");
        templateExporter.exportFMSettingsGradleTemplateToFile(configuration.getTemplateOutputName(), projectFolder);
        System.out.println("Export settings.gradle - Completed!");
        templateExporter.exportFMDAOTemplateToFile(fmDataModel, projectFolder, sourceFolder, resourcesFolder, persistenceFolder);
        System.out.println("Export DAOs - Completed!");
//        Map<String, Map<String, Object>> dtoDataModels = fmDataModelGenerator.generateDTODataModels(models, new ArrayList<String>(cleanedStructure.keySet()), configuration);
//        System.out.println("Generation DTOs DataModels - Completed!");
        templateExporter.exportFMDTOTemplateToFile(fmDataModel, sourceFolder);
        System.out.println("Export DTOs - Completed!");
        templateExporter.exportFMEndpointTemplateToFile(fmDataModel, sourceFolder);
        System.out.println("Export Endpoint - Completed!");
        templateExporter.exportFMRouterTemplateToFile(fmDataModel, sourceFolder);
        System.out.println("Export Router - Completed!");
        templateExporter.exportFMWebTemplateToFile(fmDataModel, webFolder);
        System.out.println("Export web.xml - Completed!");

    }
}
