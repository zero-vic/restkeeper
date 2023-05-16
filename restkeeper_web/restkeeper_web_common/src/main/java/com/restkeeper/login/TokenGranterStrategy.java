package com.restkeeper.login;


import com.restkeeper.utils.Result;
import com.restkeeper.vo.LoginVO;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/11
 * Description: 登录通用接口
 * Version:V1.0
 */
public interface TokenGranterStrategy {
    /**
     * 登录方法
     * @param loginVO
     * @return
     */
    Result grant(LoginVO loginVO);
}
