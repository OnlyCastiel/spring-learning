##用户自定义springboot工程，包路径非标准路径
* 1、引入springboot包，快速启动一个web服务
* 2、引入自定义的mfqspringboot,启动一个web服务，验证是否能够启动成功
* 3、引入配置类，根据用户服务包依赖，启动tomcat/jetty
* 4、自动配置类，@EnableAutoConfiguration, 如何实现自动配置
（selector -> 引入多个自动配置类 -> ServiceLoader（JDK方式根据类加载对应的spi文件） /AutoConfigurationImportSelector (spring实现根据指定文件加载)  -> 所有自动配置类）
为什么要写在文件中，而不是代码中 -> 便于外部应用自定义自动配置，便于第三方组件的接入，spring.factories
用户实现自动配置类后，springboot负责进行加载:扫描引入的每个jar包下的的 /META-INF 中的对应文件，

3、读取用户自定义的配置文件，如果存在则按照用户定义的参数启动，不存在则按照默认参数启动
* 

java SPI 与 spring SPI 的对比差异
java SPI 性能效率弱于spring SPI

spring 接入 mybatis 方式：
引入mybatis依赖，自己定义一些bean

spring boot 自动配置，实现bean的自动配置（当用户引入相关组件时）
spring boot-start   版本管理，引入该组件需要的所有jar包



