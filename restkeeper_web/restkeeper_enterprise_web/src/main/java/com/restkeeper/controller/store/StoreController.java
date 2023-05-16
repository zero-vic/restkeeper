package com.restkeeper.controller.store;

import com.restkeeper.constant.SystemCode;
import com.restkeeper.vo.PageVO;
import com.restkeeper.shop.dto.StoreDTO;
import com.restkeeper.shop.entity.Store;
import com.restkeeper.shop.service.IStoreService;
import com.restkeeper.utils.Result;
import com.restkeeper.vo.shop.AddStoreVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 门店信息账号 前端控制器
 * </p>
 */
@Slf4j
@Api(tags = { "门店信息" })
@RestController
@RequestMapping("/store")
public class StoreController {

    @Reference(version = "1.0.0", check=false)
    private IStoreService storeService;


    /**
     * 分页数据
     */
    @ApiOperation(value = "分页查询所有门店")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path", name = "page", value = "当前页码", required = true, dataType = "Integer"),
        @ApiImplicitParam(paramType = "path", name = "pageSize", value = "分大小", required = true, dataType = "Integer"),
        @ApiImplicitParam(paramType = "query", name = "name", value = "门店名称", required = false, dataType = "String") })
    @GetMapping(value = "/pageList/{page}/{pageSize}")
    public PageVO<Store> findListByPage(@PathVariable int page,
                                        @PathVariable int pageSize,
                                        @RequestParam(value = "name",required = false) String name) {
        return new PageVO<Store>(storeService.queryPageByName(page, pageSize,name));
    }
    /**
     * 新增门店
     */
    @ApiOperation(value = "新增数据")
    @PostMapping(value = "/add")
    public boolean add(@RequestBody AddStoreVO storeVO) {
        Store store = new Store();
        BeanUtils.copyProperties(storeVO, store);
        return storeService.save(store);
    }


    /**
     * 门店停用
     */
    @ApiOperation(value = "门店停用")
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键", required = true, dataType = "String")
    @PutMapping(value = "/disabled/{id}")
    public boolean disabled(@PathVariable String id) {
        Store store = storeService.getById(id);
        store.setStatus(SystemCode.FORBIDDEN);
        return storeService.updateById(store);
    }


    @ApiOperation(value = "获取门店省份信息")
    @GetMapping("/listProvince")
    @ResponseBody
    public List<String> listProvince() {
        return storeService.getAllProvince();
    }

    @ApiOperation(value = "根据省份获取门店列表")
    @GetMapping("/getStoreByProvince/{province}")
    @ResponseBody
    public List<StoreDTO> getStoreByProvince(@PathVariable String province) {
        return storeService.getStoreByProvince(province);
    }

    @ApiOperation(value = "获取当前商户管理的门店信息")
    @GetMapping(value = "/listManagerStores")
    public List<StoreDTO> getStoreListByManagerId(){
        return storeService.getStoresByManagerId();
    }

    @ApiOperation(value = "门店切换")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "storeId", value = "门店Id", dataType = "String")})
    @GetMapping(value = "/switchStore/{storeId}")
    public Result switchStore(@PathVariable("storeId") String storeId){
        return storeService.switchStore(storeId);
    }
}