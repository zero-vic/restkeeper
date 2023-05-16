package com.restkeeper.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.store.entity.Staff;
import com.restkeeper.utils.Result;

public interface IStaffService extends IService<Staff> {

    /**
     * 添加员工
     * @param staff
     * @return
     */
    boolean addStaff(Staff staff);

    /**
     * 员工登录
     * @param shopId
     * @param loginName
     * @param loginPass
     * @return
     */
    Result login(String shopId,String loginName,String loginPass);

}
