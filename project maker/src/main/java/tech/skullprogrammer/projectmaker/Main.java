package tech.skullprogrammer.projectmaker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import tech.skullprogrammer.projectmaker.model.fm.DAOClass;
import tech.skullprogrammer.projectmaker.model.fm.EntityClass;
import tech.skullprogrammer.projectmaker.model.fm.SearchType;
import tech.skullprogrammer.projectmaker.proxy.ComponentFactory;
import tech.skullprogrammer.projectmaker.utility.Utility;

public class Main {

    public static void main(String[] args) throws IOException, TemplateException, URISyntaxException {

        new Main().esegui2();
    }

    private void esegui2() throws IOException {
        tech.skullprogrammer.projectmaker.model.Configuration configuration = ComponentFactory.getInstance().getConfigurationProxy().getConfiguration();
        Utility.unzipFolderZip4j(Paths.get(configuration.getTemplatePath()), Paths.get(configuration.getOutputPath()), "progetto", false);
    }

    private void esegui() throws IOException, TemplateException, URISyntaxException {
    // Create your Configuration instance, and specify if up to what FreeMarker
// version (here 2.3.29) do you want to apply the fixes that are not 100%
// backward-compatible. See the Configuration JavaDoc for details.
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);

// Specify the source where the template files come from. Here I set a
// plain directory for it, but non-file-system sources are possible too:
        cfg.setDirectoryForTemplateLoading(new Utility().getFileFromResource("templates"));

// From here we will set the settings recommended for new projects. These
// aren't the defaults for backward compatibilty.
// Set the preferred charset template files are stored in. UTF-8 is
// a good choice in most applications:
        cfg.setDefaultEncoding("UTF-8");

// Sets how errors will appear.
// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
        cfg.setLogTemplateExceptions(false);

// Wrap unchecked exceptions thrown during template processing into TemplateException-s:
        cfg.setWrapUncheckedExceptions(true);

// Do not fall back to higher scopes when reading a null loop variable:
        cfg.setFallbackOnNullLoopVariable(false);

        // Create the root hash. We use a Map here, but it could be a JavaBean too.
        Map<String, Object> root = new HashMap<>();

// Put string "user" into the root
        DAOClass dao = new DAOClass();
        dao.setPackageName("tech.skullprogrammer.myapp.persistence");
        dao.setName("DAOUtente");
        dao.setInterfaceName("IDAOUtente");
        EntityClass entity = new EntityClass();
        entity.setPackageName("tech.skullprogrammer.myapp.model");
        entity.setName("Utente");
        SearchType searchUnique = new SearchType();
        searchUnique.setName("codiceFiscale");
        searchUnique.setType("String");
        SearchType searchList = new SearchType();
        searchList.setName("nome");
        searchList.setType("String");
        searchList.setOrderParameter("nome");
//        entity.setUniqueSearch(searchUnique);
//        entity.setListSearch(searchList);

        root.put("dao", dao);
        root.put("entity", entity);

        Template temp = cfg.getTemplate("DAOEntity.ftlh");

        Writer out = new OutputStreamWriter(System.out);
        temp.process(root, out);
    }
}
