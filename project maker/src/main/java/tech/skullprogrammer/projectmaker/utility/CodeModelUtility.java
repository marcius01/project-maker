package tech.skullprogrammer.projectmaker.utility;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JFieldVar;

public class CodeModelUtility {

    public static String extractParameterNameFromName(String name) {
        String result = name.replace("get", "");
        return Character.toLowerCase(result.charAt(0)) + result.substring(1);
    }

    public static JClass extractParametrizedType (JFieldVar fieldVar) {        
        return extractJClass(fieldVar, false);
    }
    
    public static JClass extractBaseType (JFieldVar fieldVar) {        
        return extractJClass(fieldVar, true);
    }
    
    private static JClass extractJClass(JFieldVar fieldVar, boolean baseType) {
        boolean parametrized = fieldVar.type().boxify().isParameterized();
        JClass parametrizedType;
        if (parametrized) {
            if (baseType) {
                parametrizedType = fieldVar.type().boxify().erasure();
            } else {
                parametrizedType = fieldVar.type().boxify().getTypeParameters().get(0);
            }
        } else {
            parametrizedType = fieldVar.type().boxify();
        }
        return parametrizedType;
    }

}
