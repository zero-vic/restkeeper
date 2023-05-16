package com.restkeeper.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.shop.dto.StoreDTO;
import com.restkeeper.shop.entity.Store;
import com.restkeeper.utils.Result;

import java.util.List;

public interface IStoreService extends IService<Store> {
    /**
     * 根据名称分页查询
     * @param pageNo
     * @param pageSize
     * @param name
     * @return
     */
    IPage<Store> queryPageByName(int pageNo, int pageSize, String name);

    /**
     * 获取所有省份信息
     * @return
     */
    List<String> getAllProvince();

    /**
     * 根据省份获取门店信息
     * @return
     */
    List<StoreDTO> getStoreByProvince(String province);

    /**
     * 管理的门店列表
     * @return
     */
    List<StoreDTO> getStoresByManagerId();

    /**
     * 门店切换逻辑
     * @param storeId
     * @param storeId
     * @return
     */
    Result switchStore(String storeId);
}
