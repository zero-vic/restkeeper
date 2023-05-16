package com.restkeeper.service;

import com.restkeeper.entity.DishEs;
import com.restkeeper.entity.SearchResult;

public interface IDishSearchService {
    /**
     * 更据商品码和类型查询信息
     * @param code
     * @param type
     * @param pageNumber
     * @param pageSize
     * @return
     */
    SearchResult<DishEs> searchAllByCode(String code,int type,int pageNumber,int pageSize);

    /**
     * 更具商品码查询信息
     * @param code
     * @param pageNumber
     * @param pageSize
     * @return
     */
    SearchResult<DishEs> searchDishByCode(String code,int pageNumber,int pageSize);

    /**
     * 更具商品名称查询信息
     * @param name
     * @param type
     * @param pageNumber
     * @param pageSize
     * @return
     */
    SearchResult<DishEs> searchDishByName(String name,int type,int pageNumber,int pageSize);
}
