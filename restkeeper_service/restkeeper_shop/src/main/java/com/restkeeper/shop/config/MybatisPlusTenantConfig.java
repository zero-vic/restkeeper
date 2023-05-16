package com.restkeeper.shop.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/04/28
 * Description:
 * Version:V1.0
 */
@Configuration
public class MybatisPlusTenantConfig {
    private static final String SYSTEM_TENANT_ID = "shop_id";
    private static final List<String> IGNORE_TENANT_TABLES = Lists.newArrayList();

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        System.out.println("paginationInterceptor----------------");
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // SQL 解析处理 增加租户回调
        TenantSqlParser tenantSqlParser = new TenantSqlParser().setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId(boolean where) {

                // 使用dubbo 隐式传参
                String shopId = RpcContext.getContext().getAttachment("shopId");

                if(StringUtils.isEmpty(shopId)){
                    throw new RuntimeException("getCurrentProviderId error!!!");
                }

                return new StringValue(shopId);
            }

            @Override
            public String getTenantIdColumn() {
                return SYSTEM_TENANT_ID;
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 忽略掉一些表：如租户表（provider）本身不需要执行这样的处理。

                return IGNORE_TENANT_TABLES.stream().anyMatch(e -> e.equalsIgnoreCase(tableName));
            }
        });
        paginationInterceptor.setSqlParserList(Lists.newArrayList(tenantSqlParser));
        return paginationInterceptor;
    }

}
