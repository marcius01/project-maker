package tech.skullprogrammer.projectmaker.model;

public class Configuration {

    private String packageRootName;
    private String packageModelName;
    private String templatePath;
    private String templateOutputName;
    private String outputPath;
    private String jsonFolderPath;

    public Configuration(String packageRootName, String packageModelName, String templatePath, String templateOutputName, String outputPath, String jsonFolderPath) {
        this.packageRootName = packageRootName;
        this.packageModelName = packageModelName;
        this.templatePath = templatePath;
        this.templateOutputName = templateOutputName;
        this.outputPath = outputPath;
        this.jsonFolderPath = jsonFolderPath;
    }

    public String getPackageRootName() {
        return packageRootName;
    }

    public void setPackageRootName(String packageRootName) {
        this.packageRootName = packageRootName;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplateOutputName() {
        return templateOutputName;
    }

    public void setTemplateOutputName(String templateOutputName) {
        this.templateOutputName = templateOutputName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getJsonFolderPath() {
        return jsonFolderPath;
    }

    public void setJsonFolderPath(String jsonFolderPath) {
        this.jsonFolderPath = jsonFolderPath;
    }

    public String getPackageModelName() {
        return packageModelName;
    }

    public void setPackageModelName(String packageModelName) {
        this.packageModelName = packageModelName;
    }

}
