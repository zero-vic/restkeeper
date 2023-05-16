package com.restkeeper.intercepter;

import com.restkeeper.tenant.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/09
 * Description:
 * Version:V1.0
 */
@Slf4j
public class MicroHandlerInterceptor implements HandlerInterceptor {
    /**
     * 请求方法执行之前
     * 返回true则通过
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("shopId",request.getHeader("shopId"));
        map.put("storeId",request.getHeader("storeId"));
        map.put("tableId",request.getHeader("tableId"));
        TenantContext.addAttachments(map);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        TenantContext.clear();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
