package com.gysoft.center.news.bean;

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
 * @Date 2019/1/23 10:20
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel(value = "banner返回值")
public class BannerInfo {
    @ApiModelProperty("bannerid")
    private   String id;
    @ApiModelProperty("标题")
    private  String  title;
    @ApiModelProperty("缩略图id")
    private String  thumbnailId;
    @ApiModelProperty("所属项目名称")
    private   String projectName;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("上架状态")
    private int sheftStatus;
    @ApiModelProperty("编辑人")
    private  String editor;

}
