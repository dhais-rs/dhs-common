package com.dhais.interceptor;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;
import java.util.Properties;


/**
 * @author Fan Jun
 * @since 2021/2/24 14:28
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "insert", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class,method = "update",args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class PerformanceInterceptor implements Interceptor {

    //拦截器方法
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //开始执行时间
        long start = SystemClock.now();
        Object result = invocation.proceed();
        //sql执行时间间隔
        long limit = SystemClock.now()-start;

        return result;
    }

    /**
     * 生成拦截器代理对象
     *
     * @param o
     * @return
     */
    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    /**
     * 获取配置文件的属性
     **/
    //插件初始化的时候调用，也只调用一次，插件配置的属性从这里设置进来
    @Override
    public void setProperties(Properties properties) {

    }
}
