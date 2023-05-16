package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restkeeper.exception.BussinessException;
import com.restkeeper.redis.CalculationBusinessLock;
import com.restkeeper.store.entity.SellCalculation;
import com.restkeeper.store.mapper.SellCalculationMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service("sellCalculationService")
@Service(version = "1.0.0",protocol = "dubbo")
public class SellCalculationServiceImpl extends ServiceImpl<SellCalculationMapper, SellCalculation> implements ISellCalculationService {
    @Autowired
    private CalculationBusinessLock lock;
    @Override
    public Integer getRemainderCount(String dishId) {
        QueryWrapper<SellCalculation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(SellCalculation::getRemainder).eq(SellCalculation::getDishId,dishId);
        SellCalculation sellCalculation = this.getOne(queryWrapper);
        if (sellCalculation == null){
            //估清表中没数据，代表该菜品为无限制菜品,返回-1
            return -1;
        }
        return sellCalculation.getRemainder();
    }

    @Override
    public void decrease(String dishId, Integer dishNumber) {
        QueryWrapper<SellCalculation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SellCalculation::getDishId,dishId);
        SellCalculation sellCalculation = this.getOne(queryWrapper);
        if (sellCalculation != null){
            int resultCount = sellCalculation.getRemainder()-dishNumber;
            if (resultCount < 0) {
                resultCount = 0;
            }
            sellCalculation.setRemainder(resultCount);
            this.updateById(sellCalculation);
        }
    }

    @Override
    public void add(String dishId, int dishNum) {
        QueryWrapper<SellCalculation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SellCalculation::getDishId,dishId);
        SellCalculation sellCalculation = this.getOne(queryWrapper);
        sellCalculation.setRemainder(sellCalculation.getRemainder()+dishNum);
        this.updateById(sellCalculation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void plusDish(String dishId) {
        //判断沽清
        Integer remainderCount = this.getRemainderCount(dishId);
        if (remainderCount<=0){
            return;
        }
        String redisKey = dishId;
        boolean flag = lock.spinLock(redisKey, () -> getRemainderCount(dishId));
        if(!flag){
            throw new BussinessException("商品已被沽清");
        }else {
            //获取到锁
            try {
                QueryWrapper<SellCalculation> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(SellCalculation::getDishId,dishId);
                SellCalculation sellCalculation = this.getOne(queryWrapper);
                sellCalculation.setRemainder(sellCalculation.getRemainder()-1);
                this.updateById(sellCalculation);
            }finally {
                //释放锁
                lock.unLock(redisKey);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reduceDish(String dishId) {
        //判断沽清
        Integer remainder = this.getRemainderCount(dishId);
        if (remainder<0){
            return;
        }
        QueryWrapper<SellCalculation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SellCalculation::getDishId,dishId);
        SellCalculation calculation = this.getOne(queryWrapper);
        remainder = calculation.getRemainder()+1;
        if (remainder>calculation.getSellLimitTotal()){
            remainder = calculation.getSellLimitTotal();
        }
        calculation.setRemainder(remainder);
        this.updateById(calculation);

    }
}
