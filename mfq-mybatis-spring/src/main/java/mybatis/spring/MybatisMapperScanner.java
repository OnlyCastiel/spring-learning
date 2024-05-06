package mybatis.spring;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Set;

/**
 * 基于包路径的扫描器
 * 扫描指定的mapper
 */
public class MybatisMapperScanner extends ClassPathBeanDefinitionScanner {


    public MybatisMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);

        //对于扫描到的BeanDefinitionHolder，需要进行改造，交给MybatisFactoryBean生成需要的mapper代理对象
        for(BeanDefinitionHolder bean : beanDefinitionHolders){
            GenericBeanDefinition beanDefinition =(GenericBeanDefinition) bean.getBeanDefinition();
            //设置参数值，明确需要生成代理类的mapper
            //TODO getBeanClassName  getBeanClass 方法区别
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
            beanDefinition.setBeanClass(MybatisFactoryBean.class); //然后调整为MybatisFactoryBean.class
        }

        return beanDefinitionHolders; //交给spring 进行注册
    }

    @Override
    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        return true;  //判断是否是组件类型
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface(); //接口类型即可
    }
}
