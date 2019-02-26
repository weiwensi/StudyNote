package com.gysoft.center.news.bean.parm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/1/23 11:11
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel(value = "banner请求参数")
public class IndustryParam {

    @ApiModelProperty("查询关键字")
    private  String searchKey;
    @ApiModelProperty("起始时间")
    private Date beginTime;
    @ApiModelProperty("结束时间")
    private Date endTime;
    @ApiModelProperty("当前页")
    private  int pageNum;
    @ApiModelProperty("分页尺度")
    private  int pageSize;
    @ApiModelProperty("是否上架")
    private   int  sheftStatus;
}
