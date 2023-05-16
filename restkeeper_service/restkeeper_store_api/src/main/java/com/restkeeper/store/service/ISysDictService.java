package com.restkeeper.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.store.entity.SysDictionary;

import java.util.List;


public interface ISysDictService extends IService<SysDictionary> {

    /**
     * 分类获取字典表信息
     * @param category
     * @return
     */
    List<SysDictionary> getDictionaryList(String category);
}
