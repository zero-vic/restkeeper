package com.restkeeper.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/10
 * Description:
 * Version:V1.0
 */
@Component
public class CalculationBusinessLock {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private final String LOCK_VALUE="lockvalue";

    //锁标记
    private boolean lock = false;

    //默认锁时长
    private Integer lockTime = 10;

    /**
     * 定义自旋锁
     */

    public boolean spinLock(String lockKey,RemainderCount remainderCount){
        while(!getLock(lockKey,lockTime)){
            if(remainderCount.getRemainderCount()<=1) {
                return false;
            }
            try{
                TimeUnit.SECONDS.sleep(1);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    //释放锁
    public void unLock(String redisKey) {
        redisTemplate.delete(redisKey);
    }
    public boolean lock(String lockKey, Integer lockTime) {

        return getLock(lockKey, lockTime);
    }

    private boolean getLock(String lockKey,Integer lockTime){
        //设置锁，防止死锁，手动设置一个过去时间
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(lockKey,lockTime);
        if(flag!=null&&flag){
            //添加锁成功
            redisTemplate.expire(lockKey, lockTime, TimeUnit.SECONDS);
            lock = true;
        }else {
            lock = false;
        }
        return lock;
    }

    @FunctionalInterface
    public interface RemainderCount{
        int getRemainderCount();
    }
}
