package starter;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MfqStarterInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {


    @Override
    protected Class<?>[] getRootConfigClasses() {
        //指定spring配置类，启动配置类路径，AppConfig.class --扫描spirng容器（@Component），排除MVC （@Controller  @RestController）的内容
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        //指定springMVC 配置类，启动配置类路径，AppWebConfig.class  --扫描 @Controller  @RestController注解的类
        return new Class[0];
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    public static void main(String[] args) {
        MfqStarterInitializer initializer = new MfqStarterInitializer();
        initializer.onStartup();
    }
}
