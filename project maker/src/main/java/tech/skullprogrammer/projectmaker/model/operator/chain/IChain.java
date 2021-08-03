package tech.skullprogrammer.projectmaker.model.operator.chain;

import com.sun.codemodel.JDeclaration;

public interface IChain<T extends JDeclaration> {

    public boolean execute(T elemntToModify);

    public boolean check(T elemntToModify);

    public void setNextStep(IChain<T> nextStep);
}
