package tech.skullprogrammer.projectmaker;

import com.sun.codemodel.JCodeModel;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import tech.skullprogrammer.projectmaker.model.Configuration;
import tech.skullprogrammer.projectmaker.model.operator.FMEntityGenerator;
import tech.skullprogrammer.projectmaker.model.operator.OperatorException;
import tech.skullprogrammer.projectmaker.model.operator.PojoExporter;
import tech.skullprogrammer.projectmaker.proxy.ComponentFactory;
import tech.skullprogrammer.projectmaker.utility.Utility;

public class MainAll {

    public static void main(String[] args) throws OperatorException, IOException {
        new MainAll().execute();
    }

    private void execute() throws OperatorException, IOException {

        Configuration configuration = ComponentFactory.getInstance().getConfigurationProxy().getConfiguration();    
        Path templatePath = Paths.get(configuration.getTemplatePath());
        Path outputPath = Paths.get(configuration.getOutputPath());
        Utility.unzipFolderZip4j(templatePath, outputPath, configuration.getTemplateOutputName(), true);
        PojoExporter pojoExporter = new PojoExporter();
        String modelPackage = configuration.getPackageRootName() + "." + configuration.getPackageModelName();
        Path sourceFolder = Paths.get(configuration.getOutputPath(), configuration.getTemplateOutputName(), "src", "main", "java");
//        pojoExporter.exportFromJsonToPojoFile(configuration.getJsonFolderPath(), modelPackage, sourceFolder.toString());
        List<JCodeModel> models = pojoExporter.exportFromJsonToCodeModel(configuration.getJsonFolderPath(), modelPackage);
        System.out.println("Creation Completed!");
        FMEntityGenerator fmGenerator = new FMEntityGenerator();
//        fmGenerator.generatesEntitiesFromCodeModel(models);
        fmGenerator.modifyModelForHibernate(models);
        System.out.println("Modification Completed!");
        pojoExporter.exportJCodeModelToFile(models, sourceFolder.toString());
        System.out.println("Export Completed!");
    }
}
