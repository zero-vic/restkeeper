package com.restkeeper.controller.enterprise;

import com.restkeeper.constant.SystemCode;
import com.restkeeper.operator.service.IEnterpriseAccountService;
import com.restkeeper.shop.service.IStoreManagerService;
import com.restkeeper.utils.Result;
import com.restkeeper.utils.ResultCode;
import com.restkeeper.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/04/28
 * Description:
 * Version:V1.0
 */
@Api(tags = "登陆接口")
@Slf4j
@RestController
public class LoginController {


    @Reference(version = "1.0.0",check = false)
    private IEnterpriseAccountService enterpriseAccountService;

    /**
     * 门店管理员
     */
    @Reference(version = "1.0.0", check=false)
    private IStoreManagerService storeManagerService;
    @ApiOperation(value = "登录入口")
    @ApiImplicitParam(name = "Authorization", value = "jwt token", required = false, dataType = "String",paramType="header")
    @PostMapping(value = "/login")
    public Result login(@RequestBody LoginVO loginVO) {
        if(SystemCode.USER_TYPE_SHOP.equals(loginVO.getType())){
            return enterpriseAccountService.login(loginVO.getShopId(),loginVO.getPhone(),loginVO.getPassword());
        }

        if(SystemCode.USER_TYPE_STORE_MANAGER.equals(loginVO.getType())){
            return storeManagerService.login(loginVO.getShopId(),loginVO.getPhone(),loginVO.getPassword());
        }
        Result result =new Result();
        result.setStatus(ResultCode.error);
        result.setDesc("不支持该类型用户登录");
        return result;

    }
}
