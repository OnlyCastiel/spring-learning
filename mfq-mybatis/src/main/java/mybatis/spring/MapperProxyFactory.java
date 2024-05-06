package mybatis.spring;


import mybatis.spring.handler.GenericTokenParser;
import mybatis.spring.handler.ParameterMappingTokenHandler;
import mybatis.spring.typehandler.StringTypeHandler;
import mybatis.spring.typehandler.TypeHandler;
import mybatis.spring.handler.ParameterMapping;
import mybatis.spring.typehandler.IntegerTypeHandler;

import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * Mapper工厂类
 * 1、通过jdk动态代理，生成mapper代理类
 * 2、实际获取的对象为代理对象，执行的接口方法会执行到代理类重写的代理方法
 * 3、封装连接池，维护连接信息，事务等等
 * 4、封装sql
 * 5、封装返回对象实体
 */
public class MapperProxyFactory {


    private static String driverClassName = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/example?useUnicode=true&characterEncoding=UTF8&useSSL=false";
    private static String userName = "root";
    private static String password = "123456";

    private static Map<Class, TypeHandler> handlerMap = new HashMap<Class, TypeHandler>(){{
        put(String.class,new StringTypeHandler());
        put(Integer.class,new IntegerTypeHandler());
    }};

    public static <T> T getMapper(Class<T> mapper){

        //JDK动态代理方法,参数为一组接口
        Object proxyInstance = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{mapper}, new InvocationHandler() {
            //代理对象执行任何方法时，都会调用该invoke方法
            //proxy 原始对象，method原始方法，args方法参数
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


                /**
                 * 获取sql,封装为mysql可识别的标准sql,参数用占位符‘？’替代
                 * 参数映射关系，即每个‘？’所在位置应该使用的参数
                 * List<String> params 记录参数顺序，以及对应位置的参数code
                 * Map<String,Object> 记录参数code对应的参数值
                 * 此处需要一个TypeHandlerMap,映射不同的参数对象Object设置方法,判断Object instanceof Class,选择不同的处理类
                 * 先解析sql有助于性能提升
                 */
                //处理SQL，将#{}使用 ‘？’占位符替换，同时记录 #{}号中参数code
                Select annotation = method.getAnnotation(Select.class);
                String querySql = annotation.value();
                //返回一个占位符‘？’填充的标准sql，并填充各占位符中的参数code顺序，List<String>
                ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
                String parseSql = new GenericTokenParser("#{", "}", parameterMappingTokenHandler).parse(querySql);
                List<ParameterMapping> codeList = parameterMappingTokenHandler.getParameterMappings();

                Map<String,Object> params =  new HashMap<String,Object>();
                Parameter[] parameters = method.getParameters();
                for(int i = 0 ; i < parameters.length ; i++){
                    //填充参数code,对应值的映射
                    params.put(parameters[i].getAnnotation(Param.class).value(),args[i]);
                }


                /**
                 * 加载驱动（可放静态方法中）---jdbc4.0后引入相关包即可自动引入驱动 java.sql.Driver
                 * 获取数据库连接
                 */
                Connection connection = DriverManager.getConnection(url, userName, password);
                /**
                 * Preperstatement 编译sql
                 * 遍历对每个‘？’，进行赋值
                 */
                PreparedStatement statement = connection.prepareStatement(parseSql);
                for (int i = 0 ;i < codeList.size() ; i++){
                    //需要根据参数类型，定位到对应方法，设置参数
                    String property = codeList.get(i).getProperty();
                    TypeHandler typeHandler = handlerMap.get(params.get(property).getClass());
                    typeHandler.setParameter(statement,i+1,params.get(property));
                }


                //执行sql，获取ResuleSet
                statement.execute();
                ResultSet resultSet = statement.getResultSet();


                //获取方法的返回类型,判断是否包含泛型
                Class<?> returnType = null;
                Type genericReturnType = method.getGenericReturnType();
                if(genericReturnType instanceof Class){//不包含泛型
                    returnType = (Class) genericReturnType;
                }else if(genericReturnType instanceof ParameterizedType){//包含泛型
                    //获取实际类型
                    Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
                    //如果存在多个泛型，假设第一个是我们需要的，实际情况中需要拓展
                    returnType = (Class) actualTypeArguments[0];
                }

                //封装默认映射关系，根据字段名找到对应的set方法
                Map<String,Method> methodMap = new HashMap<>();
                for(Method declaredMethod : returnType.getDeclaredMethods()){
                    if(declaredMethod.getName().startsWith("set")){
                        //String name = declaredMethod.getParameters()[0].getName();//此种方式只能拿到args0,拿不到实际参数名
                        String name = declaredMethod.getName().substring(3);
                        //首字母变小写
                        name = name.substring(0,1).toLowerCase(Locale.ROOT).concat(name.substring(1));
                        methodMap.put(name,declaredMethod);
                    }
                }

                //找到sql执行结果的返回列名
                List<String> columnList = new ArrayList<>();
                for(int i = 0 ;i < resultSet.getMetaData().getColumnCount(); i++){
                    columnList.add(resultSet.getMetaData().getColumnName(i+1));
                }

                //
                List<Object> resultList = new ArrayList<>();
                while (resultSet.next()){
                    Object instance = returnType.newInstance();
                    //分别调用返回字段对应的invoke方法
                    //找到方法method, invoke,
                    for(String column : columnList){
                        //如何判断需要调用的方法，TODO 未处理下划线情况
                        Method setterMethod = methodMap.get(column);
                        TypeHandler typeHandler = handlerMap.get(setterMethod.getParameterTypes()[0]);
                        setterMethod.invoke(instance,typeHandler.getResule(resultSet,column));

                    }
                    //如果是列表则封装列表
                    resultList.add(instance);
                }

                if(method.getReturnType().equals(List.class)){
                    return resultList;
                }
                return resultList.get(0);
            }
        });

        return (T) proxyInstance;
    }
}
