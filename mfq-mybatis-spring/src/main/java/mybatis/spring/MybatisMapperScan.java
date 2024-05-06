package mybatis.spring;


import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//@MapperScan中核心入口
@Import(MybatisImportBeanDefinitionRegistrar.class)
public @interface MybatisMapperScan {

    //默认扫描路径
    String value() default "";
}
