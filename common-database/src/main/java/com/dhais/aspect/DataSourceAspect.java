package com.dhais.aspect;

import com.dhais.annotation.DataSourceTarget;
import com.dhais.holder.DataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * All rights Reserved, Designed By EspecialFirSt。。
 *
 * @author EspecialFirSt。。
 * @since 2021/2/26 16:16
 */

@Aspect
@Component
public class DataSourceAspect {

    @Pointcut("execution(* com.dhais.service..*.*(..))")
    public void dataSourcePointcut(){}

    @Before("dataSourcePointcut()")
    public void before(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        Class<?> declaringType = signature.getDeclaringType();
        String methodName = signature.getName();
        String dataSourceName = "default";

        Method[] methods = declaringType.getMethods();
        for (Method method : methods) {
            if(methodName.equalsIgnoreCase(method.getName())){
                if(method.isAnnotationPresent(DataSourceTarget.class)){
                    DataSourceTarget annotation = method.getAnnotation(DataSourceTarget.class);
                    dataSourceName = annotation.value();
                }
            }
        }

        DataSourceContextHolder.setDS(dataSourceName);
    }

    @After("dataSourcePointcut()")
    public void after(){
        DataSourceContextHolder.clearDS();
    }
}
