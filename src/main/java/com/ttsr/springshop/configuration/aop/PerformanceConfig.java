package com.ttsr.springshop.configuration.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class PerformanceConfig {

    @Bean
    public PerformanceMonitorInterceptor performanceMonitorInterceptor(){
        return new PerformanceMonitorInterceptor(true);
    }

    @Pointcut("execution(public * (@org.springframework.stereotype.Repository com.ttsr.springshop..*).*(..))")
    public void serviceAnnotation(){
    }

    @Bean
    public Advisor performanceAdvisor(){
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("com.ttsr.springshop.configuration.aop.PerformanceConfig.serviceAnnotation()");
        return new DefaultPointcutAdvisor(pointcut,performanceMonitorInterceptor());
    }
}
