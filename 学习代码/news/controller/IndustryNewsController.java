package com.gysoft.center.news.controller;

import com.gysoft.bean.page.PageResult;
import com.gysoft.center.news.bean.IndustryInfo;
import com.gysoft.center.news.bean.parm.AddIndustryNews;
import com.gysoft.center.news.bean.parm.IndustryParam;
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
 * @Description   行业资讯接口
 * @Author DJZ-WWS
 * @Date 2019/2/1 9:44
 */
@RestController
@Api(value = "行业资讯", tags = "行业资讯")
@RequestMapping("/news/")
public class IndustryNewsController  extends GyBasicSession {

    private static final Logger logger = LoggerFactory.getLogger(IndustryNewsController.class);
    @Resource
    private NewsService newsService;


    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @PostMapping(value = "pageQueryIndustryNewsInfo")
    @ApiOperation(value = "分页获取行业资讯信息", notes = "分页获取行业资讯信息", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<IndustryInfo> pageQueryIndustryNews(@RequestBody IndustryParam industryParam) throws Exception {

        try {
            return newsService.pageQueryIndustryNews(industryParam);
        } catch (Exception e) {
            logger.error(" pageQueryIndustryNews error,industryParam={}",industryParam, e);
            throw e;
        }

    }


    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @PostMapping(value = "addIndustryNews/{isShelf}")
    @ApiImplicitParam(name="isShelf",value = "0 保存，1保存并上架",dataType = "int",paramType = "path",required = true)
    @ApiOperation(value = "添加行业资讯", notes = "添加行业资讯", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addIndustryNews(@RequestBody AddIndustryNews addIndustryNews, @PathVariable Integer isShelf) throws Exception {
        try {
            return newsService.addIndustryNews(addIndustryNews, getUserBasicInfo(), isShelf);
        } catch (Exception e) {
            logger.error("addIndustryNews  error,addIndustryNews={},saveorShelf={}", addIndustryNews,isShelf, e);
            throw e;
        }
    }

    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @PostMapping(value = "editIndustryNews/{industryNewsId}")
    @ApiOperation(value = "修改行业资讯", notes = "修改行业资讯", produces = MediaType.APPLICATION_JSON_VALUE)
    public void editIndustry(@PathVariable String industryNewsId, @RequestBody AddIndustryNews addIndustryNews) throws Exception {
        try {
            newsService.editIndustry(industryNewsId, addIndustryNews, getUserBasicInfo());
        } catch (Exception e) {
            logger.error("editIndustry  error,editIndustryNews={},industryNewsId={}", addIndustryNews,industryNewsId, e);
            throw e;
        }
    }


    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @DeleteMapping(value = "deleteIndustryNews/{industryNewsId}")
    @ApiOperation(value = "删除行业资讯", notes = "删除行业资讯", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteIndustryNews(@PathVariable String industryNewsId) throws Exception {
        try {
            newsService.deleteIndustryNews(industryNewsId, getUserBasicInfo());
        } catch (Exception e) {
            logger.error("deleteIndustryNews  error,industryNewsId={}", industryNewsId, e);
            throw e;
        }
    }

    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @GetMapping(value = "onSheltIndustryNews/{industryNewsId}/{operationType}")
    @ApiOperation(value = "行业资讯上架 ,下架,置顶,取消置顶", notes = "行业资讯上架 ,下架,置顶，取消置顶", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name="operationType",value = "1 上架，-1下架，0置顶，2 取消置顶",dataType = "int",paramType = "path",required = true)
    public void setIndustryNewsStatus(@PathVariable String industryNewsId, @PathVariable Integer operationType) throws Exception {
        try {
            newsService.setIndustryNewsStatus(industryNewsId, operationType, getUserBasicInfo());
        } catch (Exception e) {
            logger.error("setIndustryNewsStatus  error,operationType={},industryNewsId={}", operationType,industryNewsId, e);
            throw e;
        }

    }
}
