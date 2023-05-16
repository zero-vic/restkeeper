package com.restkeeper.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.shop.entity.Brand;

import java.util.List;
import java.util.Map;

public interface IBrandService extends IService<Brand> {

    /**
     * 品牌管理
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Brand> getBrandByPage(int pageNum,int pageSize);

    List<Map<String,Object>> getBrandList();
}
