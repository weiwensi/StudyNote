package com.gysoft.center.news.pojo;

import com.gysoft.utils.jdbc.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description 行业资讯表
 * @Author DJZ-WWS
 * @Date 2019/1/23 8:51
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "center_news_industry")
public class NewsIndustry {


    private String id;
    /**
     * 行业资讯标题
     */
    private String title;
    /**
     * 缩略图id
     */
    private String thumbnailId;

    /**
     * 编辑人
     */
    private String editor;
    /**
     * 上架状态
     */
    private Integer shelfStatus;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 指定功能是否开启   -1置低  0不操作  1置顶
     */
    private Integer isTop;
    /**
     * 置顶时间，用于做排序
     */
    private Date topTime;
    /**
     * banner 内容
     */
    private String context;
    /**
     * 操作人 同banner的editor
     */
    private String operator;


}
