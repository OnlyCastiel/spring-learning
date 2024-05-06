package mybatis.spring;


import com.mfq.mapper.UserMapper;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Set;

/**
 * 模拟mybatis中的mapper注册器
 * 通过 @MapperScan中引入，作为mybatis接入的核心入口，
 * 由于此注册器实现ImportBeanDefinitionRegistrar
 * 则spring在启动过程中执行了该类中重写的注册bean的方法，注册用户所需的bean
 *
 * 从MapperScan获取扫描路径，遍历注册这些mapper
 */
public class MybatisImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        //通过importingClassMetadata 获取引入类中扫描的路径，即 @MapperScan扫描的路径 ,不存在则扫描
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(MybatisMapperScan.class.getName());
        String basePackage = (String) annotationAttributes.get("value");

        //TODO 未自定义路径时，默认路径与添加 @MapperScan 注解所在类路径相同
        if(basePackage == null || basePackage.equals("")){
            String className = importingClassMetadata.getClassName();
            basePackage = className.substring(0,className.lastIndexOf('.'));
        }
        //扫描路径下的所有
        MybatisMapperScanner scanner = new MybatisMapperScanner(registry);
        //扫描完成即完成注册
        scanner.doScan(basePackage);


        //通过扫描器后，不再需要显式注册
//        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
//        beanDefinition.setBeanClass(MybatisFactoryBean.class);
//        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(UserMapper.class);//通过构造方法，设置需要实现的mapper,从而可以循环注册大量mapper
//
//        registry.registerBeanDefinition("userMapper",beanDefinition);

    }


}
