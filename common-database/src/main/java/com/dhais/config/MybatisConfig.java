package com.dhais.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import top.ibase4j.core.util.PropertiesUtil;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author Fan Jun
 * @since 2021/2/24 16:03
 */
@Configuration
public class MybatisConfig {

    @Bean("sqlSessionFactory")
    @ConditionalOnBean(DataSource.class)
    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {

        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();

        sqlSessionFactory.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //mapper xml文件
        sqlSessionFactory.setMapperLocations(resolver.getResources(PropertiesUtil.getString("mybatis.mapperLocations")));
        //别名使用的包
        sqlSessionFactory.setTypeAliasesPackage(PropertiesUtil.getString("mybatis.typeAliasesPackage"));
        //是否开启驼峰转换
        Boolean mapUnderscoreToCamelCase = PropertiesUtil.getBoolean("mybatis.configuration.mapUnderscoreToCamelCase", true);
        //自定义mybatis配置
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        //自定义日志工具
        mybatisConfiguration.setLogImpl(Slf4jImpl.class);
        //sql数据无返回 设置为 null
        mybatisConfiguration.setCallSettersOnNulls(true);
        //是否开启驼峰转换
        mybatisConfiguration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
        sqlSessionFactory.setConfiguration(mybatisConfiguration);

        return sqlSessionFactory;
    }
}
