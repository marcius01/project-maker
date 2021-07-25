package tech.skullprogrammer.projectmaker.model.operator;

import com.sun.codemodel.JCodeModel;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.NoopAnnotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.SourceType;
import org.jsonschema2pojo.rules.RuleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PojoExporter {
    
    private static Logger logger = LoggerFactory.getLogger(PojoExporter.class);

    public List<JCodeModel> exportFromJsonToCodeModel(String jsonFolder, String packageName) throws OperatorException {
        List<JCodeModel> result = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(jsonFolder))) {
            GenerationConfig config = defineConfiguration();

            List<String> paths = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".json")).collect(Collectors.toList());

            for (String path : paths) {
                JCodeModel codeModel = generateCodeModel(path, config, packageName);
                result.add(codeModel);
                logger.debug("JModel created for file: " + path);
            }
            return result;
        } catch (IOException e) {
            throw new OperatorException(e);
        }
    }

    public void exportFromJsonToPojoFile(String jsonFolder, String packageName, String outputFolder) throws OperatorException {
        List<JCodeModel> codeModels = exportFromJsonToCodeModel(jsonFolder, packageName);
        exportJCodeModelToFile(codeModels, outputFolder);
    }

    public void exportJCodeModelToFile(List<JCodeModel> codeModels, String outputFolder) throws OperatorException {
        try {
            for (JCodeModel codeModel : codeModels) {
                File toFile = new File(outputFolder);
                codeModel.build(toFile);
                logger.debug("Exported model with number of artifacts of: " + codeModel.countArtifacts());
            }
        } catch (IOException e) {
            throw new OperatorException(e);
        }
    }

    private JCodeModel generateCodeModel(String path, GenerationConfig config, String packageName) throws MalformedURLException {
        JCodeModel codeModel = new JCodeModel();
        URL source = new File(path).toURI().toURL();
        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new NoopAnnotator(), new SchemaStore()), new SchemaGenerator());
//        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
        String fileName = FilenameUtils.getBaseName(source.getPath());
        mapper.generate(codeModel, fileName, packageName, source);
        return codeModel;
    }

    private static GenerationConfig defineConfiguration() {
        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() { // set config option by overriding method
                return true;
            }

            public SourceType getSourceType() {
                return SourceType.JSONSCHEMA;
            }

            @Override
            public boolean isIncludeAdditionalProperties() {
                return false;
            }

            @Override
            public boolean isIncludeToString() {
                return false;
            }

            @Override
            public boolean isIncludeHashcodeAndEquals() {
                return false;
            }

            @Override
            public boolean isIncludeGeneratedAnnotation() {
                return false;
            }
            
            

        };
        return config;
    }
}
