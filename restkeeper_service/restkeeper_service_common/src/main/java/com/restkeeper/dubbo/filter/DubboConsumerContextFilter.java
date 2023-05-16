package com.restkeeper.dubbo.filter;

import com.restkeeper.tenant.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/04/30
 * Description: 解决隐式传参的问题  dubbo的filter
 * Version:V1.0
 */
@Activate
@Slf4j
public class DubboConsumerContextFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment("shopId", TenantContext.getShopId());

        RpcContext.getContext().setAttachment("loginUserId", TenantContext.getLoginUserId());

        RpcContext.getContext().setAttachment("loginUserName", TenantContext.getLoginUserName());

        RpcContext.getContext().setAttachment("storeId", TenantContext.getStoreId());

            log.info("shopId------------" + RpcContext.getContext().getAttachment("shopId"));

            log.info("ThreadName---------" + Thread.currentThread().getName());

            return invoker.invoke(invocation);

    }
}
