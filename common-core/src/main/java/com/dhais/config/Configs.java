package com.dhais.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import top.ibase4j.core.util.PropertiesUtil;
import top.ibase4j.core.util.SecurityUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * All rights Reserved, Designed By Fan Jun
 *
 * @author Fan Jun
 * @since 2021/3/3 16:01
 */
@Configuration
public class Configs implements EnvironmentPostProcessor, Ordered {

    private static final Integer POST_PROCESSOR_ORDER = Integer.MIN_VALUE+10;

    private final Logger logger = LoggerFactory.getLogger(Configs.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();
        String[] profiles = environment.getActiveProfiles();
        Properties prop = getConfig(profiles);
        propertySources.addLast(new PropertiesPropertySource("allProp",prop));
        for (PropertySource<?> propertySource : propertySources) {
            //是键值对的properties 才放入propertiesUtil工具类中
            if(propertySource.getSource() instanceof Map){
                Map map = (Map) propertySource.getSource();
                for (Object key : map.keySet()) {
                    String keyStr = key.toString();
                    Object value = map.get(key);
                    String mainRegex = "druid.\\w+.password|druid.writer\\d?.\\w+.password|druid.reader\\d?.\\w+.password";
                    if (keyStr.matches(mainRegex)) {
//                        String dkey = (String) map.get("druid.key");
//                        dkey = DataUtil.isEmpty(dkey) ? Constants.DB_KEY : dkey;
                        value = SecurityUtil.decryptDes(value.toString());
                        map.put(key, value);
                    }
                    PropertiesUtil.getProperties().put(keyStr, value.toString());
                }
            }
        }

    }

    @Override
    public int getOrder() {
        return this.POST_PROCESSOR_ORDER+1;
    }

    private Properties getConfig(String[] profiles){
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> resourceList = new ArrayList<>();
        getResource(resolver,resourceList,"classpath*:/*.properties");
        getResource(resolver,resourceList,"classpath*:/log.config");
        if(profiles!=null){
            for (int i = 0; i < profiles.length; i++) {
                String p = "classpath*:/config/"+profiles[i]+"/*.properties";
                getResource(resolver,resourceList,p);
            }
        }
        try {
            //spring properties管理对象  将properties加载处理进ioc
            PropertiesFactoryBean config = new PropertiesFactoryBean();
            config.setLocations(resourceList.toArray(new Resource[]{}));
            config.afterPropertiesSet();
            return config.getObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getResource(PathMatchingResourcePatternResolver resolver,List<Resource> resourceList,String path){
        try {
            Resource[] resources = resolver.getResources(path);
            for (Resource resource : resources) {
                resourceList.add(resource);
            }
        } catch (IOException e) {
            logger.error("",e);
        }
    }
}
