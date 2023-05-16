package com.restkeeper.login;

import com.restkeeper.exception.BussinessException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenGranterHolder {

    private final Map<String, TokenGranterStrategy> granterMap = new ConcurrentHashMap<>();

    public TokenGranterHolder(Map<String, TokenGranterStrategy> granterMap) {
        this.granterMap.putAll(granterMap);
    }


    /**
     * 根据登陆类型获取具体的处理类
     * @param grantType
     * @return
     */
    public TokenGranterStrategy getGranter(String grantType) {
        TokenGranterStrategy tokenGranterStrategy = granterMap.get(grantType);
        Optional.ofNullable(tokenGranterStrategy).orElseThrow(() -> new BussinessException("不存在该登陆类型"));
        return tokenGranterStrategy;
    }
}