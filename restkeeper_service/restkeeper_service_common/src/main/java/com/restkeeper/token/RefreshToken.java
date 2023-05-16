package com.restkeeper.token;

import com.restkeeper.constant.SystemCode;
import com.restkeeper.utils.JWTUtil;
import com.restkeeper.utils.Result;
import com.restkeeper.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/12
 * Description:
 * Version:V1.0
 */

@Component
@RefreshScope
public class RefreshToken {

    @Value("${gateway.secret}")
    private String secret;
    @Autowired
    private StringRedisTemplate redisTemplate;
    public Result getAccToken(String refreshToken) throws IOException {
        Result result = new Result();

        JWTUtil.VerifyResult verifyResult = JWTUtil.verifyJwt(refreshToken, secret);
        if (verifyResult.isValidate()){
            //合法
            Map<String, Object> tokenInfo = JWTUtil.decode(refreshToken);
            String uid = tokenInfo.get("uid").toString();
            String token = redisTemplate.opsForValue().get(SystemCode.REFRESH_TOKEN_PREFIX + uid);
            if(refreshToken.equals(token)){
                String accToken = JWTUtil.createJWTByObj(tokenInfo, secret);
                redisTemplate.opsForValue().set(SystemCode.ACC_TOKEN_PREFIX + uid, accToken,30, TimeUnit.MINUTES);
                result.setToken(accToken);
                result.setRefreshToken(refreshToken);
                result.setStatus(ResultCode.success);
                result.setDesc("ok");
            }else {
                result.setStatus(verifyResult.getCode());
                result.setDesc("签名验证失败");
            }
        }else{
            result.setStatus(verifyResult.getCode());
            result.setDesc("签名验证失败");
        }
        return result;

    }
}
