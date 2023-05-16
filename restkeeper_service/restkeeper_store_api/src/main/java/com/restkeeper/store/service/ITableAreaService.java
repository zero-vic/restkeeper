package com.restkeeper.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.store.entity.TableArea;

import java.util.List;
import java.util.Map;

public interface ITableAreaService extends IService<TableArea> {
    /**
     * 新增区域借口
     * @param tableArea
     * @return
     */
    boolean add(TableArea tableArea);

    /**
     * 获取区域列表
     * @return
     */
    List<Map<String, Object>> listTableArea();
}
