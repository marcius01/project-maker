package tech.skullprogrammer.projectmaker;

import com.sun.codemodel.JCodeModel;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.NoopAnnotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.SourceType;
import org.jsonschema2pojo.rules.RuleFactory;
import tech.skullprogrammer.projectmaker.utility.Utility;

public class MainPojo {

    public static void main(String[] args) throws URISyntaxException, MalformedURLException, IOException {
        JCodeModel codeModel = new JCodeModel();

        URL source = (new Utility().getFileFromResource("json/utente.json")).toURI().toURL();
        System.out.println("URL -> " + source.toString());

        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() { // set config option by overriding method
                return true;
            }

            public SourceType getSourceType() {
                return SourceType.JSON;
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

        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new NoopAnnotator(), new SchemaStore()), new SchemaGenerator());

//        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
        mapper.generate(codeModel, "Utente", "tech.skullprogrammer.project.model", source);
        File toFile = new File("/Temp/Sviluppo/maker/");
        codeModel.build(toFile);
        System.out.println("-> " + toFile.getAbsolutePath());

    }
}
