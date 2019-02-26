package com.gysoft.center.news.service;

import com.gysoft.bean.login.UserBasicInfo;
import com.gysoft.bean.page.PageResult;
import com.gysoft.center.news.bean.*;
import com.gysoft.center.news.bean.parm.*;
import org.springframework.data.repository.query.Param;


  /**
 * * @author 魏文思
 * * @Date 2019-01-29
 */

public interface NewsService {

    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description：添加banner
     * @Date 13:42 2019/1/29
     * @Param [banner, userBasicInfo, saveorShelf]
     * @version 2.0
     **/


    String addBannerNews(AddBannerParm banner, UserBasicInfo userBasicInfo, Integer saveorShelf) throws Exception;


    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description：删除banner
     * @Date 13:41 2019/1/29
     * @Param [bannerId, userBasicInfo]
     * @version 2.0
     **/

    void deleteBanner(String bannerId, UserBasicInfo userBasicInfo) throws Exception;

    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description：修改banner
     * @Date 13:43 2019/1/29
     * @Param [bannerId, banner, userBasicInfo]
     * @version  2.0
     **/

    void updateBanner(String bannerId, AddBannerParm banner, UserBasicInfo userBasicInfo) throws Exception;

    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description：分页查询banner
     * @Date 13:43 2019/1/29
     * @Param [bannerParm]
     * @version  2.0
     **/

    PageResult<BannerInfo> pageQueryBannerNews(BannerParm bannerParm) throws Exception;


    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description：banner的上架下架操作
     * @Date 13:43 2019/1/29
     * @Param [bannerId, operationType, userBasicInfo]
     * @version  2.0
     **/

    void setStatus(String bannerId, Integer operationType, UserBasicInfo userBasicInfo) throws Exception;


    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description：添加行业资讯
     * @Date 13:44 2019/1/29
     * @Param [addIndustryNews, userBasicInfo, oper]    操作类型  保存或者保存并上架   0保存 1保存并上架
     * @version  2.0
     **/

    String addIndustryNews(AddIndustryNews addIndustryNews, UserBasicInfo userBasicInfo, Integer oper) throws Exception;


    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description：修改行业资讯
     * @Date 13:44 2019/1/29
     * @Param [industryNewsId, addIndustryNews, userBasicInfo]
     * @version  2.0
     **/

    void editIndustry(String industryNewsId, AddIndustryNews addIndustryNews, UserBasicInfo userBasicInfo) throws Exception;


    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description：删除行业资讯
     * @Date 13:45 2019/1/29
     * @Param [industryNewsId, userBasicInfo]
     * @version  2.0
     **/

    void deleteIndustryNews(String industryNewsId, UserBasicInfo userBasicInfo) throws Exception;

    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description：分页查询行业资讯
     * @Date 13:45 2019/1/29
     * @Param [industryParam]
     * @version  2.0
     **/

    PageResult<IndustryInfo> pageQueryIndustryNews(IndustryParam industryParam) throws Exception;


    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description：上架，下架，置顶，取消置顶
     * @Date 13:45 2019/1/29
     * @Param [industryNewsId, operationType, userBasicInfo]
     * @version  2.0
     **/

    void setIndustryNewsStatus(String industryNewsId, Integer operationType, UserBasicInfo userBasicInfo) throws Exception;


    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description：添加项目资讯
     * @Date 13:46 2019/1/29
     * @Param [addProjectNews, userBasicInfo, oper]
     * @version 2.0
     **/

    String addProjectNews(AddProjectNews addProjectNews, UserBasicInfo userBasicInfo, Integer oper) throws Exception;


    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description：修改项目资讯
     * @Date 13:46 2019/1/29
     * @Param [projectNewsId, addProjectNews, userBasicInfo]
     * @version  2.0
     **/

    void editProjectNews(String projectNewsId, AddProjectNews addProjectNews, UserBasicInfo userBasicInfo) throws Exception;


    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description：删除项目资讯
     * @Date 13:46 2019/1/29
     * @Param [projectNewsId, userBasicInfo]
     * @version  2.0
     **/

    void deleteProjectNews(String projectNewsId, UserBasicInfo userBasicInfo) throws Exception;


    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description:行业资讯 项目上架 下架  置顶 取消置顶
     * @Date 13:46 2019/1/29
     * @Param [projectNewsId, operationType, userBasicInfo]
     * @version  2.0
     **/

    void setProjectNewsStatus(String projectNewsId, Integer operationType, UserBasicInfo userBasicInfo) throws Exception;


    /**
     * @return
     * @throws Exception
     * @Author weiwensi
     * @Description:分页查询项目资讯
     * @Date 13:47 2019/1/29
     * @Param [projectParm]
     * @version  2.0
     **/


    PageResult<ProjectNewsInfo> pageQueryProjectNews(ProjectParm projectParm) throws Exception;

}
