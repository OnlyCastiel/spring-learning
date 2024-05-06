package mybatis.spring.handler;


import java.util.ArrayList;
import java.util.List;

/**
 * 用于处理 #{department, mode=OUT, jdbcType=CURSOR, javaType=ResultSet, resultMap=departmentResultMap}格式的入参
 */
public class ParameterMappingTokenHandler implements TokenHandler {

    /**
     * {@link org.apache.ibatis.mapping.ParameterMapping}
     */
    private List<ParameterMapping> parameterMappings = new ArrayList<>();


    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    /**
     * 处理展位符
     *
     * @param content
     * @return
     */
    @Override
    public String handleToken(String content) {
        //这里对内容进行了分析，并且注册到全局里。
        parameterMappings.add(new ParameterMapping(content));
        //结果返回的是一个？，也就是说#{}类型的参数都会被替换成？
        return "?";
    }


}
