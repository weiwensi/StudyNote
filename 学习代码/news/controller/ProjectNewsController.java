package com.gysoft.center.news.controller;

import com.gysoft.bean.page.PageResult;
import com.gysoft.center.news.bean.ProjectNewsInfo;
import com.gysoft.center.news.bean.parm.AddProjectNews;
import com.gysoft.center.news.bean.parm.ProjectParm;
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
 * @Description   项目资讯
 * @Author DJZ-WWS
 * @Date 2019/2/1 9:44
 */

@RestController
@Api(value = "项目资讯", tags = "项目资讯")
@RequestMapping("/news/")
public class ProjectNewsController   extends GyBasicSession {


    private static final Logger logger = LoggerFactory.getLogger(ProjectNewsController.class);
    @Resource
    private NewsService newsService;


    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @PostMapping(value = "pageQueryProjectNewsInfo/")
    @ApiOperation(value = "分页获取项目资讯", notes = "分页获取项目资讯", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<ProjectNewsInfo> pageQueryProjectNews(@RequestBody ProjectParm projectParm) throws Exception {
        try {
            return newsService.pageQueryProjectNews(projectParm);
        } catch (Exception e) {
            logger.error("pageQueryProjectNews  error,projectParm={}", projectParm,e);
            throw e;
        }
    }

    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @PostMapping(value = "addProjectNews/{operationType}")
    @ApiOperation(value = "添加ProjectNews", notes = "添加ProjectNews", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name="operationType",value = "0 保存，1保存并上架",dataType = "int",paramType = "path",required = true)
    public String addProjectNews(@RequestBody AddProjectNews addProjectNews,@PathVariable Integer operationType) throws Exception {
        try {
            return newsService.addProjectNews(addProjectNews, getUserBasicInfo(), operationType);
        } catch (Exception e) {
            logger.error("addProjectNews  error,addProjectNews={},operationType={}", addProjectNews,operationType, e);
            throw e;
        }
    }

    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @PostMapping(value = "editProjectNews/{projectNewsId}")
    @ApiOperation(value = "修改ProjectNews", notes = "修改ProjectNews", produces = MediaType.APPLICATION_JSON_VALUE)
    public void editProjectNews(@PathVariable(required = true) String projectNewsId, @RequestBody AddProjectNews addProjectNews) throws Exception {
        try {
            newsService.editProjectNews(projectNewsId, addProjectNews, getUserBasicInfo());
        } catch (Exception e) {
            logger.error("editProjectNews  error,editParm={},projectNewsId={}", addProjectNews,projectNewsId, e);
            throw e;
        }
    }



    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @DeleteMapping(value = "deleteProjectNews/{projectNewsId}")
    @ApiOperation(value = "删除ProjectNews", notes = "删除ProjectNews", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteProjectNews(@PathVariable String projectNewsId) throws Exception {
        try {
            newsService.deleteProjectNews(projectNewsId, getUserBasicInfo());
        } catch (Exception e) {
            logger.error("deleteProjectNews  error,projectNewsId={}", projectNewsId, e);
            throw e;
        }
    }

    @ApiVersion(group = ApiVersionConstant.CO_V200)
    @GetMapping(value = "onSheltProjectNews/{projectNewsId}/{operationType}")
    @ApiOperation(value = "ProjectNews上架，下架，置顶,取消置顶", notes = "ProjectNews上架，下架，置顶，取消置顶", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name="operationType",value = "1 上架，-1下架，0置顶，2 取消置顶",dataType = "int",paramType = "path",required = true)
    public void setProjectNewsStatus(@PathVariable String projectNewsId, @PathVariable Integer operationType) throws Exception {
        try {
            newsService.setProjectNewsStatus(projectNewsId, operationType, getUserBasicInfo());
        } catch (Exception e) {
            logger.error("setProjectNewsStatus  error,operationType={},projectNewsId={}", operationType,projectNewsId, e);
            throw e;
        }
    }

}
