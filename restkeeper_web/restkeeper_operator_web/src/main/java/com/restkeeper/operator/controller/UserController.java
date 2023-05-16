package com.restkeeper.operator.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.restkeeper.enums.GrantTypeEnum;
import com.restkeeper.login.TokenGranterHolder;
import com.restkeeper.login.TokenGranterStrategy;
import com.restkeeper.operator.entity.OperatorUser;
import com.restkeeper.operator.service.IOperatorUserService;
import com.restkeeper.token.RefreshToken;
import com.restkeeper.vo.LoginVO;
import com.restkeeper.vo.PageVO;
import com.restkeeper.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 管理员的登录接口
 */
@RestController
@RefreshScope //配置中心的自动刷新
@Slf4j
@Api(tags = {"管理员相关接口"})
public class UserController {


    @Value("${server.port}")
    private String port;

    @Reference(version = "1.0.0",check = false)
    private IOperatorUserService operatorUserService;
    @Autowired
    private RefreshToken refreshToken;

    @GetMapping(value = "/echo")
    public String echo() {
        System.out.println(port+"ssss");
        return "i am from port: " + port;

    }

    @Autowired
    private TokenGranterHolder tokenGranterHolder;


//    @GetMapping("/pageList/{page}/{pageSize}")
//    public IPage<OperatorUser> findListByPage(@PathVariable("page") int pageNum,
//                                              @PathVariable("pageSize") int pageSize){
//
//        IPage<OperatorUser> page = new Page<OperatorUser>(pageNum,pageSize);
//        log.info("管理员数据分页查询："+ JSON.toJSONString(page));
//        return operatorUserService.page(page);
//    }
    /**
     * 运营端管理员分页查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */

    @ApiOperation("分页列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",name= "page",value = "当前页码",required = true,dataType = "Integer"),
            @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页数据大小",required = true,dataType = "Integer"),
            @ApiImplicitParam(paramType = "query",name = "name",value = "用户名",required = false,dataType = "String"),
    })
    @GetMapping("/pageList/{page}/{pageSize}")
    public PageVO<OperatorUser> findListByPage(@PathVariable(name="page") int pageNum,
                                               @PathVariable(name="pageSize") int pageSize,
                                               @RequestParam(name="name")  String name) {
        //开启分页查询
//        IPage<OperatorUser> page = new Page<OperatorUser>(pageNum,pageSize);
//        log.info("管理员数据分页查询: "+ JSON.toJSONString(page));
//        return operatorUserService.page(page);
//    }
        IPage<OperatorUser> page = operatorUserService.queryPageByName(pageNum, pageSize, name);
        PageVO<OperatorUser> pageV0= new PageVO<OperatorUser>(page);
        return pageV0;
    }

    @ApiOperation(value = "登录效验")
    @ApiImplicitParam(name = "Authorization", value = "jwt token", required = false, dataType = "String",paramType="header")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVO loginVO){
        String operator = GrantTypeEnum.getValueByType("operator");
        TokenGranterStrategy tokenGranterStrategy  = tokenGranterHolder.getGranter(operator);
        Result result = tokenGranterStrategy.grant(loginVO);


//        Result result = operatorUserService.login(loginVO.getLoginName(), loginVO.getPassword());
        return result;
    }
    @ApiOperation(value = "刷新token")
    @ApiImplicitParam(name = "Authorization", value = "jwt token", required = false, dataType = "String",paramType="header")
    @PostMapping("/refresh")
    public Result refreshTokens(HttpServletRequest request) throws IOException {
        String token = request.getHeader("Authorization");
        if(!StringUtils.isEmpty(token)){
            return refreshToken.getAccToken(token);
        }
        return new Result();
    }


}
