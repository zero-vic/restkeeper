package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.store.entity.Credit;
import com.restkeeper.store.entity.CreditCompanyUser;

import java.util.List;

public interface ICreditService extends IService<Credit> {

    /**
     * 新增挂账
     * @param credit
     * @param users
     * @return
     */
    boolean add(Credit credit, List<CreditCompanyUser> users);

    /**
     * 分页查询挂账列表
     * @param page
     * @param size
     * @param username
     * @return
     */
    IPage<Credit> queryPage(int page, int size, String username);

    /**
     * 更具id查询挂账信息
     * @param id
     * @return
     */
    Credit queryById(String id);

    /**
     * 挂账修改
     * @param credit
     * @param users
     * @return
     */
    boolean updateInfo(Credit credit, List<CreditCompanyUser> users);
}
