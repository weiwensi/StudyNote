package com.gysoft.center.news.pojo;

import com.gysoft.utils.jdbc.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description banner资讯
 * @Author DJZ-WWS
 * @Date 2019/1/23 8:42
 */
@Data
@Table(name = "center_news_banner")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsBanner {

    /**
     * bannerid
     */
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 缩略图uuid
     */
    private String thumbnailId;

    /**
     * 记录谁登陆系统做了操作
     */
    private String operator;
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
     * banner内容
     */
    private String context;


}
