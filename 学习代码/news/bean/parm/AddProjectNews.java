package com.gysoft.center.news.bean.parm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/1/24 11:05
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel(value = "添加项目资讯")
public class AddProjectNews {



    @ApiModelProperty("标题")
    private  String  title;
    @ApiModelProperty("缩略图id")
    private   String  thumbnailId;
    @ApiModelProperty("bannerd的内容")
    private    String   context;
    @ApiModelProperty("编辑人")
    private String editor;
    @ApiModelProperty("所属项目id")
    private  String  projectId;
}
