package com.restkeeper.operator.controller;

import com.restkeeper.operator.entity.EnterpriseAccount;
import com.restkeeper.operator.service.IEnterpriseAccountService;
import com.restkeeper.vo.AddEnterpriseAccountVO;
import com.restkeeper.vo.PageVO;
import com.restkeeper.vo.ResetPwdVO;
import com.restkeeper.vo.UpdateEnterpriseAccountVO;
import com.restkeeper.utils.AccountStatus;
import com.restkeeper.utils.Result;
import com.restkeeper.utils.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/04/25
 * Description:
 * Version:V1.0
 */
@Slf4j
@RestController
@RequestMapping("/enterprise")
@Api(tags = {"企业账号管理"})
public class EnterpriseAccountController {

    @Reference(version = "1.0.0",check = false)
    private IEnterpriseAccountService enterpriseAccountService;


    /**
     * 查询分页数据
     */
    @ApiOperation(value = "查询企业账号(支持分页)")
    @GetMapping(value = "/pageList/{pageNum}/{pageSize}")
    public PageVO<EnterpriseAccount> findListByPage(@PathVariable("pageNum") int pageNum,
                                                    @PathVariable("pageSize") int pageSize,
                                                    @RequestParam(value = "enterpriseName",required = false) String enterpriseName ){
        return new PageVO<EnterpriseAccount>(enterpriseAccountService.queryPageByName(pageNum,pageSize,enterpriseName));
    }
    @ApiOperation(value = "新增企业账号")
    @PostMapping("/add")
    public boolean add(@RequestBody AddEnterpriseAccountVO accountVO){
        EnterpriseAccount account = new EnterpriseAccount();
        BeanUtils.copyProperties(accountVO,account);
        // 设置时间
        LocalDateTime localDateTime = LocalDateTime.now();
        account.setApplicationTime(localDateTime);
        LocalDateTime expireTime = null;
        // 试用期默认7天
        if(accountVO.getStatus()==0){
            expireTime = localDateTime.plusDays(7);
        }
        if (accountVO.getStatus() == 1){
            //设置到期时间
            expireTime = localDateTime.plusDays(accountVO.getValidityDay());
        }

        if (expireTime != null){
            account.setExpireTime(expireTime);
        }else{
            throw new RuntimeException("帐号类型信息设置有误");
        }

        return enterpriseAccountService.add(account);
    }

    @ApiOperation(value = "账户查看")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",paramType = "path",value = "主键",required = true,dataType = "String")})
    @GetMapping("/getById/{id}")
    public EnterpriseAccount getEnterpriseAccountById(@PathVariable("id") String id){
        return enterpriseAccountService.getById(id);
    }
    @ApiOperation(value = "账户编辑")
    @PutMapping("update")
    public Result updateEnterpriseAccount(@RequestBody UpdateEnterpriseAccountVO accountVO){

        EnterpriseAccount enterpriseAccount = enterpriseAccountService.getById(accountVO.getEnterpriseId());
        Result result = new Result();
        if (enterpriseAccount == null){
            result.setStatus(ResultCode.error);
            result.setDesc("修改账户不存在");
            return result;
        }
        // 修改状态效验
        if(accountVO.getStatus()!=null){
            //正式不能改成试用 状态(试用中0，已停用-1，正式1)")
            if(accountVO.getStatus()==0 && enterpriseAccount.getStatus() ==1){
                result.setStatus(ResultCode.error);
                result.setDesc("不能将正式帐号改为试用帐号");
                return result;
            }
            // 试用改正式
            if(accountVO.getStatus() == 1 && enterpriseAccount.getStatus() == 0){
                //到期时间
                LocalDateTime now = LocalDateTime.now();
                //到期时间
                LocalDateTime expireTime = now.plusDays(accountVO.getValidityDay());
                enterpriseAccount.setApplicationTime(now);
                enterpriseAccount.setExpireTime(expireTime);
            }
            //正式改延期
            if (accountVO.getStatus() == 1 && enterpriseAccount.getStatus() == 1){
                LocalDateTime now = LocalDateTime.now();
                //到期时间
                LocalDateTime expireTime = now.plusDays(accountVO.getValidityDay());
                enterpriseAccount.setExpireTime(expireTime);
            }
        }
        BeanUtils.copyProperties(accountVO,enterpriseAccount);

        //执行修改
        boolean flag = enterpriseAccountService.updateById(enterpriseAccount);
        if (flag){
            //修改成功
            result.setStatus(ResultCode.success);
            result.setDesc("修改成功");
            return result;
        }else{
            //修改失败
            result.setStatus(ResultCode.error);
            result.setDesc("修改失败");
            return result;
        }
    }

    /**
     * 根据id删除  逻辑删除
     */
    @ApiOperation(value = "账户删除")
    @ApiImplicitParam(paramType="query", name = "id", value = "主键", required = true, dataType = "String")
    @DeleteMapping(value = "/deleteById/{id}")
    public boolean deleteById(@PathVariable(value = "id") String id){
        return enterpriseAccountService.removeById(id);
    }

    /**
     * 数据还原
     */
    @ApiOperation(value = "数据恢复")
    @ApiImplicitParam(paramType="query", name = "id", value = "主键", required = true, dataType = "String")
    @GetMapping(value = "/recovery/{id}")
    public boolean recovery(@PathVariable("id") String id){
        return enterpriseAccountService.recovery(id);
    }

    /**
     * 帐号禁用
     * @param id
     * @return
     */
    @ApiOperation(value = "禁止使用")
    @ApiImplicitParam(paramType="path", name = "id", value = "主键", required = true, dataType = "String")
    @GetMapping(value = "/forbidden/{id}")
    public boolean forbidden(@PathVariable("id") String id){
        EnterpriseAccount enterpriseAccount = enterpriseAccountService.getById(id);
        enterpriseAccount.setStatus(AccountStatus.Forbidden.getStatus());
        return enterpriseAccountService.updateById(enterpriseAccount);
    }

    /**
     重置密码
     */
    @ApiOperation(value = "重置密码")
    @PutMapping(value = "/resetPwd")
    public boolean resetPwd(@RequestBody ResetPwdVO resetPwdVO){
        return enterpriseAccountService.resetPwd(resetPwdVO.getId(),resetPwdVO.getPwd());
    }
}
