package tech.skullprogrammer.projectmaker.model.operator;

import com.google.common.collect.ImmutableMap;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDeclaration;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JVar;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChainGenerator {

    private static Logger logger = LoggerFactory.getLogger(ChainGenerator.class);
    private static String UNIQUE_SUFFIX = "UNIQUE";
    private static String DATE_SUFFIX = "DATE";
    private static String TIMESTAMP_SUFFIX = "TIMESTAMP";
    private static String ID_SUFFIX = "ID";
    private static final JCodeModel jcodeModel = new JCodeModel();

    public static IChain generateNameChain() {
        IChain rootChain = new NameChain(UNIQUE_SUFFIX);
        IChain idChain = new NameChain(ID_SUFFIX);
        IChain dateChain = new NameChain(DATE_SUFFIX);
        IChain timestampChain = new NameChain(TIMESTAMP_SUFFIX);
        rootChain.setNextStep(idChain);
        idChain.setNextStep(dateChain);
        dateChain.setNextStep(timestampChain);
        return rootChain;
    }

    public static IChain generateMethodChain() {
        IChain rootChain = new MethodChain(UNIQUE_SUFFIX, ImmutableMap.of(Column.class, new ImmutablePair<>("unique", Boolean.TRUE)));    
        IChain idChain = new MethodChain(ID_SUFFIX, ImmutableMap.of(Id.class, new ImmutablePair<>(null, null), GeneratedValue.class, new ImmutablePair<>("strategy", GenerationType.TABLE)));        
        IChain dateChain = new MethodChain(DATE_SUFFIX, ImmutableMap.of(Temporal.class, new ImmutablePair<>("value", TemporalType.DATE)));        
        IChain timestampChain = new MethodChain(TIMESTAMP_SUFFIX, ImmutableMap.of(Temporal.class, new ImmutablePair<>("value", TemporalType.TIMESTAMP)));        
        rootChain.setNextStep(idChain);
        idChain.setNextStep(dateChain);
        dateChain.setNextStep(timestampChain);
        return rootChain;
    }

    public static interface IChain<T extends JDeclaration> {

        public boolean execute(T elemntToModify);

        public boolean check(T elemntToModify);

        public void setNextStep(IChain<T> nextStep);
    }

    private static class NameChain implements IChain<JFieldVar> {

        private IChain<JFieldVar> nextStep;
        private final String suffix;

        public NameChain(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public boolean execute(JFieldVar fieldToModify) {
            return performOperation(fieldToModify, false);
        }

        @Override
        public boolean check(JFieldVar fieldToModify) {
            return performOperation(fieldToModify, true);
        }

        private boolean performOperation(JFieldVar fieldToModify, boolean isOnlyCheck) {
            boolean result = false;
            if (fieldToModify.name().endsWith(suffix)) {
                if (!isOnlyCheck) {
                    fieldToModify.name(fieldToModify.name().substring(0, fieldToModify.name().length() - suffix.length()));
                }
                result = true;
            }
            if (nextStep != null) {
                return result || (isOnlyCheck ? nextStep.check(fieldToModify) : nextStep.execute(fieldToModify));
            }
            return result;
        }

        @Override
        public void setNextStep(IChain<JFieldVar> nextStep) {
            this.nextStep = nextStep;
        }

    }

    private static class MethodChain implements IChain<JMethod> {

        private IChain<JMethod> nextStep;
        private final Map<Class, Pair<String, Object>> annotationsMap;
        private final String suffix;

        public MethodChain(String suffix, Map<Class, Pair<String, Object>> annotationsMap) {
            this.suffix = suffix;
            this.annotationsMap = annotationsMap;
        }

        @Override
        public boolean execute(JMethod method) {
            boolean result = false;
            if (method.name().endsWith(suffix)) {
                method.name(method.name().substring(0, method.name().length() - suffix.length()));
                if (method.name().startsWith("get")) {
                    addAnnotations(method);
                }
                for (JVar param : method.params()) {
                    if (param.name().endsWith(suffix)) {
                        param.name(param.name().substring(0, param.name().length() - suffix.length()));
                    }
                }
                result = true;
            }
            if (nextStep != null) {
                return result || nextStep.execute(method);
            }
            return result;
        }

        private void addAnnotations(JMethod method) {
            for (Class annotation : annotationsMap.keySet()) {
                Pair<String, Object> param = annotationsMap.get(annotation);
                JAnnotationUse annotateUse = method.annotate(jcodeModel.ref(annotation));
                if (param != null && param.getLeft() != null && param.getRight() != null) {
                    if (param.getRight() instanceof Boolean) {
                        annotateUse.param(param.getLeft(), (Boolean) param.getRight());
                    } else if (param.getRight() instanceof Enum) {
                        annotateUse.param(param.getLeft(), (Enum) param.getRight());
                    } else {
                        annotateUse.param(param.getLeft(), param.getRight().toString());
                        
                    }
                }
            }
        }

        @Override
        public boolean check(JMethod fieldToModify) {
            return true;
        }

        @Override
        public void setNextStep(IChain<JMethod> nextStep) {
            this.nextStep = nextStep;
        }

    }

}
