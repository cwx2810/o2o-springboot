package com.imooc.o2o.config.dao;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author: LieutenantChen
 * @create: 2018-09-13 15:33
 **/
@Configuration
public class SessionFactoryConfiguration {

    // 注入配置属性
    @Autowired
    private DataSource dataSource;

    // 静态需要setter方法
    private static String mybatisConfig;
    @Value("${mybatis_config_file}")
    public void setMybatisConfig(String mybatisConfig) {
        SessionFactoryConfiguration.mybatisConfig = mybatisConfig;
    }

    @Value("${type_alias_package}")
    private String typeAliasPackage;

    private static String mapperPath;
    @Value("${mapper_path}")
    public void setMapperPath(String mapperPath) {
        SessionFactoryConfiguration.mapperPath = mapperPath;
    }

    /**
     * 创建session工厂
     * @return
     * @throws IOException
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean createSessionFactory() throws IOException {

        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();

        // 注入数据库连接池
        sessionFactory.setDataSource(dataSource);
        // 加载mybatis配置文件
        sessionFactory.setConfigLocation(new ClassPathResource(mybatisConfig));
        // 扫描数据库表的别名实体
        sessionFactory.setTypeAliasesPackage(typeAliasPackage);
        // 扫描mapper.xml
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver =
                new PathMatchingResourcePatternResolver();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPath;
        sessionFactory.setMapperLocations(
                pathMatchingResourcePatternResolver.getResources(packageSearchPath)
        );

        return sessionFactory;
    }


}
