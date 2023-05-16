package com.restkeeper.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.restkeeper.constant.SystemCode;
import com.restkeeper.entity.OrderDetailEntity;
import com.restkeeper.entity.OrderEntity;
import com.restkeeper.enums.PayType;
import com.restkeeper.exception.BussinessException;
import com.restkeeper.redis.CalculationBusinessLock;
import com.restkeeper.service.IOrderService;
import com.restkeeper.store.entity.Table;
import com.restkeeper.store.service.ISellCalculationService;
import com.restkeeper.store.service.ITableService;
import com.restkeeper.utils.SequenceUtils;
import com.restkeeper.vo.DishRequestVO;
import com.restkeeper.vo.DishShopCartVO;
import com.restkeeper.vo.ShopCartVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/10
 * Description:
 * Version:V1.0
 */
@RestController
@Api(tags = {"小程序订单接口"})
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Reference(version = "1.0.0",check = false)
    private ISellCalculationService sellCalculationService;

    @Autowired
    private CalculationBusinessLock calculationBusinessLock;

    @Reference(version = "1.0.0",check = false)
    private ITableService tableService;

    @Reference(version = "1.0.0",check = false)
    private IOrderService orderService;

    @PostMapping("/addDish")
    public boolean addDish(@RequestBody DishRequestVO dishRequestVO){
        if(dishRequestVO.getDishNumber()>0){
            //加菜
            sellCalculationService.plusDish(dishRequestVO.getDishId());

        }else if(dishRequestVO.getDishNumber()<0){
            //减菜
            sellCalculationService.reduceDish(dishRequestVO.getDishId());
        }

        // 获取购物车
        ShopCartVO shopCartVO = getShopCart(dishRequestVO.getTableId());
        //封装购物车菜品信息
        DishShopCartVO dishShopCartVO = null;
        if(!CollectionUtils.isEmpty(shopCartVO.getDishList())){
             dishShopCartVO = shopCartVO.getDishList().stream().filter(d -> d.getId().equals(dishRequestVO.getDishId())).findFirst().get();
        }
        if (dishShopCartVO!=null){
            dishShopCartVO.setNumber(dishShopCartVO.getNumber()+dishRequestVO.getDishNumber());
        }else {
            //新增菜品信息
            dishShopCartVO = new DishShopCartVO();
            dishShopCartVO.setNumber(dishRequestVO.getDishNumber());
            dishShopCartVO.setId(dishRequestVO.getDishId());
            dishShopCartVO.setPrice(dishRequestVO.getPrice());
            dishShopCartVO.setName(dishRequestVO.getDishName());
            dishShopCartVO.setFlavorRemark(dishRequestVO.getFlavorRemark());
            if(dishRequestVO.getType() == SystemCode.DISH_TYPE_MORMAL){
                dishShopCartVO.setType(SystemCode.DISH_TYPE_MORMAL);
            }else if(dishRequestVO.getType() == SystemCode.DISH_TYPE_SETMEAL){
                dishShopCartVO.setType(SystemCode.DISH_TYPE_SETMEAL);
            }
            shopCartVO.getDishList().add(dishShopCartVO);
        }
        //更新redis中的缓存信息
        String key = SystemCode.MIRCO_APP_SHOP_CART_PREFIX+dishRequestVO.getTableId();
        String json = JSON.toJSONString(shopCartVO);
        redisTemplate.opsForValue().set(key,json);
        //通过mq推送消息到客户端
        sendToMQ(dishRequestVO.getTableId(),json);
        return true;

    }

    /**
     * 获取购物车信息
     * @param tableId
     * @return
     */
    @GetMapping("/shopCart/{tableId}")
    public ShopCartVO getshopCart(@PathVariable String tableId){
        return getShopCart(tableId);
    }

    /**
     * 支付
     * @param tableId
     * @return 返回
     */
    @RequestMapping("/pay/{tableId}/{jsCode}")
    public String pay(@PathVariable String tableId, @PathVariable String jsCode){

        String key = SystemCode.MIRCO_APP_SHOP_CART_PREFIX + tableId;
        String json = redisTemplate.opsForValue().get(key);

        //获取购物车
        ShopCartVO shopCartVO = JSON.parseObject(json, ShopCartVO.class);
        if(shopCartVO == null){
            throw new BussinessException("购物车不能为空！");
        }
        if(shopCartVO.getDishList() == null){
            throw new BussinessException("该桌没有点餐");
        }
        boolean lockSucess = calculationBusinessLock.lock(SystemCode.MICRO_APP_LOCK_PREFIX+tableId,10);
        if(!lockSucess){
            throw new BussinessException("该桌有人正在支付");
        }

        Table table = tableService.getById(tableId);
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setPayType(PayType.WeiXin.getValue());
        orderEntity.setPayStatus(SystemCode.ORDER_STATUS_NOTPAY);
        orderEntity.setTableId(tableId);
        String flowCode = SequenceUtils.getSequence(table.getStoreId());
        orderEntity.setOrderNumber(flowCode);
        orderEntity.setTableId(table.getTableId());
        orderEntity.setPayStatus(SystemCode.ORDER_STATUS_NOTPAY);
        orderEntity.setCreateTime(LocalDateTime.now());
        orderEntity.setOrderSource(SystemCode.ORDER_SOURCE_APP);
        orderEntity.setPersonNumbers(shopCartVO.getSeatNumber());

        List<OrderDetailEntity> orderDetails =new ArrayList<>();

        shopCartVO.getDishList().forEach(d->{
            OrderDetailEntity orderDetailEntity =new OrderDetailEntity();
            orderDetailEntity.setTableId(tableId);
            orderDetailEntity.setDishPrice(d.getPrice());
            orderDetailEntity.setDetailStatus(1);
            orderDetailEntity.setDishName(d.getName());
            orderDetailEntity.setDishType(d.getType());
            orderDetailEntity.setDishAmount(d.getNumber()*d.getPrice());
            orderDetailEntity.setDishId(d.getId());
            orderDetailEntity.setDishNumber(d.getNumber());
            if(d.getFlavorRemark() != null) {
                orderDetailEntity.setFlavorRemark(d.getFlavorRemark().toString());
            }
            orderDetails.add(orderDetailEntity);
        });
        orderEntity.setTotalAmount(orderDetails.stream().mapToInt(OrderDetailEntity::getDishAmount).sum());
        orderEntity.setOrderDetails(orderDetails);
        String orderId = orderService.addMicroOrder(orderEntity);

        //todo: 发起支付

        return "";

    }

    /**
     * 获取购物除
     * @param tableId
     * @return
     */
    private ShopCartVO getShopCart(String tableId) {
        String key = SystemCode.MIRCO_APP_SHOP_CART_PREFIX+tableId;
        String cartJson = redisTemplate.opsForValue().get(key);
        ShopCartVO shopCartVO = null;
        if(StringUtils.isEmpty(cartJson)){
            shopCartVO = new ShopCartVO();
            List<DishShopCartVO> dishList = Lists.newArrayList();
            shopCartVO.setDishList(dishList);

        }else {
            shopCartVO = JSON.parseObject(cartJson,ShopCartVO.class);
        }
        return shopCartVO;

    }

    private void sendToMQ(String tableId,String jsonMsg){
        try {
            MessageProperties msgProp = new MessageProperties();
            msgProp.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            msgProp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            Message message = new Message(jsonMsg.getBytes("UTF-8"),msgProp);
            rabbitTemplate.send(SystemCode.MICROSOFT_EXCHANGE_NAME,tableId,message);
        } catch (Exception e) {
            log.error("发送点餐消息失败",e);
        }
    }
}
