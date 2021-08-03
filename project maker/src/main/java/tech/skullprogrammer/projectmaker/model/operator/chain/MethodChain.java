package tech.skullprogrammer.projectmaker.model.operator.chain;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JVar;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;


   public class MethodChain implements IChain<JMethod> {

        private IChain<JMethod> nextStep;
        private final Map<Class, Pair<String, Object>> annotationsMap;
        private final String suffix;
        private final JCodeModel jCodeModel;

        public MethodChain(String suffix, Map<Class, Pair<String, Object>> annotationsMap, JCodeModel jCodeModel) {
            this.suffix = suffix;
            this.annotationsMap = annotationsMap;
            this.jCodeModel = jCodeModel;
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
                JAnnotationUse annotateUse = method.annotate(jCodeModel.ref(annotation));
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
