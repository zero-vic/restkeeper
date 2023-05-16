package com.restkeeper.controller.store;

import com.google.common.collect.Lists;
import com.restkeeper.store.entity.Remark;
import com.restkeeper.store.service.IRemarkService;
import com.restkeeper.vo.store.RemarkVO;
import com.restkeeper.vo.store.SettingsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/06
 * Description:
 * Version:V1.0
 */
@RestController
@Slf4j
@RequestMapping("settings")
@Api(tags = {"门店备注管理"})
public class SettingsController {
    @Reference(version = "1.0.0",check = false)
    private IRemarkService remarkService;

    @ApiOperation(value = "获取门店设置信息")
    @GetMapping("/getSysSettings")
    public SettingsVO getSysSettings(){
        //当前只有门店备注
        List<Remark> remarks= remarkService.getRemarks();
        List<RemarkVO> remarkvos= new ArrayList<>();
        remarks.forEach(d->{
            RemarkVO remarkVO=new RemarkVO();
            remarkVO.setRemarkName(d.getRemarkName());

            //value
            //remarkValue : "[店庆，节假日]"
            String remarkValue = d.getRemarkValue();
            String remarkValue_substring=remarkValue.substring(remarkValue.indexOf("[")+1,remarkValue.indexOf("]"));
            if(StringUtils.isNotEmpty(remarkValue_substring)){
                String[] remark_array= remarkValue_substring.split(",");
                remarkVO.setRemarkValue(Arrays.asList(remark_array));
            }
            remarkvos.add(remarkVO);
        });
        SettingsVO settingsVO =new SettingsVO();
        settingsVO.setRemarks(remarkvos);
        return settingsVO;
    }

    /**
     * {
     *   "remarks": [
     *     {
     *       "remarkName": "string",
     *       "remarkValue": [
     *         "string"
     *       ]
     *     },
     *     {
     *       "remarkName": "string",
     *       "remarkValue": [
     *         "string"
     *       ]
     *     }
     *   ]
     * }
     * @param settingsVO
     * @return
     */
    @ApiOperation(value = "修改门店设置")
    @PutMapping("/update")
    public boolean update(@RequestBody SettingsVO settingsVO){

        List<RemarkVO> remarkVOList = settingsVO.getRemarks();
        List<Remark> remarks = Lists.newArrayList();
        remarkVOList.forEach(remarkVO->{
            Remark remark =new Remark();
            remark.setRemarkName(remarkVO.getRemarkName());
            remark.setRemarkValue(remarkVO.getRemarkValue().toString());
            remarks.add(remark);
        });

        return remarkService.updateRemarks(remarks);
    }
}
