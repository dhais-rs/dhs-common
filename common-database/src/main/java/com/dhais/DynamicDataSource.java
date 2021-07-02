package com.dhais;

import com.dhais.holder.DataSourceContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author EspecialFirSt。。
 * @since 2021/2/26 14:23
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static DynamicDataSource dynamicDataSource;

    public static DynamicDataSource newInstance(){
        if(dynamicDataSource==null){
            synchronized (DynamicDataSource.class){//加上同步锁 集群并发时避免创建多个对象
                if(dynamicDataSource==null){
                    dynamicDataSource = new DynamicDataSource();
                }
            }
        }
        return dynamicDataSource;
    }

    Map<Object, Object> targetDataSourcesObjs = new HashMap<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDS();
    }

    //设置数据源（所有）
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        this.targetDataSourcesObjs.putAll(targetDataSources);
        super.setTargetDataSources(targetDataSourcesObjs);
        super.afterPropertiesSet();
    }

    public Map<Object, Object> getTargetDataSourcesObjs(){
        return this.targetDataSourcesObjs;
    }

    //移除数据源
    public void removeDataSource(String code) {
        if (targetDataSourcesObjs.get(code) != null) {
            targetDataSourcesObjs.remove(code);
        }
        super.setTargetDataSources(targetDataSourcesObjs);
        super.afterPropertiesSet();
    }


}
