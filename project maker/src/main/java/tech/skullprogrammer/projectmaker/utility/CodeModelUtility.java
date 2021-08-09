package tech.skullprogrammer.projectmaker.utility;

public class CodeModelUtility {

    public static String extractParameterNameFromName(String name) {
        String result = name.replace("get", "");
        return Character.toLowerCase(result.charAt(0)) + result.substring(1);
    }

}
