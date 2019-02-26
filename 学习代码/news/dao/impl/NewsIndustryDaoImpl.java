package com.gysoft.center.news.dao.impl;

import com.gysoft.center.news.dao.NewsIndustryDao;
import com.gysoft.center.news.pojo.NewsIndustry;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * @Description  行业资讯实现
 * @Author DJZ-WWS
 * @Date 2019/1/25 9:59
 */
@Repository
public class NewsIndustryDaoImpl   extends EntityDaoImpl<NewsIndustry,String> implements NewsIndustryDao {
}
