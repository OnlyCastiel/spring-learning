package com.mfq.proxyCreator;


import com.mfq.proxyCreator.advice.MfqAfterReturningAdvice;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfig {


    /*方案一、根据bean的名称判断是否需要增强*/
    /**
     *
     * creator : 约等于 advisor
     * 定义了一个切面，同时制定了 advice -增强的方法
     *
     * BeanNameAutoProxyCreator 继承了BeanPostProcessor 以及 BeanFactoryAware
     * 会在所有的bean初始化后，通过改creator判断是否需要进行AOP增强，若判断为真
     * @return
     */
    @Bean
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
        beanNameAutoProxyCreator.setBeanNames("userSe*");
        beanNameAutoProxyCreator.setInterceptorNames("zhouyuAroundAdvice");
        beanNameAutoProxyCreator.setProxyTargetClass(true);

        return beanNameAutoProxyCreator;
    }


    /*方案二、自动扫描所有的advisor,确定需要代理的逻辑*/
    /**
     * 注册该类后，会自动扫描所有的Advisor
     * 例如：spring的事务实现，便是通过这种方式，
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 声明 Advisor, 确定 pointCut（切面） + advice（增强逻辑）
     */
    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(){
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.addMethodName("test");

        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        defaultPointcutAdvisor.setPointcut(pointcut);
        defaultPointcutAdvisor.setAdvice(new MfqAfterReturningAdvice());

        return defaultPointcutAdvisor;
    }


    /*方案三、直接定义一个切面类,见TestAspect*/


}
