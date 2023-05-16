package com.restkeeper.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.store.entity.CreditRepayment;

public interface ICreditRepaymentService extends IService<CreditRepayment> {
    /**
     * 还款接口
     * @param repayment
     * @return
     */
    boolean repayment(CreditRepayment repayment);
}
