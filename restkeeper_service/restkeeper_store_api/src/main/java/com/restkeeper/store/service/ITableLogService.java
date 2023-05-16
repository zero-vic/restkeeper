package com.restkeeper.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.store.entity.TableLog;

public interface ITableLogService extends IService<TableLog> {

    /**
     * 开桌接口
     * @param tableLog
     * @return
     */
    boolean openTable(TableLog tableLog);

    /**
     *  根据桌台id获取最新开台日志
     * @param tableId
     * @return
     */
    TableLog getOpenTableLog(String tableId);
}
