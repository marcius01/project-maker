package tech.skullprogrammer.projectmaker.model.operator;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDeclaration;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JVar;
import javax.persistence.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChainGenerator {

    private static Logger logger = LoggerFactory.getLogger(ChainGenerator.class);

    public static IChain generateNameChain() {
        IChain rootChain = new NameChain("UNIQUE");
        rootChain.setNextStep(new NameChain("ID"));
        return rootChain;
    }

    public static IChain generateMethodChain() {
        IChain rootChain = new MethodChain(Column.class, "unique", Boolean.TRUE, "UNIQUE");
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
                return result || nextStep.execute(fieldToModify);
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
        private Class annotation;
        private String paramName;
        private Object paramValue;
        private final String suffix;

        public MethodChain(Class annotation, String paramName, Object paramValue, String suffix) {
            this.annotation = annotation;
            this.paramName = paramName;
            this.paramValue = paramValue;
            this.suffix = suffix;
        }

        @Override
        public boolean execute(JMethod method) {
            boolean result = false;
            if (method.name().endsWith(suffix)) {
                method.name(method.name().substring(0, method.name().length() - suffix.length()));
                if (method.name().startsWith("get")) {
                    if (paramValue instanceof Boolean) {
                        method.annotate(new JCodeModel().ref(annotation)).param(paramName, (Boolean) paramValue);
                    } else {
                        method.annotate(new JCodeModel().ref(annotation)).param(paramName, paramValue.toString());
                    }
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

        private <T> T castObject(Class<T> clazz, Object object) {
            return (T) object;
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
