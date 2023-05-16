package com.restkeeper.vo;

import lombok.Data;

@Data
public class TableVO{
    /**
     * 桌台Id
     */
    private String tableId;
    /**
     * 品牌Logo
     */
    private String brandLogo;
    /**
     * 桌台名称
     */
    private String tableName;
    /**
     * 是否已经开桌
     */
    private boolean opened;
}

