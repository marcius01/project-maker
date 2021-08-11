package tech.skullprogrammer.projectmaker.model.operator;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JPackage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JModelCleaner {

    private static Logger logger = LoggerFactory.getLogger(JModelCleaner.class);

    public Map<String, Pair<JDefinedClass, JPackage>> createStructureWithNoDuplicatesAndClean(List<JCodeModel> models) {
        Map<String, Pair<JDefinedClass, JPackage>> result = new HashMap<>();
        for (JCodeModel codeModel : models) {
            Iterator<JPackage> packages = codeModel.packages();
            logger.debug("Number of artifacts of model after analisys: " + codeModel.countArtifacts());
            while (packages.hasNext()) {
                JPackage jPackage = packages.next();
                Iterator<JDefinedClass> classes = jPackage.classes();
                while (classes.hasNext()) {
                    JDefinedClass definedClass = classes.next();
                    logger.debug("Analyzed class: {}", definedClass.name());
                    filterClass(definedClass, result, classes, jPackage);
                }
            }
            logger.debug("Number of artifacts of model before analisys: " + codeModel.countArtifacts());
        }
        return result;
    }

    private void filterClass(JDefinedClass definedClass, Map<String, Pair<JDefinedClass, JPackage>> cycles, Iterator<JDefinedClass> classes, JPackage jPackage) {
        Pair<JDefinedClass, JPackage> cycleClassPackage = cycles.get(definedClass.name());
        if (cycleClassPackage != null) {
            JDefinedClass cycleClass = cycleClassPackage.getLeft();
            int cycleClassMethodsNumber = cycleClass.methods().size();
            int definedClassMethodsNumber = cycleClass.methods().size();
            logger.debug("Cycle Class methods: {}", cycleClass.methods().size());
            logger.debug("Analized Class methods: {}", definedClass.methods().size());
            if (cycleClassMethodsNumber > definedClassMethodsNumber) {
                logger.debug("Remove Duplicated or Less Informative Class for: {}", definedClass.name());
                classes.remove();
            } else {
                logger.debug("Found More Informative Class for: {}", definedClass.name());
                JPackage cyclePackage = cycleClassPackage.getRight();
                cyclePackage.remove(cycleClass);
                cycles.put(definedClass.name(), new ImmutablePair<>(definedClass, jPackage));
            }
            logger.debug("Equals: {}", definedClass.equals(cycleClass));
        } else {
            cycles.put(definedClass.name(), new ImmutablePair<>(definedClass, jPackage));
        }
    }

}
