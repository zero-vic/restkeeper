package com.restkeeper.operator.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.restkeeper.constant.SystemCode;
import com.restkeeper.operator.entity.OperatorUser;
import com.restkeeper.operator.mapper.OperatorUserMapper;
import com.restkeeper.utils.JWTUtil;
import com.restkeeper.utils.MD5CryptUtil;
import com.restkeeper.utils.Result;
import com.restkeeper.utils.ResultCode;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//@Service("operatorUserService")
@RefreshScope
@Service(version = "1.0.0",protocol = "dubbo")
    /**
     * dubbo中支持的协议
     * dubbo 默认
     * rmi
     * hessian
     * http
     * webservice
     * thrift
     * memcached
     * redis
     */
public class OperatorUserServiceImpl extends ServiceImpl<OperatorUserMapper, OperatorUser> implements IOperatorUserService{

    @Value("${gateway.secret}")
    private String secret;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public IPage<OperatorUser> queryPageByName(int pageNum, int pageSize, String name) {
        IPage<OperatorUser> page = new Page<>(pageNum,pageSize);
        QueryWrapper<OperatorUser> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isEmpty(name)){
            queryWrapper.like("loginname",name);
        }
        return this.page(page,queryWrapper);
    }

    @Override
    /**
     * 管理员登陆
     */
    public Result login(String loginName, String loginPass) {
        Result result = new Result();
        //参数校验
        if (StringUtils.isEmpty(loginName)){
            result.setStatus(ResultCode.error);
            result.setDesc("用户名为空");
            return result;
        }
        if (StringUtils.isEmpty(loginPass)){
            result.setStatus(ResultCode.error);
            result.setDesc("密码为空");
            return result;
        }
        // 查询用户是否存在
        QueryWrapper<OperatorUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginname",loginName);
        OperatorUser user = getOne(queryWrapper);
        if(user ==null){
            result.setStatus(ResultCode.error);
            result.setDesc("用户不存在");
            return result;
        }
        //比对密码
        String salts = MD5CryptUtil.getSalts(user.getLoginpass());
        if (!Md5Crypt.md5Crypt(loginPass.getBytes(),salts).equals(user.getLoginpass())){
            result.setStatus(ResultCode.error);
            result.setDesc("密码不正确");
            return result;
        }
        // 生成jwt令牌
        String uid = user.getUid();
        String accTokenKey = SystemCode.ACC_TOKEN_PREFIX+uid;
        String refreshTokenKey  = SystemCode.REFRESH_TOKEN_PREFIX+uid;
        Map<String,Object> tokenInfo = Maps.newHashMap();
        tokenInfo.put("loginName",user.getLoginname());
        tokenInfo.put("uid",uid);
        String token = null;
        String refreshToken = null;
        try {
            token = JWTUtil.createJWTByObj(tokenInfo,secret);

            refreshToken = JWTUtil.createJWTByObj(tokenInfo, secret, LocalDateTime.now().plusDays(1));
        }catch (Exception e) {
            log.error("加密失败",e);
            result.setStatus(ResultCode.error);
            result.setDesc("生成令牌失败");
            return result;
        }
        redisTemplate.opsForValue().set(accTokenKey,token,30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(refreshTokenKey,refreshToken,1,TimeUnit.DAYS);

        //返回结果
        result.setStatus(ResultCode.success);
        result.setDesc("ok");
        result.setData(user);
        result.setToken(token);
        result.setRefreshToken(refreshToken);

        return result;
    }
}
