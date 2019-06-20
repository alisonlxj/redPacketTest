package com.alison.redpackets.config;


import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.Properties;


/**
 * @description SpringIoC上下文配置
 */

@Configuration
@ComponentScan(value = "com.*")
@EnableTransactionManagement
public class RootConfig implements TransactionManagementConfigurer {

    private DataSource dataSource = null;

    @Bean(name = "dataSource")
    public DataSource initDataSource() {
        if(dataSource == null) {
            Properties properties = new Properties();
            properties.setProperty("jdbc.driver", "com.mysql.jdbc.Driver");
            properties.setProperty("jdbc.url", "jdbc\\:mysql\\://localhost\\:3306/ssm?useUnicode=true&characterEncoding=UTF-8");
            properties.setProperty("jdbc.username", "root");
            properties.setProperty("jdbc.password", "mysql2087");
            properties.setProperty("jdbc.maxWait", "60000");
            // 使用BasicDataSourceFactory.createDataSource 来生成dataSource
            try {
                dataSource = BasicDataSourceFactory.createDataSource(properties);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean getSqlSessionFactory(){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(initDataSource());
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
        return sqlSessionFactoryBean;
    }


    @Bean(name = "mapperScannerConfigurer")
    public MapperScannerConfigurer getMapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setAnnotationClass(Repository.class);
        mapperScannerConfigurer.setBasePackage("com.*");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return mapperScannerConfigurer;
    }


    /**
     * 实现接口方法，注册注解事务，当@Transactional使用时产生注解事务
     * @return 返回 注解驱动事务管理器
     */
    @Override
    @Bean(name = "annotationDrivenTransactionManager")
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(initDataSource());
        return dataSourceTransactionManager;
    }
}
