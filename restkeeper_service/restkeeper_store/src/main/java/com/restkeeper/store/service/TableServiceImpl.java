package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restkeeper.exception.BussinessException;
import com.restkeeper.store.entity.Table;
import com.restkeeper.store.mapper.TableMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service("tableService")
@Service(version = "1.0.0",protocol = "dubbo")
public class TableServiceImpl extends ServiceImpl<TableMapper, Table> implements ITableService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(Table table) {

        //桌台名称防重
        checkNameExist(table.getTableName());
        return this.save(table);
    }

    @Override
    public IPage<Table> queryPageByAreaId(String areaId, int pageNum, int pageSize) {
        IPage<Table> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Table> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Table::getLastUpdateTime);
        if (!areaId.equals("all")){
            queryWrapper.lambda().eq(Table::getAreaId,areaId);
        }

        return this.page(page, queryWrapper);
    }

    @Override
    public Integer countTableByStatus(String areaId, Integer status) {
        QueryWrapper<Table> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Table::getAreaId,areaId).eq(Table::getStatus,status);


        return this.count(queryWrapper);
    }

    /**
     * 检测重名
     * @param tableName
     */
    private void checkNameExist(String tableName) {

        QueryWrapper<Table> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(Table::getTableId).eq(Table::getTableName,tableName);
        Integer count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BussinessException("该桌台已存在");
        }
    }
}
