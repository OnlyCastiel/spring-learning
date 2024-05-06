package mybatis.spring;

import com.mfq.mapper.UserMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


@Component //移出用户项目路径后，无法被扫描到

/**
 *
 * FactoryBean被注册后， 可以在容器中注册两个对象，特别是 mapper 的代理对象，此对象封装了我们需要的mapper实现
 * 通过传入需要代理的mapper对象，产生所需要的代理类
 * 封装sqlSessionFactory
 * 核心：重复代码解耦
 */
public class MybatisFactoryBean implements FactoryBean {


    private Class mapper;

    public MybatisFactoryBean(Class mapper) {
        this.mapper = mapper;
    }


    private SqlSessionFactory sqlSessionFactory;


    /**
     * 此处需要用户完成sqlSessionFactory bean对象的定义
     * 可以通过 @configuration  / xml 等方式进行定义
     * @param sqlSessionFactory
     */
    @Autowired
    public void setSqlSession(SqlSessionFactory sqlSessionFactory){
        sqlSessionFactory.getConfiguration().addMapper(mapper);
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public Object getObject() throws Exception {
        //可以参照项目 mfq-mybatis 中的具体实现 ,亦或者直接使用mybatis提供的代理对象生成
        Object mapper = sqlSessionFactory.openSession().getMapper(this.mapper);
        return mapper;

//        Object instance = Proxy.newProxyInstance(MybatisFactoryBean.class.getClassLoader(), new Class[]{this.mapper}, new InvocationHandler() {
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                //mapper执行接口，会最终执行到代理对象此处的invoke方法， 在此处可完成实际的数据库查询操作
//
//                System.out.println(method.getName());
//                System.out.println("执行了真正的查询方法");
//                return null;
//            }
//        });
//        return instance;
    }

    @Override
    public Class<?> getObjectType() {
        return mapper;
    }



}
