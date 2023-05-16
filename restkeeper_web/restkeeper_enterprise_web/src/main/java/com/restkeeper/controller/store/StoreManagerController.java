package com.restkeeper.controller.store;

import com.restkeeper.vo.PageVO;
import com.restkeeper.shop.entity.StoreManager;
import com.restkeeper.shop.service.IStoreManagerService;
import com.restkeeper.shop.vo.StoreManagerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/04/30
 * Description:
 * Version:V1.0
 */
@RestController
@Api(tags = "门店管理员接口")
@RequestMapping("/storeManger")
public class StoreManagerController {

    @Reference(version = "1.0.0",check = false)
    private IStoreManagerService storeManagerService;
    /**
     * 查询分页数据
     */
    @ApiOperation(value = "查询分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页码", required = false, dataType = "Integer"),
            @ApiImplicitParam(paramType = "path", name = "pageSize", value = "分大小", required = false, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "criteria", value = "查询条件", required = false, dataType = "String") })
    @PostMapping(value = "/pageList/{page}/{pageSize}")
    public PageVO<StoreManager> findListByPage(@PathVariable int page,
                                               @PathVariable int pageSize,
                                               @RequestParam(value = "criteria",required = false) String criteria) {
        return new PageVO<>(storeManagerService.queryPageByCriteria(page,pageSize,criteria));
    }

    /**
     * 添加店长
     */
    @ApiOperation(value = "添加店长")
    @PostMapping(value = "/add")
    public boolean add(@RequestBody StoreManagerVO storeManagerVO) {
        return  storeManagerService.addStoreManager(
                storeManagerVO.getName(),
                storeManagerVO.getPhone(),
                storeManagerVO.getStoreIds());
    }

    /**
     * 门店管理员修改接口
     */
    @ApiOperation(value = "更新数据")
    @PutMapping(value = "/update")
    public boolean update(@RequestBody StoreManagerVO storeManagerVO) {
        return storeManagerService.updateStoreManager(storeManagerVO.getStoreManagerId(),
                storeManagerVO.getName(),
                storeManagerVO.getPhone(),
                storeManagerVO.getStoreIds());
    }

    /**
     * 删除门店管理员
     */
    @ApiOperation(value = "删除数据")
    @DeleteMapping(value = "/del/{id}")
    public boolean delete(@PathVariable String id) {
        return storeManagerService.deleteStoreManager(id);
    }

    /**
     * 停用
     */
    @ApiOperation(value = "门店管理员停用")
    @PutMapping(value = "/pause/{id}")
    public boolean pause(@PathVariable String id) {
        return storeManagerService.pauseStoreManager(id);
    }
}
