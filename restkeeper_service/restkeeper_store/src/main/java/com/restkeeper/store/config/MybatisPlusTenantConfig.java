package com.restkeeper.store.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.google.common.collect.Lists;
import com.restkeeper.mybatis.GeneralMetaObjectHandler;
import com.restkeeper.tenant.TenantContext;
import io.seata.rm.datasource.DataSourceProxy;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/01
 * Description: 多租户配置类支持 shop_id、store_id两级多租户
 * Version:V1.0
 */
@Configuration
@AutoConfigureAfter(MybatisPlusTenantConfig.class)
@EnableConfigurationProperties({MybatisPlusProperties.class})
public class MybatisPlusTenantConfig {

    private static final String SYSTEM_TENANT_SHOP_ID = "shop_id";
    private static final String SYSTEM_TENANT_STORE_ID = "store_id";

    private static final List<String> IGNORE_TENANT_TABLES = Lists.newArrayList();

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        TenantSqlParser tenantSqlParser_shop = new TenantSqlParser().setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId(boolean where) {
                // 从当前系统上下文中取出当前请求的服务商ID，通过解析器注入到SQL中。
                String shopId = RpcContext.getContext().getAttachment("shopId");
                if(StringUtils.isEmpty(shopId)){
                    throw new RuntimeException("#1129 getCurrentProviderId error.");
                }
                return new StringValue(shopId);
            }

            @Override
            public String getTenantIdColumn() {
                return SYSTEM_TENANT_SHOP_ID;
            }

            @Override
            public boolean doTableFilter(String tableName) {
                 // 忽略掉一些表：如租户表（provider）本身不需要执行这样的处理。
                return IGNORE_TENANT_TABLES.stream().anyMatch((e) -> e.equalsIgnoreCase(tableName));
            }
        });

        TenantSqlParser tenantSqlParser_store = new TenantSqlParser().setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId(boolean where) {
                // 从当前系统上下文中取出当前请求的服务商ID，通过解析器注入到SQL中。
                String storeId = RpcContext.getContext().getAttachment("storeId");
                if(StringUtils.isEmpty(storeId)){
                    throw new RuntimeException("#1129 getCurrentProviderId error.");
                }
                return new StringValue(storeId);
            }

            @Override
            public String getTenantIdColumn() {
                return SYSTEM_TENANT_STORE_ID;
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 忽略掉一些表：如租户表（provider）本身不需要执行这样的处理。
                return IGNORE_TENANT_TABLES.stream().anyMatch((e) -> e.equalsIgnoreCase(tableName));
            }
        });
        paginationInterceptor.setSqlParserList(Lists.newArrayList(tenantSqlParser_shop,tenantSqlParser_store));

        // 自定义忽略多租户的方法
        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
            @Override
            public boolean doFilter(MetaObject metaObject) {

                MappedStatement mappedStatement = SqlParserHelper.getMappedStatement(metaObject);
                // 过滤自定义查询，此时无租户信息约束
                if("com.restkeeper.store.mapper.StaffMapper.login".equals(mappedStatement.getId())){
                    return true;
                }
                return false;
            }
        });

        return paginationInterceptor;
    }
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();

        return druidDataSource;
    }

    @Primary//@Primary标识必须配置在代码数据源上，否则本地事务失效
    @Bean("dataSource")
    public DataSourceProxy dataSourceProxy(DataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }

    private MybatisPlusProperties properties;
    public MybatisPlusTenantConfig(MybatisPlusProperties properties) {
        this.properties = properties;
    }


    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSourceProxy dataSourceProxy) throws Exception {

        // 这里必须用 MybatisSqlSessionFactoryBean 代替了 SqlSessionFactoryBean，否则 MyBatisPlus 不会生效
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSourceProxy);
        mybatisSqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());

        GlobalConfig globalConfig  = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new GeneralMetaObjectHandler());
        mybatisSqlSessionFactoryBean.setGlobalConfig(globalConfig);
        mybatisSqlSessionFactoryBean.setPlugins(paginationInterceptor());

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
