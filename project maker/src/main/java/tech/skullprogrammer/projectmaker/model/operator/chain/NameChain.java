package tech.skullprogrammer.projectmaker.model.operator.chain;

import com.sun.codemodel.JFieldVar;

public class NameChain implements IChain<JFieldVar> {

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
