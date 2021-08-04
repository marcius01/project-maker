package tech.skullprogrammer.projectmaker.model.operator;

import com.sun.codemodel.JAnnotationUse;
import tech.skullprogrammer.projectmaker.model.operator.chain.ChainFactory;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JPackage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.skullprogrammer.projectmaker.model.fm.GetMethodInfo;
import tech.skullprogrammer.projectmaker.model.operator.chain.IChain;
import tech.skullprogrammer.projectmaker.utility.Errors;

public class FMEntityGenerator {

    private static Logger logger = LoggerFactory.getLogger(FMEntityGenerator.class);
    private IChain rootNameChain;
    private IChain rootMethodChain;

    public FMEntityGenerator() {
        rootNameChain = ChainFactory.generateNameChain();
        rootMethodChain = ChainFactory.generateMethodChain();
    }

    public void modifyModelForHibernate(List<JCodeModel> models) {
        Map<String, Pair<JDefinedClass, JPackage>> cycles = new HashMap<>();
        for (JCodeModel codeModel : models) {
            Iterator<JPackage> packages = codeModel.packages();
            logger.debug("Number of artifacts of model after analisys: " + codeModel.countArtifacts());
            while (packages.hasNext()) {
                JPackage jPackage = packages.next();
                Iterator<JDefinedClass> classes = jPackage.classes();
                while (classes.hasNext()) {
                    JDefinedClass definedClass = classes.next();
                    logger.debug("Analyzed class: {}", definedClass.name());
                    filterClass(definedClass, cycles, classes, jPackage);
                }
            }
            logger.debug("Number of artifacts of model before analisys: " + codeModel.countArtifacts());
        }
        cleanClassAndAddEntityAnnotation(cycles);
        createEntityMapping(cycles);
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

    private void cleanClassAndAddEntityAnnotation(Map<String, Pair<JDefinedClass, JPackage>> cycles) {
        for (String className : cycles.keySet()) {
            JDefinedClass definedClass = cycles.get(className).getLeft();
            definedClass.annotate(definedClass.owner().ref(Entity.class));
            modifyFields(definedClass);
            modifyMethods(definedClass);
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

    private void createEntityMapping(Map<String, Pair<JDefinedClass, JPackage>> elements) {
        Map<String, Map<String, List<GetMethodInfo>>> methodsMap = createMethodsInfoMap(elements);
        JCodeModel jCodeModel = new JCodeModel();
        for (String className : methodsMap.keySet()) {
            Map<String, List<GetMethodInfo>> methodsMapForParametrizedType = methodsMap.get(className);
            for (String parametrizedType : methodsMapForParametrizedType.keySet()) {
                List<GetMethodInfo> methodsForParametrizedType = methodsMapForParametrizedType.get(parametrizedType);
                for (GetMethodInfo methodInfoSelected : methodsForParametrizedType) {
                    Map<String, List<GetMethodInfo>> methodsMapForAssociatedParametrizedType = methodsMap.get(parametrizedType);
                    List<GetMethodInfo> methodsOfAssociatedEntity = methodsMapForAssociatedParametrizedType != null ? methodsMapForAssociatedParametrizedType.get(className) : null;
                    createMapping(className, methodInfoSelected, methodsForParametrizedType, methodsOfAssociatedEntity, jCodeModel);
                }
            }
        }
    }

    //TODO use properties to match the elments, then via name properties obtains methods
    private Map<String, Map<String, List<GetMethodInfo>>> createMethodsInfoMap(Map<String, Pair<JDefinedClass, JPackage>> elements) {
        Map<String, Map<String, List<GetMethodInfo>>> methodsMap = new HashMap<>();
        for (String className : elements.keySet()) {
            Map<String, List<GetMethodInfo>> methodsMapForParametrizedType = new HashMap<>();
            methodsMap.put(className, methodsMapForParametrizedType);
            Pair<JDefinedClass, JPackage> element = elements.get(className);
            JDefinedClass definedClass = element.getLeft();
//            JMethod methodtmp = definedClass.getMethod("getConti", new JType[0]);
            for (JMethod method : definedClass.methods()) {
                if (method.type().isReference() && method.name().startsWith("get")) {
                    boolean parametrized = method.type().boxify().isParameterized();
                    String parametrizedType;
                    if (parametrized) {
                        parametrizedType = method.type().boxify().getTypeParameters().get(0).name();
                    } else {
                        parametrizedType = method.type().name();
                    }
                    Pair<JDefinedClass, JPackage> entity = elements.get(parametrizedType);
                    boolean isEntityType = entity != null;
                    if (isEntityType) {
                        String returnType = method.type().name();
                        String parameterName = extractParameterNameFromName(method.name());
                        GetMethodInfo methodInfo = new GetMethodInfo(method, parameterName, returnType, parametrized, parametrizedType);
                        List<GetMethodInfo> methodsForParametrizedType = methodsMapForParametrizedType.get(methodInfo.getParametrizedType());
                        if (methodsForParametrizedType == null) {
                            methodsForParametrizedType = new ArrayList<>();
                            methodsMapForParametrizedType.put(methodInfo.getParametrizedType(), methodsForParametrizedType);
                        }
                        methodsForParametrizedType.add(methodInfo);
                        logger.debug("{}\n", methodInfo.toFullString());
                    }
                }
            }
        }
        return methodsMap;
    }

    private String extractParameterNameFromName(String name) {
        String result = name.replace("get", "");
        return Character.toLowerCase(result.charAt(0)) + result.substring(1);
    }

    //TODO analize if it is possible to use a chain
    private void createMapping(String className, GetMethodInfo methodInfo, List<GetMethodInfo> methodsOfEntity, List<GetMethodInfo> methodsOfAssociatedEntity, JCodeModel jCodeModel) {
        JMethod method = methodInfo.getjMethod();
        if (methodsOfAssociatedEntity == null || methodsOfAssociatedEntity.isEmpty()) {
            if (methodInfo.isParametrized()) {
                method.annotate(jCodeModel.ref(OneToMany.class));
            } else {
                method.annotate(jCodeModel.ref(ManyToOne.class));
            }
        } else if (methodsOfEntity.size() != methodsOfAssociatedEntity.size()) {
            throw new UnsupportedOperationException("Different sizes for associations between " + className + " and " + methodInfo.getParametrizedType());
        } else {
            GetMethodInfo associatedMethodInfo = findAssociatedMethodInfo(methodInfo, methodsOfAssociatedEntity);
            if (methodInfo.isParametrized()) {
                if (associatedMethodInfo.isParametrized()) {
                    JAnnotationUse annotation = method.annotate(jCodeModel.ref(ManyToMany.class));
                    if (!associatedMethodInfo.getjMethod().annotations().isEmpty()) {
                        annotation.param("mappedBy", associatedMethodInfo.getAssociatedParameterName());
                    }
                } else {
                    JAnnotationUse annotation = method.annotate(jCodeModel.ref(OneToMany.class));
                    annotation.param("mappedBy", associatedMethodInfo.getAssociatedParameterName());
                }

            } else {
                if (associatedMethodInfo.isParametrized()) {
                    method.annotate(jCodeModel.ref(ManyToOne.class));
                } else {
                    JAnnotationUse annotation = method.annotate(jCodeModel.ref(OneToOne.class));
                    if (!associatedMethodInfo.getjMethod().annotations().isEmpty()) {
                        annotation.param("mappedBy", associatedMethodInfo.getAssociatedParameterName());
                    }
                }
            }

        }

    }

    private GetMethodInfo findAssociatedMethodInfo(GetMethodInfo methodInfo, List<GetMethodInfo> methodsOfAssociatedEntity) {
        if (methodsOfAssociatedEntity.size() == 1) {
            return methodsOfAssociatedEntity.get(0);
        }
        for (GetMethodInfo associatedMethodInfo : methodsOfAssociatedEntity) {
            String cleanedField = cleanFieldName(methodInfo.getAssociatedParameterName(), methodInfo.getParametrizedType()).toLowerCase();
            String cleanedAssociatedField = cleanFieldName(associatedMethodInfo.getAssociatedParameterName(), associatedMethodInfo.getParametrizedType()).toLowerCase();
            logger.debug("cleanedFields: {} -- {}", cleanedField, cleanedAssociatedField);
            boolean methodInfoContainsAssociatedName = cleanedField.contains(cleanedAssociatedField);
            boolean associatedMethodInfoContainsName = cleanedAssociatedField.contains(cleanedField);
            if(methodInfoContainsAssociatedName || associatedMethodInfoContainsName) {
                return associatedMethodInfo;
            }            
        }
        throw new IllegalArgumentException(Errors.MULTIPLE_MAPPING_ASSOCCIATION_NOT_MATCHED + methodInfo.getAssociatedParameterName());
    }
    
    private String cleanFieldName(String string, String toFind) {
        String stringToLowerCase = string.toLowerCase();
        String toFindToLowerCase = toFind.toLowerCase();
        if (stringToLowerCase.contains(toFindToLowerCase)) {
            int startIndex = stringToLowerCase.indexOf(toFindToLowerCase);
            StringBuilder builder = new StringBuilder(string);
            return builder.replace(startIndex, startIndex+toFind.length(), "").toString();
        }
        return string;
    }

}
