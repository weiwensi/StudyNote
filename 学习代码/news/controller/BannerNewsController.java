package com.gysoft.center.news.controller;

import com.gysoft.bean.page.PageResult;
import com.gysoft.center.news.bean.*;
import com.gysoft.center.news.bean.parm.*;
import com.gysoft.center.news.service.NewsService;
import com.gysoft.coutils.swagger.ApiVersionConstant;
import com.gysoft.sso.bean.GyBasicSession;
import com.gysoft.utils.swagger.ApiVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description banner 接口
 * @Author DJZ-WWS
 * @Date 2019/1/23 10:04
 */

@RestController
@Api(value = "banner信息", tags = "banner信息")
@RequestMapping("/news/")
public class BannerNewsController extends GyBasicSession {

    private static final Logger logger = LoggerFactory.getLogger(BannerNewsController.class);
    @Resource
    private NewsService newsService;

    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @PostMapping(value = "pageQueryBannerNewsInfo/")
    @ApiOperation(value = "分页获取banner资讯信息", notes = "分页获取banner资讯信息", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<BannerInfo> pageQueryBannerNews(@RequestBody BannerParm bannerParm) throws Exception {
        try {
            return newsService.pageQueryBannerNews(bannerParm);
        } catch (Exception e) {
            logger.error("pageQueryBannerNews error,bannerParm={}",bannerParm, e);
            throw e;
        }
    }


    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @PostMapping(value = "addBanner/{isShelf}")
    @ApiOperation(value = "添加banner", notes = "添加banner", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name="isShelf",value = "0保存，1保存并上架",dataType = "int",paramType = "path",required = true)
    public String addBannerNews(@RequestBody AddBannerParm banner, @PathVariable Integer isShelf) throws Exception {
        try {
            return newsService.addBannerNews(banner, getUserBasicInfo(), isShelf);
        } catch (Exception e) {
            logger.error("addBannerNews error,banner={},saveorShelf={}", banner,isShelf, e);
            throw e;
        }
    }


    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @PostMapping(value = "editBanner/{bannerId}")
    @ApiOperation(value = "修改banner", notes = "修改banner", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateBanner(@PathVariable String bannerId, @RequestBody AddBannerParm banner) throws Exception {
        try {
            newsService.updateBanner(bannerId, banner, getUserBasicInfo());
        } catch (Exception e) {
            logger.error("updateBanner error,bannerid={},edit banner parm={}", bannerId,banner, e);
            throw e;
        }

    }

    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @DeleteMapping(value = "deleteBanner/{bannerId}")
    @ApiOperation(value = "删除banner", notes = "删除banner", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteBanner(@PathVariable String bannerId) throws Exception {
        try {
            newsService.deleteBanner(bannerId, getUserBasicInfo());
        } catch (Exception e) {
            logger.error("deleteBanner banner error,bannerid={}", bannerId, e);
            throw e;
        }
    }

    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @GetMapping(value = "onSheltBanner/{bannerId}/{operationType}")
    @ApiOperation(value = "banner上架,下架", notes = "banner上架，下架", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name="operationType",value = "1 上架，-1下架",dataType = "int",paramType = "path",required = true)
    public void setStatus(@PathVariable String bannerId, @PathVariable Integer operationType) throws Exception {

        try {
            newsService.setStatus(bannerId, operationType, getUserBasicInfo());
        } catch (Exception e) {
            logger.error("setStatus error,operationType={},bannerId={}", operationType,bannerId, e);
            throw e;
        }

    }



}
