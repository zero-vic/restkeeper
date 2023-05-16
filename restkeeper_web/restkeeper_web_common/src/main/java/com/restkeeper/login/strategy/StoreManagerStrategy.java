package com.restkeeper.login.strategy;

import com.restkeeper.login.AbstractTokenGranter;
import com.restkeeper.shop.service.IStoreManagerService;
import com.restkeeper.utils.Result;
import com.restkeeper.vo.LoginVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/12
 * Description: 店铺管理员登录
 * Version:V1.0
 */
@Component
public class StoreManagerStrategy extends AbstractTokenGranter {

    @Reference(version = "1.0.0", check=false)
    private IStoreManagerService storeManagerService;
    @Override
    public Result grant(LoginVO loginVO) {
        publicCheck();
        Result result = storeManagerService.login(loginVO.getShopId(), loginVO.getPhone(), loginVO.getPassword());
        createToken(loginVO);
        return result;
    }
}
