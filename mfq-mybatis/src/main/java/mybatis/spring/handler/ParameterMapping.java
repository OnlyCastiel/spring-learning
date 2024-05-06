package mybatis.spring.handler;

public class ParameterMapping {

    private String property;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public ParameterMapping(String property) {
        this.property = property;
    }
}
