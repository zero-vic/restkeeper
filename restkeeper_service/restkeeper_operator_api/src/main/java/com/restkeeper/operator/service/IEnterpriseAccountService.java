package com.restkeeper.operator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.operator.entity.EnterpriseAccount;
import com.restkeeper.utils.Result;

public interface IEnterpriseAccountService extends IService<EnterpriseAccount> {
    /**
     * 根据名称分页查询
     * @param pageNum
     * @param pageSize
     * @param enterpriseName
     * @return
     */
    IPage<EnterpriseAccount> queryPageByName(int pageNum, int pageSize, String enterpriseName);

    /**
     * 新增账号
     * @param enterpriseAccount
     * @return
     */
    boolean add(EnterpriseAccount enterpriseAccount);

    /**
     * 还原
     * @param id
     * @return
     */
    boolean recovery(String id);

    /**
     * 重置密码
     * @param id
     * @param password
     * @return
     */
    boolean resetPwd(String id, String password);


    /**
     * 根据商铺id，账号，密码校验登录信息
     * @param shopId
     * @param telphone
     * @param loginPass
     * @return
     */
    Result login(String shopId, String telphone, String loginPass);
}