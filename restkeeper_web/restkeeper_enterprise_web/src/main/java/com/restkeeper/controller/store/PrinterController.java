package com.restkeeper.controller.store;

import com.restkeeper.store.entity.Printer;
import com.restkeeper.store.service.IPrinterService;
import com.restkeeper.vo.PrinterVO;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"打印接口"})
@RestController
@RequestMapping("/printer")
public class PrinterController{
    @Reference(version = "1.0.0", check=false)
    private IPrinterService printerService;

    @PutMapping
    public boolean AddPrinter(@RequestBody PrinterVO printerVO){
        Printer printer = new Printer();
        printer.setAreaType(printerVO.getAreaType());
        printer.setEnableBeforehand(printerVO.isEnableBeforehand());
        printer.setEnableBill(printerVO.isEnableBill());
        printer.setEnableChangeMenu(printerVO.isEnableChangeMenu());
        printer.setEnableChangeTable(printerVO.isEnableChangeTable());
        printer.setEnableCustomer(printerVO.isEnableCustomer());
        printer.setEnableMadeMenu(printerVO.isEnableMadeMenu());
        printer.setEnableReturnDish(printerVO.isEnableReturnDish());
        printer.setMachineCode(printerVO.getMachineCode());
        printer.setPrinterName(printerVO.getPrinterName());
        if(printerVO.getAreaType() == 1){
            return printerService.addBackendPrinter(printer,printerVO.getDishIdList());
        }else {
            return printerService.addFrontPrinter(printer);
        }
    }
}