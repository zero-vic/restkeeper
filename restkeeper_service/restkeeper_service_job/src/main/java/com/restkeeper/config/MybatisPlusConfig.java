package com.restkeeper.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties({MybatisPlusProperties.class})
@MapperScan("com.restkeeper.mapper")
public class MybatisPlusConfig{
    @Value("${spring.datasource.order.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.order.password}")
    private String password;

    @Value("${spring.datasource.order.url}")
    private String url;

    @Value("${spring.datasource.order.username}")
    private String username;

    private  MybatisPlusProperties properties;

    public MybatisPlusConfig(MybatisPlusProperties properties) {
        this.properties = properties;
    }

    @Bean(name = "orderDataSource")
    public DataSource orderDataSource(){
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .password(password)
                .username(username)
                .build();
    }

    @Bean("sqlSessionFactory")
    public MybatisSqlSessionFactoryBean sqlSessionFactory(@Qualifier("orderDataSource") DataSource dataSource) throws Exception {

        // 这里必须用 MybatisSqlSessionFactoryBean 代替了 SqlSessionFactoryBean，否则 MyBatisPlus 不会生效
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
        mybatisSqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());

        GlobalConfig globalConfig  = new GlobalConfig();


        mybatisSqlSessionFactoryBean.setGlobalConfig(globalConfig);


        mybatisSqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/mapper/*.xml"));

        MybatisConfiguration configuration = this.properties.getConfiguration();
        if(configuration == null){
            configuration = new MybatisConfiguration();
        }
        mybatisSqlSessionFactoryBean.setConfiguration(configuration);

        return mybatisSqlSessionFactoryBean;
    }
}

