package mybatis.spring.typehandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StringTypeHandler implements TypeHandler<String>{

    @Override
    public void setParameter(PreparedStatement statement, int i, String value) throws SQLException {
        statement.setString(i,value);
    }

    @Override
    public String getResule(ResultSet resultSet, String column) throws SQLException {
        return resultSet.getString(column);
    }
}
