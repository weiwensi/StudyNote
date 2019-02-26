package com.gysoft.center.news.dao.impl;

import com.gysoft.center.news.dao.NewsBannerProjectDao;
import com.gysoft.center.news.pojo.NewsBanner;
import com.gysoft.center.news.pojo.NewsBannerProject;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * @Description   banner工程关联实现
 * @Author DJZ-WWS
 * @Date 2019/1/23 16:32
 */
@Repository
public class NewsBannerProjectDaoImpl  extends EntityDaoImpl<NewsBannerProject,String> implements NewsBannerProjectDao {
}
