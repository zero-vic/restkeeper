package com.restkeeper.controller.shop;

import com.restkeeper.vo.PageVO;
import com.restkeeper.shop.entity.Brand;
import com.restkeeper.shop.service.IBrandService;
import com.restkeeper.vo.shop.AddTBrandVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/04/30
 * Description:
 * Version:V1.0
 */
@RestController
@Api(tags = "品牌管理")
@RequestMapping("/brand")
public class BrandController {

    @Reference(version = "1.0.0",check = false)
    private IBrandService brandService;

    /**
     * 查询分页数据及统计信息
     */
    @ApiOperation(value = "分页查询所有品牌信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "pageNum", value = "当前页码", required = false, dataType = "Integer"),
            @ApiImplicitParam(paramType = "path", name = "pageSize", value = "分大小", required = false, dataType = "Integer")})
    @GetMapping(value = "/pageList/{pageNum}/{pageSize}")
    public PageVO<Brand> findListByPage(
            @PathVariable int pageNum, @PathVariable int pageSize) {
        return new PageVO<Brand>(brandService.getBrandByPage(pageNum, pageSize));
    }


    /**
     * 新增品牌
     */
    @ApiOperation(value = "新增品牌")
    @PostMapping(value = "/add")
    public boolean add(@RequestBody AddTBrandVO brandVO){
        Brand brand =new Brand();
        BeanUtils.copyProperties(brandVO, brand);
        return brandService.save(brand);
    }

    @ApiOperation(value = "品牌列表（下拉选择使用）")
    @GetMapping("/brandList")
    @ResponseBody
    public List<Map<String, Object>> list() {
        return brandService.getBrandList();
    }

}
