package tech.skullprogrammer.projectmaker.model.fm;

import com.sun.codemodel.JMethod;

public class GetMethodInfo {

    private final JMethod jMethod;
    private final String associatedParameterName;
    private final String returnType;
    private final boolean parametrized;
    private final String parametrizedType;

    public GetMethodInfo(JMethod jMethod, String associatedParameterName, String returnType, boolean parametrized, String parametrizedType) {
        this.jMethod = jMethod;
        this.associatedParameterName = associatedParameterName;
        this.returnType = returnType;
        this.parametrized = parametrized;
        this.parametrizedType = parametrizedType;
    }

    public JMethod getjMethod() {
        return jMethod;
    }

    public String getAssociatedParameterName() {
        return associatedParameterName;
    }

    public String getReturnType() {
        return returnType;
    }

    public boolean isParametrized() {
        return parametrized;
    }

    public String getParametrizedType() {
        return parametrizedType;
    }

    public String toFullString() {
        StringBuilder result =  new StringBuilder();
        result.append("Original Return Method Type: ").append(returnType).append("\n");
        result.append("Parameter Name: ").append(associatedParameterName).append("\n");
        result.append("Is Parametrized: ").append(parametrized).append("\n");
        result.append("Parametrized Type: ").append(parametrizedType);
        return result.toString();
    }
}
