package com.gysoft.center.news.pojo;

import com.gysoft.utils.jdbc.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description 项目资讯
 * @Author DJZ-WWS
 * @Date 2019/1/23 9:00
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "center_news_project")
public class NewsProject {

    private String id;
    /**
     *项目资讯标题
     */
    private String title;
    /**
     * 缩略图id
     */
    private String thumbnailId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 编辑人
     */
    private String editor;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 上架状态
     */
    private Integer shelfStatus;
    /**
     * 置顶状态
     */
    private Integer isTop;
    /**
     * 项目资讯内容
     */
    private String context;
    /**
     * 置顶时间
     */
    private Date topTime;
    /**
     * 操作人
     */
    private String operator;


}
