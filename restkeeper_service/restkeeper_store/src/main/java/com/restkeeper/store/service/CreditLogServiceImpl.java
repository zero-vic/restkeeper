package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restkeeper.store.entity.CreditLogs;
import com.restkeeper.store.mapper.CreditLogMapper;
import org.apache.dubbo.config.annotation.Service;

import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Service("creditLogService")
@Service(version = "1.0.0",protocol = "dubbo")
public class CreditLogServiceImpl extends ServiceImpl<CreditLogMapper, CreditLogs> implements ICreditLogService {


    @Override
    public IPage<CreditLogs> queryPage(String creditId, int pageNum, int pageSize) {
        IPage<CreditLogs> page = new Page<>(pageNum, pageSize);

        QueryWrapper<CreditLogs> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CreditLogs::getCreditId, creditId)
                .orderByDesc(CreditLogs::getLastUpdateTime);
        return this.page(page, queryWrapper);
    }

    @Override
    public List<CreditLogs> list(String creditId, LocalDateTime start, LocalDateTime end) {
        QueryWrapper<CreditLogs> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CreditLogs::getCreditId,creditId)
                .between(CreditLogs::getLastUpdateTime,start,end)
                .orderByDesc(CreditLogs::getLastUpdateTime);

        return this.list(queryWrapper);
    }
}
