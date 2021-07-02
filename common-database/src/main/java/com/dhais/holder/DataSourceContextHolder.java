package com.dhais.holder;

/**
 *
 * All rights Reserved, Designed By Fan Jun
 *
 * @author Fan Jun
 * @since 2021/2/26 14:25
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> dataSourceHolderThreadLocal = new ThreadLocal<>();

    public static void setDS(String dataSourceName){
        dataSourceHolderThreadLocal.set(dataSourceName);
    }

    public static String getDS(){
        return dataSourceHolderThreadLocal.get();
    }

    public static void clearDS(){
        dataSourceHolderThreadLocal.remove();
    }

    public static ThreadLocal<String> getDataSourceContextThreadLocal(){
        return dataSourceHolderThreadLocal;
    }
}
