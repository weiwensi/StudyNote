package com.gysoft.center.news.dao.impl;

import com.gysoft.center.news.dao.NewsIndustryDao;
import com.gysoft.center.news.dao.NewsProjectDao;
import com.gysoft.center.news.pojo.NewsProject;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * @Description    项目资讯实现
 * @Author DJZ-WWS
 * @Date 2019/1/25 16:09
 */
@Repository
public class NewsProjectDaoImpl   extends EntityDaoImpl<NewsProject,String> implements NewsProjectDao {
}
