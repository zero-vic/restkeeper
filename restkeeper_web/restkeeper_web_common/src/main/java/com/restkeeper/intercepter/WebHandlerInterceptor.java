package com.restkeeper.intercepter;

import com.restkeeper.tenant.TenantContext;
import com.restkeeper.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/04/28
 * Description:
 * Version:V1.0
 */
@Component
@Slf4j
public class WebHandlerInterceptor implements HandlerInterceptor {
    //handler执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String tokenInfo = request.getHeader("Authorization");

        if (StringUtils.isNotEmpty(tokenInfo)){
            try {
                Map<String, Object> tokenMap = JWTUtil.decode(tokenInfo);
                TenantContext.addAttachments(tokenMap);
//                String shopId = (String) tokenMap.get("shopId");
//                RpcContext.getContext().setAttachment("shopId", shopId);
            } catch (IOException e) {
                log.error("解析token出错");
            }
        }
        return true;
    }
}
