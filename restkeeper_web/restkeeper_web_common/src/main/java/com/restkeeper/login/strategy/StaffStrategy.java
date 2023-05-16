package com.restkeeper.login.strategy;

import com.restkeeper.login.AbstractTokenGranter;
import com.restkeeper.store.service.IStaffService;
import com.restkeeper.utils.Result;
import com.restkeeper.vo.LoginVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/12
 * Description: 收银端登录
 * Version:V1.0
 */
@Component
public class StaffStrategy extends AbstractTokenGranter {

    @Reference(version = "1.0.0", check=false)
    private IStaffService staffService;
    @Override
    public Result grant(LoginVO loginVO) {
        publicCheck();
        Result result = staffService.login(loginVO.getShopId(), loginVO.getPhone(), loginVO.getPassword());
        createToken(loginVO);
        return result;
    }
}
