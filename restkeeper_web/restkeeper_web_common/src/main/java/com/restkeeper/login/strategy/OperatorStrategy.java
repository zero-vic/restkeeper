package com.restkeeper.login.strategy;

import com.restkeeper.login.AbstractTokenGranter;
import com.restkeeper.operator.service.IOperatorUserService;
import com.restkeeper.utils.Result;
import com.restkeeper.vo.LoginVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/12
 * Description: 运营端登陆
 * Version:V1.0
 */
@Component
public class OperatorStrategy extends AbstractTokenGranter {

    @Reference(version = "1.0.0",check = false)
    private IOperatorUserService operatorUserService;
    @Override
    public Result grant(LoginVO loginVO) {
        publicCheck();
        Result result = operatorUserService.login(loginVO.getLoginName(), loginVO.getPassword());
        createToken(loginVO);
        return result;
    }
}
