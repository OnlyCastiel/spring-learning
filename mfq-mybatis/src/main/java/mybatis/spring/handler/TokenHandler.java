package mybatis.spring.handler;

public interface TokenHandler {
    //根据传入的content获取对应的内容
    String handleToken(String content);
}
