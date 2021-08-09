package tech.skullprogrammer.projectmaker.model.operator;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JPackage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import tech.skullprogrammer.projectmaker.Constants;
import tech.skullprogrammer.projectmaker.model.Configuration;
import tech.skullprogrammer.projectmaker.model.ConfigurationDB;
import tech.skullprogrammer.projectmaker.model.fm.DAOClass;
import tech.skullprogrammer.projectmaker.model.fm.DAODataModels;
import tech.skullprogrammer.projectmaker.model.fm.DBClass;
import tech.skullprogrammer.projectmaker.model.fm.EntityClass;
import tech.skullprogrammer.projectmaker.model.fm.SearchType;
import tech.skullprogrammer.projectmaker.utility.CodeModelUtility;

public class FMDataModelGenerator {

    public FMDataModelGenerator() {
    }

    public DAODataModels generateDAODataModels(List<JCodeModel> models, Configuration configuration, String persistencePackage, String modelPackage) {
        Map<String, Map<String, Object>> daosRoots = new HashMap<>();
        Map<String, Object> configurationRoot = new HashMap<>();
        List<EntityClass> entities =  new ArrayList();
        for (JCodeModel codeModel : models) {
            Iterator<JPackage> packages = codeModel.packages();
            while (packages.hasNext()) {
                JPackage jPackage = packages.next();
                Iterator<JDefinedClass> classes = jPackage.classes();
                while (classes.hasNext()) {
                    Map<String, Object> root = new HashMap<>();
                    JDefinedClass definedClass = classes.next();
                    DAOClass dao = createDAOTreeHash(definedClass, persistencePackage, configuration);
                    EntityClass entity = createSearchTreeHash(definedClass, modelPackage);
                    root.put(Constants.DAO_DATA_MODEL, dao);
                    root.put(Constants.ENTITY_DATA_MODEL, entity);
                    entities.add(entity);
                    daosRoots.put(dao.getName(), root);
                }
            }
        }        
        DBClass dBClass = createDBClass(configuration);
        configurationRoot.put(Constants.DB_DATA_MODEL, dBClass);
        configurationRoot.put(Constants.ENTITIES_DATA_MODEL, entities);        
        return new DAODataModels(daosRoots, configurationRoot);
    }

    private DAOClass createDAOTreeHash(JDefinedClass definedClass, String persistencePackage, Configuration configuration) {
        DAOClass daoClass = new DAOClass();
        daoClass.setPackageName(persistencePackage);
        daoClass.setName(Constants.PREFIX_DAO + definedClass.name());
        String daoInterfaceExtensionName = configuration.getDaoInterfaceExtensionName();
        String extensionName = configuration.getExtensionName();
        daoClass.setInterfaceName(Constants.PREFIX_DAO_INTERFACE + definedClass.name());
        if (daoInterfaceExtensionName != null && !daoInterfaceExtensionName.isEmpty()) {
            daoClass.setInterfaceExtensionName(daoInterfaceExtensionName);
            daoClass.setInterfaceExtensionPackage(configuration.getDaoInterfaceExtensionPackage());
        }
        if (extensionName != null && !extensionName.isEmpty()) {
            daoClass.setExtensionName(extensionName);
            daoClass.setExtensionPackage(configuration.getExtensionPackage());
        }        
        daoClass.setParametrized(configuration.getDaoParametrized());
        daoClass.setExceptionName(configuration.getDaoExceptionName());
        daoClass.setExceptionPackage(configuration.getDaoExceptionPackage());
        return daoClass;
    }

    private EntityClass createSearchTreeHash(JDefinedClass definedClass, String modelPackage) {
        EntityClass entity = new EntityClass();
        entity.setPackageName(modelPackage);
        entity.setName(definedClass.name());
        SearchType searchUnique = new SearchType();
        for (JMethod method : definedClass.methods()) {
            for (JAnnotationUse annotation : method.annotations()) {
                if (annotation.getAnnotationClass().name().equalsIgnoreCase("Column")) {
                    if (annotation.getAnnotationMembers().get("unique") != null) {
                        searchUnique.setName(CodeModelUtility.extractParameterNameFromName(method.name()));
                        searchUnique.setType(method.type().name());
                        entity.addUniqueSearch(searchUnique);
                    }
                }

            }
        }
//        SearchType searchList = new SearchType();
//        searchList.setName("nome");
//        searchList.setType("String");
//        searchList.setOrderParameter("nome");
//        entity.setListSearch(searchList);
        return entity;
    }

    private DBClass createDBClass(Configuration configuration) {
        ConfigurationDB configurationDB = configuration.getConfigurationDB();
        if (configurationDB == null) {
            return null;
        }
        return new DBClass(configurationDB.getDbType(), configurationDB.getDbHost(), configurationDB.getDbPort(), configurationDB.getDbTmpDb(),
                configurationDB.getDbDriver(), configurationDB.getDbSchemaGeneratorClass(), configurationDB.getDbName(),
                configurationDB.getDbUsername(), configurationDB.getDbPassword(), configurationDB.getDbDialect());
    }

}
