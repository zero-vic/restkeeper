package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.store.entity.Printer;

import java.util.List;

public interface IPrinterService extends IService<Printer> {
    /**
     * 添加前台打印机
     * @param printer
     * @return
     */
    boolean addFrontPrinter(Printer printer);

    /**
     * 添加后厨打印机
     * @param printer
     * @return
     */
    boolean addBackendPrinter(Printer printer, List<String> dishIdList);

}
