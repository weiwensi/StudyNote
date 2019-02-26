package com.gysoft.center.news.pojo;

import com.gysoft.utils.jdbc.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/1/23 9:08
 */
@Data
@Table(name = "center_news_banner_project")
@NoArgsConstructor
@AllArgsConstructor
public class NewsBannerProject {

    private String id;
    /**
     * banner id
     */
    private String bannerId;
    /**
     * 项目id
     */
    private String projectId;


}
