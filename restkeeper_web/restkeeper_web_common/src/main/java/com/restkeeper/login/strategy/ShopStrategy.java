package com.restkeeper.login.strategy;


import com.restkeeper.login.AbstractTokenGranter;
import com.restkeeper.operator.service.IEnterpriseAccountService;
import com.restkeeper.utils.Result;
import com.restkeeper.vo.LoginVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/12
 * Description: 商店管理员
 * Version:V1.0
 */
@Component
public class ShopStrategy extends AbstractTokenGranter {

    @Reference(version = "1.0.0",check = false)
    private IEnterpriseAccountService enterpriseAccountService;
    @Override
    public Result grant(LoginVO loginVO) {
        publicCheck();
        Result result = enterpriseAccountService.login(loginVO.getShopId(), loginVO.getPhone(), loginVO.getPassword());
        createToken(loginVO);
        return result;
    }
}
