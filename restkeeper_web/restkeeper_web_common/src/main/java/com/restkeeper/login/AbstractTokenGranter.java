package com.restkeeper.login;


import com.restkeeper.utils.Result;
import com.restkeeper.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/12
 * Description: 登录抽象类，实现一些通用的方法
 * Version:V1.0
 */
@Slf4j
@Component
public abstract class AbstractTokenGranter implements TokenGranterStrategy{

    protected void publicCheck(){
        log.info("publicCheck()");
    }

    /**
     * 效验账号信息，并生成token
     * @return
     */
    protected Result createToken(LoginVO loginVO) {
        log.info("createToken()");
        return null;
    }
}
