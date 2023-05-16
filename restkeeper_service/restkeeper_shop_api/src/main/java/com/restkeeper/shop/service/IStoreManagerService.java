package com.restkeeper.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.shop.entity.StoreManager;
import com.restkeeper.utils.Result;

import java.util.List;

public interface IStoreManagerService extends IService<StoreManager> {

    /**
     * 店长列表分页查询
     * @param pageNo
     * @param pageSize
     * @param criteria
     * @return
     */
    IPage<StoreManager> queryPageByCriteria(int pageNo, int pageSize, String criteria);


    /**
     * 门店管理员添加逻辑
     * @param name
     * @param phone
     * @param storeIds
     * @return
     */
    public boolean addStoreManager(String name, String phone, List<String> storeIds);

    /**
     * 门店管理员修改
     * @param storeManagerId
     * @param name
     * @param phone
     * @param storeIds
     * @return
     */
    boolean updateStoreManager(String storeManagerId, String name,String phone, List<String> storeIds);

    /**
     * 删除门店管理员
     * @param storeManagerId
     * @return
     */
    boolean deleteStoreManager(String storeManagerId);

    /**
     * 暂停门店管理员
     * @param storeManagerId
     * @return
     */
    boolean pauseStoreManager(String storeManagerId);


    /**
     * 门店管理员登录接口
     * @param shopId
     * @param phone
     * @param loginPass
     * @return
     */
    Result login(String shopId, String phone, String loginPass);
}
