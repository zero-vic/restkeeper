package com.restkeeper.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restkeeper.shop.entity.Brand;
import com.restkeeper.shop.mapper.BrandMapper;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;
import java.util.Map;

@Service(version = "1.0.0",protocol = "dubbo")
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {
    @Override
    public IPage<Brand> getBrandByPage(int pageNum, int pageSize) {

        IPage<Brand> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Brand::getLastUpdateTime);
        return this.page(page,queryWrapper);
    }

    @Override
    public List<Map<String, Object>> getBrandList() {
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(Brand::getBrandId,Brand::getBrandName);

        return this.listMaps(queryWrapper);
    }
}
