package com.gysoft.center.news.bean.parm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Description 资讯请求参数整体一致公用一个实体
 * @Author DJZ-WWS
 * @Date 2019/1/23 10:12
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel(value = "查询banner请求参数")
public class BannerParm {

    @ApiModelProperty("查询关键字")
    private String searchKey;
    @ApiModelProperty("起始时间")
    private Date beginTime;
    @ApiModelProperty("结束时间")
    private Date endTime;
    @ApiModelProperty("当前页")
    private int pageNum;
    @ApiModelProperty("分页尺度")
    private int pageSize;
    @ApiModelProperty("是否上架")
    private int sheftStatus;
    @ApiModelProperty("所属项目id")
    private String projectId;
}
