package com.gysoft.center.news.bean.parm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/1/23 19:33
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel(value = "添加banner参数")
public class AddBannerParm {


    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("缩略图id")
    private String thumbnailId;
    @ApiModelProperty("bannerd的内容")
    private String context;
    @ApiModelProperty("banner关联的项目id")
    private List<String> projectIds;


}
