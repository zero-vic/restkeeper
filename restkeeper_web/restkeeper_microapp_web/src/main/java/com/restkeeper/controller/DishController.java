package com.restkeeper.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.restkeeper.constant.SystemCode;
import com.restkeeper.exception.BussinessException;
import com.restkeeper.service.IDishSearchService;
import com.restkeeper.store.entity.Dish;
import com.restkeeper.store.entity.DishFlavor;
import com.restkeeper.store.entity.SetMeal;
import com.restkeeper.store.service.*;
import com.restkeeper.vo.DishCategoryVO;
import com.restkeeper.vo.DishFlavorVO;
import com.restkeeper.vo.DishVO;
import com.restkeeper.vo.PageVO;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/10
 * Description:
 * Version:V1.0
 */
@Api(tags = {"菜品搜索相关接口"})
@RestController
@RequestMapping("/dish")
public class DishController {

    @Reference(version = "1.0.0", check=false)
    private IDishService dishService;

    @Reference(version = "1.0.0", check=false)
    private ISetMealService setMealService;

    @Reference(version = "1.0.0",check = false)
    IDishCategoryService dishCategoryService;

    @Reference(version = "1.0.0",check = false)
    ISetMealDishService setMealDishService;

    @Reference(version = "1.0.0", check=false)
    private IDishSearchService dishSearchService;

    @Reference(version = "1.0.0", check=false)
    private IRemarkService remarkService;

    @Reference(version = "1.0.0", check=false)
    private IDishFlavorService dishFlavorService;
    @Reference(version = "1.0.0", check = false)
    private ISellCalculationService sellCalculationService;

    /**
     * 获取套餐和产品分类类别
     * @return
     */
    @GetMapping("/category")
    public List<DishCategoryVO> getCategory(){
        return  dishCategoryService
                .getAllCategory()
                .stream()
                .map(d->{
                    DishCategoryVO vo = new DishCategoryVO();
                    vo.setCategoryId(d.getCategoryId());
                    vo.setName(d.getName());
                    vo.setType(d.getType());

                    return vo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询可用的菜品列表
     */
    @GetMapping("/findEnableDishList/{categoryId}")
    public List<Map<String,Object>> findEnableDishList(@PathVariable String categoryId,
                                                       @RequestParam(value = "name",defaultValue = "") String name){
        return dishService.findEnableDishListInfo(categoryId, name);
    }

    /**
     * 分页获取
     * @param categoryId
     * @param type
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/dishPageList/{categoryId}/{type}/{page}/{pageSize}")
    public PageVO<DishVO> getDishByCategory(@PathVariable String categoryId,
                                            @PathVariable int type,
                                            @PathVariable long page,
                                            @PathVariable long pageSize) {

        PageVO<DishVO> result = new PageVO<>();
        if (type == SystemCode.DISH_TYPE_MORMAL) {
            IPage<Dish> dishPage = dishService.queryByCategory(categoryId, page, pageSize);
            result.setPages(dishPage.getPages());
            result.setPage(dishPage.getCurrent());
            result.setPagesize(dishPage.getSize());
            result.setCounts(dishPage.getTotal());
            result.setItems(dishPage
                    .getRecords()
                    .stream()
                    .map(d -> {
                        DishVO dishVO = new DishVO();
                        dishVO.setDishId(d.getId());
                        dishVO.setDishName(d.getName());
                        dishVO.setPrice(d.getPrice());
                        dishVO.setType(SystemCode.DISH_TYPE_MORMAL);
                        dishVO.setDesc(d.getDescription());
                        dishVO.setImageUrl(d.getImage());
                        dishVO.setRemainder(sellCalculationService.getRemainderCount(d.getId()));
                        return dishVO;
                    }).collect(Collectors.toList())
            );
            return result;
        } else if (type == SystemCode.DISH_TYPE_SETMEAL) {
            IPage<SetMeal> dishPage = setMealService.queryByCategory(categoryId,page,pageSize);
            result.setPages(dishPage.getPages());
            result.setPage(dishPage.getCurrent());
            result.setPagesize(dishPage.getSize());
            result.setCounts(dishPage.getTotal());
            result.setItems(dishPage
                    .getRecords()
                    .stream()
                    .map(s->{
                        DishVO dishVO = new DishVO();
                        dishVO.setDishId(s.getId());
                        dishVO.setDishName(s.getName());
                        dishVO.setPrice(s.getPrice());
                        dishVO.setType(SystemCode.DISH_TYPE_SETMEAL);
                        dishVO.setDesc(s.getDescription());
                        dishVO.setImageUrl(s.getImage());
                        dishVO.setRemainder(sellCalculationService.getRemainderCount(s.getId()));
                        return  dishVO;
                    }).collect(Collectors.toList())
            );
            return result;
        }

        throw new BussinessException("请选择正确的分类");
    }


    /**
     * 根据菜品id获取口味信息
     */
    @GetMapping("/flavor/{dishId}")
    public List<DishFlavorVO> dishFlavor(@PathVariable String dishId){
        List<DishFlavor> dishFlavors= dishFlavorService.getFlavor(dishId);
        List<DishFlavorVO> dishFlavorVOList =new ArrayList<>();
        dishFlavors.forEach(d->{
            DishFlavorVO dishFlavorVO=new DishFlavorVO();
            dishFlavorVO.setFlavor(d.getFlavorName());
            String flavorValue = d.getFlavorValue();
            String remarkValue_substring=flavorValue.substring(flavorValue.indexOf("[")+1,flavorValue.indexOf("]"));
            if(StringUtils.isNotEmpty(remarkValue_substring)){
                String[] flavor_array= remarkValue_substring.split(",");
                dishFlavorVO.setFlavorData(Arrays.asList(flavor_array));
            }
            dishFlavorVOList.add(dishFlavorVO);
        });
        return dishFlavorVOList;
    }



}
