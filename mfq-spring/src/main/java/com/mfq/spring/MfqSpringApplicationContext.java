package com.mfq.spring;

import com.mfq.spring.annotation.Autowired;
import com.mfq.spring.annotation.Component;
import com.mfq.spring.annotation.ComponentScan;
import com.mfq.spring.annotation.Scope;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MfqSpringApplicationContext {

    private Class clazz;

    private static ConcurrentHashMap<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String,BeanDefinition>();
    private static ConcurrentHashMap<String,Object> singletonObjects = new ConcurrentHashMap<String,Object>();

    private static List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public MfqSpringApplicationContext(Class clazz) {
        this.clazz = clazz;
    }

    //运行方法
    public void run(){
        //路径扫描sacn,将扫描到的所有组件放入beanDefinitionMap中
        scan();

        //遍历beanDefinitionMap，进行bean的创建，需要特殊处理scope=prototype的情况
        for(String beanName : beanDefinitionMap.keySet()){
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if(beanDefinition.getScope().equals("singleton")){
                createBean(beanDefinition);
            }
        }
    }

    //扫描文件
    private void scan() {
        if(clazz.isAnnotationPresent(ComponentScan.class)){
            ComponentScan componentScan =(ComponentScan) clazz.getAnnotation(ComponentScan.class);
            String scanPath = componentScan.value();
            if(scanPath == null || scanPath == ""){//默认配置类所在路径
                scanPath = clazz.getName();
                scanPath = scanPath.substring(0,scanPath.lastIndexOf('.'));
            }
            scanPath = scanPath.replace(".","/");

            //文件扫描需要绝对路径
            ClassLoader classLoader = MfqSpringApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(scanPath);

            File file = new File(resource.getFile());
            scanFile(classLoader, file);
        }else{
            //TODO 未加注解情况，非法
        }
    }

    private void scanFile(ClassLoader classLoader, File file) {
        if(file.isDirectory()){
            for(File subFile : file.listFiles()){
                try {
                    if(subFile.isDirectory()){
                        scanFile(classLoader,subFile);
                    }else{
                        //对于以 .class文件结尾的文件，需要掐头去尾
                        String absolutePath = subFile.getAbsolutePath();
                        String classPath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class")).replace("\\",".");


                        Class<?> aClass = classLoader.loadClass(classPath);
                        if(aClass.isAnnotationPresent(Component.class)){
                            if(BeanPostProcessor.class.isAssignableFrom(aClass)){
                                try {
                                    beanPostProcessorList.add((BeanPostProcessor) aClass.newInstance());
                                    continue;
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                            //放入beanDefinitionMap
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setBeanName(aClass.getSimpleName());//TODO spring标准需要处理首字母小写
                            beanDefinition.setClazz(aClass);
                            if(aClass.isAnnotationPresent(Scope.class)){
                                String scope = aClass.getAnnotation(Scope.class).value();
                                if(scope == null){
                                    beanDefinition.setScope("singleton");//默认为单例
                                }else{
                                    beanDefinition.setScope(scope);
                                }
                            }
                            beanDefinitionMap.put(aClass.getSimpleName(),beanDefinition);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //获取容器内的bean
    public Object getBean(String beanName){
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(beanDefinition == null){
            return null;//TODO 待分析是否抛出异常更合理
        }
        if(beanDefinition.getScope().equals("singleton")){
            Object bean = singletonObjects.get(beanName);
            if(bean == null){//还未创建
                bean = createBean(beanDefinition);
                singletonObjects.put(beanDefinition.getBeanName(),bean);
            }
            return bean;
        }else if(beanDefinition.getScope().equals("prototype")){//原型模式，每个都创建一个新的
            return createBean(beanDefinition);
        }else{
            return null;//TODO 还未实现的其他scope情况
        }
    }

    //创建容器内的bean
    public Object createBean(BeanDefinition beanDefinition){
        try {
            Class clazz = beanDefinition.getClazz();
            //获取构造方法 ,TODO 后续需要调整为推断构造方法
            Object instance = clazz.newInstance();
            //设置属性值(DI/IOC)
            for(Field field : clazz.getDeclaredFields()){
                if(field.isAnnotationPresent(Autowired.class)){
                    field.setAccessible(true);
                    field.set(instance,getBean(field.getType().getSimpleName()));
                }
            }

            //实现postProcessor机制--例如AOP
            for(BeanPostProcessor beanPostProcessor : beanPostProcessorList){
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanDefinition.getBeanName());
            }

            //实现initializing机制--接口实现
            if(instance instanceof InitializingBean){
                ((InitializingBean) instance).afterPropertiesSet();
            }

            for(BeanPostProcessor beanPostProcessor : beanPostProcessorList){
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanDefinition.getBeanName());
            }

            //放入单例池 TODO 单例池可以通过多种方式获取，比如 name/type
            singletonObjects.put(beanDefinition.getBeanName(),instance);

            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
