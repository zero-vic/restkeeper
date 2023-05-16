package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restkeeper.exception.BussinessException;
import com.restkeeper.store.entity.Printer;
import com.restkeeper.store.entity.PrinterDish;
import com.restkeeper.store.mapper.PrinterDishMapper;
import com.restkeeper.store.mapper.PrinterMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service("printerService")
@Service(version = "1.0.0",protocol = "dubbo")
public class PrinterServiceImpl extends ServiceImpl<PrinterMapper, Printer> implements IPrinterService {
    @Autowired
    private PrinterDishMapper printerDishMapper;

    @Override
    public boolean addFrontPrinter(Printer printer) {
        return this.save(printer);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean addBackendPrinter(Printer printer, List<String> dishIdList) {
        this.save(printer);
        dishIdList.forEach(d->{
            PrinterDish printerDish = new PrinterDish();
            printerDish.setPrinterId(printer.getPrinterId());
            printerDish.setDishId(d);
            printerDishMapper.insert(printerDish);
        });

        return true;
    }
}
