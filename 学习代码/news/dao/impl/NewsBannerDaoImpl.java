package com.gysoft.center.news.dao.impl;

import com.gysoft.center.news.dao.NewsBannerDao;
import com.gysoft.center.news.pojo.NewsBanner;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * @Description   资讯banner dao层实现
 * @Author DJZ-WWS
 * @Date 2019/1/23 14:39
 */
@Repository
public class NewsBannerDaoImpl  extends EntityDaoImpl<NewsBanner,String> implements NewsBannerDao {
}
