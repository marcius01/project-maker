package tech.skullprogrammer.projectmaker.model.operator;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JVar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.skullprogrammer.projectmaker.model.fm.EntityClass;
import tech.skullprogrammer.projectmaker.model.fm.EntityProperty;

public class FMEntityGenerator {

    private static Logger logger = LoggerFactory.getLogger(FMEntityGenerator.class);
    private ChainGenerator.IChain rootNameChain;
    private ChainGenerator.IChain rootMethodChain;

    public FMEntityGenerator() {
        rootNameChain = ChainGenerator.generateNameChain();        
        rootMethodChain = ChainGenerator.generateMethodChain();
    }
    
    

    public List<EntityClass> generatesEntitiesFromCodeModel(List<JCodeModel> models) {
        List<EntityClass> result = new ArrayList<>();
        for (JCodeModel codeModel : models) {
            EntityClass entityClass = new EntityClass();
            Iterator<JPackage> packages = codeModel.packages();
            while (packages.hasNext()) {
                JPackage jPackage = packages.next();
                entityClass.setPackageName(jPackage.name());
                Iterator<JDefinedClass> classes = jPackage.classes();
                while (classes.hasNext()) {
                    JDefinedClass definedClass = classes.next();
                    entityClass.setName(definedClass.name());
                    Map<String, EntityProperty> properties = new HashMap<>();
                    for (JMethod method : definedClass.methods()) {
                        logger.info("### {}", method.name());
                    }
                    for (String key : definedClass.fields().keySet()) {
                        logger.info("key: {} -- value: {}", key, ((JFieldVar) definedClass.fields().get(key)).type().fullName());
                    }
                }
            }
        }
        return result;
    }

    public void modifyModelForHibernate(List<JCodeModel> models) {
        Map<String, Map<JDefinedClass, JPackage>> cycles = new HashMap<>();
        for (JCodeModel codeModel : models) {
            Iterator<JPackage> packages = codeModel.packages();
            logger.debug("Number of artifacts of model after analisys: " + codeModel.countArtifacts());
            while (packages.hasNext()) {
                JPackage jPackage = packages.next();
                Iterator<JDefinedClass> classes = jPackage.classes();
                while (classes.hasNext()) {
                    JDefinedClass definedClass = classes.next();
                    logger.debug("Analyzed class: {}", definedClass.name());
                    definedClass.annotate(codeModel.ref(Entity.class));
                    modifyFields(definedClass);
                    modifyMethods(definedClass);
                    Map<JDefinedClass, JPackage> cycleClass = cycles.get(definedClass.name());
                    filterClass(cycleClass, definedClass, cycles, classes, jPackage);
                }
            }
            logger.debug("Number of artifacts of model before analisys: " + codeModel.countArtifacts());
        }
    }

    private void filterClass(Map<JDefinedClass, JPackage> cycleClassPackage, JDefinedClass definedClass, Map<String, Map<JDefinedClass, JPackage>> cycles, Iterator<JDefinedClass> classes, JPackage jPackage) {
        if (cycleClassPackage != null) {
            JDefinedClass cycleClass = cycleClassPackage.keySet().iterator().next();
            int cycleClassMethodsNumber = cycleClass.methods().size();
            int definedClassMethodsNumber = cycleClass.methods().size();
            logger.debug("Cycle Class methods: {}", cycleClass.methods().size());
            logger.debug("Analized Class methods: {}", definedClass.methods().size());
            if (cycleClassMethodsNumber > definedClassMethodsNumber) {
                logger.debug("Remove Duplicated or Less Informative Class for: {}", definedClass.name());
                classes.remove();
            } else {
                logger.debug("Found More Informative Class for: {}", definedClass.name());
                JPackage cyclePackage = cycleClassPackage.get(cycleClass);
                cyclePackage.remove(cycleClass);
                cycles.put(definedClass.name(), Collections.singletonMap(definedClass, jPackage));
            }
            logger.debug("Equals: {}", definedClass.equals(cycleClass));
        } else {
            cycles.put(definedClass.name(), Collections.singletonMap(definedClass, jPackage));
        }
    }

    private void modifyMethods(JDefinedClass definedClass) {
        for (JMethod method : definedClass.methods()) {
            rootMethodChain.execute(method);
        }
    }

    private void modifyFields(JDefinedClass definedClass) {
        List<JFieldVar> toModify = new ArrayList<>();
        for (String key : definedClass.fields().keySet()) {
            JFieldVar field = (JFieldVar) definedClass.fields().get(key);
            if (rootNameChain.check(field)) {
                toModify.add(field);                
            }
        }
        for (JFieldVar fieldToModify : toModify) {
            modifyName(fieldToModify);
        }
    }

    private void modifyName(JFieldVar fieldToModify) {
        rootNameChain.execute(fieldToModify);
    }

}
