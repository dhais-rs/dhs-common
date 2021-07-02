package com.dhais.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.dhais.DynamicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import top.ibase4j.core.util.PropertiesUtil;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By Fan Jun
 *
 * @author Fan Jun
 * @since 2021/2/26 14:54
 */

@Configuration
@ConditionalOnClass(DynamicDataSource.class)
public class DataSourceConfig {

    Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    @Primary
    public DataSource shardingdataSource() {
        DynamicDataSource dataSource = DynamicDataSource.newInstance();

        //TODO 获取所有数据源
        Map<Object, Object> targetDataSources = null;
        try {
            targetDataSources = getTargetDataSources();
        } catch (SQLException e) {
            logger.error("",e);
        }
        //TODO 设置默认数据源
        if(targetDataSources!=null||targetDataSources.containsKey("default")){
            dataSource.setDefaultTargetDataSource(targetDataSources.get("default"));
        }
        //TODO 放入动态数据池中
        dataSource.setTargetDataSources(targetDataSources);
        return dataSource;
    }

    /**
     * 获取所有数据源
     * @return
     */
    private Map<Object, Object> getTargetDataSources() throws SQLException {

        Map<Object, Object> targetDataSources = new HashMap<>();

        Map<String, String> properties = PropertiesUtil.getProperties();
        List<String> dataSourceNames = getDataSourceNames(properties);
        for (int i = 0; i < dataSourceNames.size(); i++) {
            String dataSourceName = dataSourceNames.get(i);
            DataSource dataSource = multipleDataSource(dataSourceName);
            targetDataSources.put(dataSourceName,dataSource);
        }
        return targetDataSources;
    }

    private List<String> getDataSourceNames(Map<String, String> properties) {

        List<String> dataSourceNames = new ArrayList<>();
        for(Map.Entry<String, String> entry:properties.entrySet()){
            String key = entry.getKey();
            if(key.matches("druid.\\w+.driverClassName")){
                String dataSourceName = key.split("\\.")[1];
                dataSourceNames.add(dataSourceName);
            }
        }
        return dataSourceNames;
    }

    private DataSource multipleDataSource(String dataSourceName) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(PropertiesUtil.getString("druid."+dataSourceName+".url"));
        dataSource.setDriverClassName(PropertiesUtil.getString("druid."+dataSourceName+".driverClassName"));
        dataSource.setUsername(PropertiesUtil.getString("druid."+dataSourceName+".username"));
        dataSource.setPassword(PropertiesUtil.getString("druid."+dataSourceName+".password"));
        setDruidDataSource(dataSource);
        return dataSource;
    }

    private void setDruidDataSource(DruidDataSource dataSource) throws SQLException {
        dataSource.setMaxActive(1000);   //最大连接数
        dataSource.setInitialSize(10);   //初始化数量
        dataSource.setMaxWait(60000);    //最大等待时间  超时报异常
        dataSource.setTimeBetweenEvictionRunsMillis(200000);  //间隔多久检测需要关闭的空闲连接
        dataSource.setMinEvictableIdleTimeMillis(300000);   //连接最小生存时间
        dataSource.setUseGlobalDataSourceStat(true);  //合并多个druidDataSource监控数据
        if (dataSource.getDriverClassName().toLowerCase().contains("oracle")) {
            dataSource.setValidationQuery(PropertiesUtil.getString("druid.validationQuery", "select 'X' from dual"));
        } else {
            dataSource.setValidationQuery(PropertiesUtil.getString("druid.validationQuery", "SELECT 'x'"));
        }
        dataSource.setFilters("start,wall");
        List<Filter> filters = new ArrayList<>();
        //filters.add(statFilter());
        filters.add(wallFilter());
        dataSource.setProxyFilters(filters);
    }

    private WallFilter wallFilter() {
        WallFilter wallFilter = new WallFilter();

        // 允许执行多条SQL
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);
        config.setStrictSyntaxCheck(false);
        config.setFunctionCheck(false);
        wallFilter.setConfig(config);

        return wallFilter;
    }

}
