package com.restkeeper.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DishFlavorVO {
    @ApiModelProperty(value = "口味")
    private String flavor;
    @ApiModelProperty(value = "口味数组")
    private List<String> flavorData;
}
