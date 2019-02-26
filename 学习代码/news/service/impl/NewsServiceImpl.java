package com.gysoft.center.news.service.impl;

import com.gysoft.api.file.api.FileInfoApiService;
import com.gysoft.bean.login.UserBasicInfo;
import com.gysoft.bean.page.Page;
import com.gysoft.bean.page.PageResult;
import com.gysoft.bean.page.Sort;
import com.gysoft.bean.syslog.SaveSysLogParam;
import com.gysoft.bean.syslog.SysLogConstant;
import com.gysoft.center.core.utis.CenterSysLogUtil;
import com.gysoft.center.news.bean.BannerInfo;
import com.gysoft.center.news.bean.IndustryInfo;
import com.gysoft.center.news.bean.ProjectNewsInfo;
import com.gysoft.center.news.bean.parm.*;
import com.gysoft.center.news.constant.NewsConstant;
import com.gysoft.center.news.dao.NewsBannerDao;
import com.gysoft.center.news.dao.NewsBannerProjectDao;
import com.gysoft.center.news.dao.NewsIndustryDao;
import com.gysoft.center.news.dao.NewsProjectDao;
import com.gysoft.center.news.pojo.NewsBanner;
import com.gysoft.center.news.pojo.NewsBannerProject;
import com.gysoft.center.news.pojo.NewsIndustry;
import com.gysoft.center.news.pojo.NewsProject;
import com.gysoft.center.news.service.NewsService;
import com.gysoft.center.project.pojo.Project;
import com.gysoft.utils.exception.AlreadyExistException;
import com.gysoft.utils.exception.ParamInvalidException;
import com.gysoft.utils.exception.ResultException;
import com.gysoft.utils.jdbc.bean.Criteria;
import com.gysoft.utils.jdbc.bean.Joins;
import com.gysoft.utils.jdbc.pojo.IdGenerator;
import com.gysoft.utils.rabbitmq.log.SendSysLogMsgService;
import com.gysoft.utils.util.EmptyUtils;
import com.gysoft.utils.util.lambdaexception.LambdaExceptionUtil;
import com.gysoft.utils.util.regex.RegexUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static com.gysoft.center.core.utis.CenterSysLogUtil.*;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/1/23 14:24
 */

@Service
public class NewsServiceImpl implements NewsService {

    @Resource
    private NewsBannerProjectDao newsBannerProjectDao;
    @Resource
    private NewsBannerDao bannerDao;
    @Resource
    private NewsIndustryDao newsIndustryDao;
    @Resource
    private NewsProjectDao newsProjectDao;
    @Resource
    private SendSysLogMsgService sendSysLogMsgService;
    @Resource
    private FileInfoApiService  fileInfoApiService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addBannerNews(AddBannerParm banner, UserBasicInfo userBasicInfo, Integer saveOrShelt) throws Exception {
        bannerParmCheck(banner);
        if (!(saveOrShelt == 0 || saveOrShelt == 1)) {
            throw new ParamInvalidException("操作类型有误，请选择保存或者保存并上架.");
        }
        List<NewsBanner> title = bannerDao.queryWithCriteria(new Criteria().where("title", banner.getTitle()));
        if (title.size() > 0) {
            throw new AlreadyExistException("该banner标题已经存在，请重新输入");
        }
        String bannerId = IdGenerator.newShortId();
        //保存banner关联的项目
        List<NewsBannerProject> bannerProjectList = new ArrayList<NewsBannerProject>();
        //查出所有相关工程名
        banner.getProjectIds().forEach(projectId -> {
            String bannerProijectId = IdGenerator.newShortId();
            bannerProjectList.add(new NewsBannerProject(bannerProijectId, bannerId, projectId));
        });
        newsBannerProjectDao.batchSave(bannerProjectList);
        NewsBanner newsBanner = NewsBanner.builder().id(bannerId).title(banner.getTitle()).thumbnailId(banner.getThumbnailId()).operator(userBasicInfo.getUserName())
                .updateTime(new Date())
                .createTime(new Date()).context(banner.getContext())
               .operator(userBasicInfo.getUserName()).build();
        if (NewsConstant.SAVE == saveOrShelt) {
            //执行保存
            newsBanner.setShelfStatus(NewsConstant.OFFSHEFT);
        } else {
            newsBanner.setShelfStatus(NewsConstant.ONSHEFT);
        }
        bannerDao.save(newsBanner);
      /* if (EmptyUtils.isNotEmpty(banner.getThumbnailId())) {
            fileInfoApiService.activeFiles(Collections.singletonList(banner.getThumbnailId()));
        }*/
        sendSysLogMsgService.sendMsg(LambdaExceptionUtil.rethrowSupplier(() -> new SaveSysLogParam(userBasicInfo, BANNERLOG.getOperFunc(),
                        CenterSysLogUtil.ADD_BANNER, BANNERLOG.getAddOperDesc(CenterSysLogUtil.ADD_BANNER, banner.getTitle())))
                , SysLogConstant.dispatchRoutingKey(userBasicInfo.getProductNum()));
        return bannerId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBanner(String bannerId, UserBasicInfo userBasicInfo) throws Exception {
        if (EmptyUtils.isEmpty(bannerId)) {
            throw new ParamInvalidException("您必须选择一个删除的banner");
        }
        NewsBanner newsBanner = bannerDao.queryOne(bannerId);
        if (null == newsBanner) {
            throw new ParamInvalidException("你要删除的banner不存在");
        }

        //删除banner关联的g项目id
        newsBannerProjectDao.deleteWithCriteria(new Criteria().where("bannerId", bannerId));
        //删除  banner
        bannerDao.delete(bannerId);
        //删除缩略图
    /*if(EmptyUtils.isNotEmpty(newsBanner.getThumbnailId())){
        fileInfoApiService.markFileDeleteFlag(Collections.singletonList(newsBanner.getThumbnailId()), userBasicInfo.getUserName());
     }*/

        sendSysLogMsgService.sendMsg(LambdaExceptionUtil.rethrowSupplier(() -> new SaveSysLogParam(userBasicInfo, BANNERLOG.getOperFunc(),
                        CenterSysLogUtil.DEL_BANNER, BANNERLOG.getDelOperDesc(CenterSysLogUtil.DEL_BANNER, newsBanner.getTitle())))
                , SysLogConstant.dispatchRoutingKey(userBasicInfo.getProductNum()));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBanner(String bannerId, AddBannerParm bannerParm, UserBasicInfo userBasicInfo) throws Exception {
        if (null == bannerId) {
            throw new ParamInvalidException("您必须指定一个banner");
        }
        bannerParmCheck(bannerParm);
        NewsBanner oneBanner = bannerDao.queryOne(bannerId);
        if (null == oneBanner) {
            throw new ResultException("您要修改的banner不存在");
        }
        if (oneBanner.getTitle().equals(RegexUtils.replaceEspStr(bannerParm.getTitle()))) {
            throw new AlreadyExistException("banner标题已经存在，请重新输入");
        }

        Map<String, String> map = new HashMap<>();
        map.put(oneBanner.getId(), oneBanner.getTitle());
        bannerDao.updateWithCriteria(new Criteria().update("title", bannerParm.getTitle()).update("thumbnailId", bannerParm.getThumbnailId())
                .update("operator", userBasicInfo.getUserName()).update("updateTime", new Date()).update("context", bannerParm.getContext()).where("id", bannerId));
        //删除不存在的项目id
        newsBannerProjectDao.deleteWithCriteria(new Criteria().where("bannerId", bannerId));
        // 添加新的项目
        List<NewsBannerProject> newsBannerProjectList = new ArrayList<>();
        List<String> projectIds = bannerParm.getProjectIds();
        projectIds.forEach(proId -> {
            newsBannerProjectList.add(new NewsBannerProject(IdGenerator.newShortId(), bannerId, proId));
        });
        newsBannerProjectDao.batchSave(newsBannerProjectList);
     //缩略图处理
        //editThumbnailuuId(bannerParm.getThumbnailId(),oneBanner.getThumbnailId(),userBasicInfo);
        sendSysLogMsgService.sendMsg(LambdaExceptionUtil.rethrowSupplier(() -> new SaveSysLogParam(userBasicInfo, BANNERLOG.getOperFunc(),
                        CenterSysLogUtil.EDIT_BANNER, BANNERLOG.getCustomDesc(CenterSysLogUtil.EDIT_BANNER, map.get(bannerId), oneBanner.getTitle())))
                , SysLogConstant.dispatchRoutingKey(userBasicInfo.getProductNum()));
    }

    @Override
    public PageResult<BannerInfo> pageQueryBannerNews(BannerParm bannerParm) throws Exception {

        //banner无置顶功能
        Criteria criteria = new Criteria().select("distinct a.id,a.title,a.thumbnailId,c.projectName,a.updateTime,a.shelfStatus")
                .from(NewsBanner.class).as("a").innerJoin(new Joins().with(NewsBannerProject.class).as("b")
                        .on("b.bannerId", "a.id")).leftJoin(new Joins().with(Project.class)
                        .as("c").on("c.id", "b.projectId"));
        if (bannerParm.getSheftStatus() == NewsConstant.ONSHEFT || bannerParm.getSheftStatus() == NewsConstant.OFFSHEFT) {
            criteria.where("a.shelfStatus", bannerParm.getSheftStatus());
        }

        if (EmptyUtils.isNotEmpty(bannerParm.getProjectId())) {
            //查找指定项目的banner
            criteria.where("b.projectId", bannerParm.getProjectId());
        }
        if (EmptyUtils.isNotEmpty(bannerParm.getSearchKey())) {
            criteria.like("a.title", RegexUtils.replaceEspStr(bannerParm.getSearchKey()));
        }

        if (bannerParm.getBeginTime() != null && bannerParm.getEndTime() != null) {
            if (bannerParm.getBeginTime().compareTo(bannerParm.getEndTime()) == NewsConstant.TIME_COMPARE_RESULT) {
                throw new ParamInvalidException("开始时间不能大于结束时间");
            }
            criteria.gt("a.updateTime", bannerParm.getBeginTime());
            criteria.let("a.updateTime", bannerParm.getEndTime());
        }
        criteria.orderBy(new Sort("a.updateTime"));

        return bannerDao.joinQuery(BannerInfo.class, criteria).pageQuery(new Page(bannerParm.getPageNum(), bannerParm.getPageSize()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setStatus(String bannerId, Integer operationType, UserBasicInfo userBasicInfo) throws Exception {

        //banner的上架，下架功能
        if (EmptyUtils.isEmpty(bannerId)) {
            throw new ParamInvalidException("banner不能为空,请选择你需要操作的banner");
        }
        NewsBanner banner = bannerDao.queryOne(bannerId);
        if (null == banner) {
            throw new ResultException("您操作的资讯信息不存在");
        }
        if (operationType == NewsConstant.OPERATION_TYPR_SHELF) {
            //完成上架功能  修改商品的信息
            if (banner.getShelfStatus() == NewsConstant.ONSHEFT) {
                throw new ParamInvalidException("商品已经上架，无需重复操作！");
            }
            banner.setShelfStatus(NewsConstant.ONSHEFT);

        } else {
            //商品下架
            if (banner.getShelfStatus() == NewsConstant.OFFSHEFT) {
                throw new ParamInvalidException("商品已经下架，无需重复操作！");
            }
            banner.setShelfStatus(NewsConstant.OFFSHEFT);
        }
        banner.setUpdateTime(new Date());
        banner.setOperator(userBasicInfo.getUserName());
        bannerDao.update(banner);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addIndustryNews(AddIndustryNews addIndustryNews, UserBasicInfo userBasicInfo, Integer oper) throws Exception {
        industryParmCheck(addIndustryNews);
        //校验title不能重复
        List<NewsIndustry> title = newsIndustryDao.queryWithCriteria(new Criteria().where("title", addIndustryNews.getTitle()));
        if (title.size() > 0) {
            throw new AlreadyExistException("该标题已经存在，请重新输入");
        }
        String id = IdGenerator.newShortId();
        if (!(oper == 0 || oper == 1)) {
            throw new ParamInvalidException("操作类型有误，请选择保存或者保存并上架");
        }
            //置顶时间没有封装   默认不置顶
            NewsIndustry newsIndustry = NewsIndustry.builder().id(id).createTime(new Date()).editor(addIndustryNews.getEditor())
                    .isTop(NewsConstant.NOTOP).thumbnailId(addIndustryNews.getThumbnailId()).title(addIndustryNews.getTitle()).updateTime(new Date()).context(addIndustryNews.getContext())
                    .operator(userBasicInfo.getUserName()).build();
            //判断操作类型
            if (NewsConstant.SAVE == oper) {
                //执行保存操作，不上架
                newsIndustry.setShelfStatus(NewsConstant.OFFSHEFT);
            } else {
                //执行保存并上架操作
                newsIndustry.setShelfStatus(NewsConstant.ONSHEFT);
            }
        newsIndustryDao.save(newsIndustry);
          // 文件激活
            /* if (EmptyUtils.isNotEmpty(addIndustryNews.getThumbnailId())) {
            fileInfoApiService.activeFiles(Collections.singletonList(addIndustryNews.getThumbnailId()));
        }*/
        sendSysLogMsgService.sendMsg(LambdaExceptionUtil.rethrowSupplier(() -> new SaveSysLogParam(userBasicInfo, INDUSTRYNEWSLOG.getOperFunc(),
                        CenterSysLogUtil.ADD_INDUSTRYNEWS, INDUSTRYNEWSLOG.getAddOperDesc(CenterSysLogUtil.ADD_INDUSTRYNEWS, addIndustryNews.getTitle())))
                , SysLogConstant.dispatchRoutingKey(userBasicInfo.getProductNum()));
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editIndustry(String industryNewsId, AddIndustryNews addIndustryNews, UserBasicInfo userBasicInfo) throws Exception {

        if (EmptyUtils.isEmpty(industryNewsId)) {
            throw new ParamInvalidException("请选择你需要修改的行业资讯");
        }
        industryParmCheck(addIndustryNews);
        NewsIndustry newsIndustry = newsIndustryDao.queryOne(industryNewsId);
        if (null == newsIndustry) {
            throw new ResultException("您操作的资讯信息已被其他管理员删除，请重新选择你要修改的资讯信息");
        }
        Map<String, String> map = new HashMap<>();
        map.put(newsIndustry.getId(), newsIndustry.getTitle());
        newsIndustryDao.updateWithCriteria(new Criteria().update("operator", userBasicInfo.getUserName()).update("updateTime", new Date()).update("title", addIndustryNews.getTitle())
                .update("editor", addIndustryNews.getEditor()).update("thumbnailId", addIndustryNews.getThumbnailId()).update("context", addIndustryNews.getContext())
                .where("id", industryNewsId));

       //缩略图处理
       // editThumbnailuuId(addIndustryNews.getThumbnailId(),newsIndustry.getThumbnailId(),userBasicInfo);


        sendSysLogMsgService.sendMsg(LambdaExceptionUtil.rethrowSupplier(() -> new SaveSysLogParam(userBasicInfo, INDUSTRYNEWSLOG.getOperFunc(),
                        CenterSysLogUtil.EDIT_INDUSTRYNEWS, INDUSTRYNEWSLOG.getCustomDesc(CenterSysLogUtil.EDIT_INDUSTRYNEWS, map.get(industryNewsId), addIndustryNews.getTitle())))
                , SysLogConstant.dispatchRoutingKey(userBasicInfo.getProductNum()));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteIndustryNews(String industryNewsId, UserBasicInfo userBasicInfo) throws Exception {

        if (EmptyUtils.isEmpty(industryNewsId)) {
            throw new ParamInvalidException("请选择需要删除的行业资讯");
        }
        NewsIndustry newsIndustry = newsIndustryDao.queryOne(industryNewsId);
        if (null == newsIndustry) {
            throw new ResultException("删除的行业资讯不存在");
        }
        newsIndustryDao.delete(industryNewsId);
        //删除缩略图
      /* if(EmptyUtils.isNotEmpty(newsIndustry.getThumbnailId())){
            fileInfoApiService.markFileDeleteFlag(Collections.singletonList(newsIndustry.getThumbnailId()), userBasicInfo.getUserName());
        }*/
        sendSysLogMsgService.sendMsg(LambdaExceptionUtil.rethrowSupplier(() -> new SaveSysLogParam(userBasicInfo, INDUSTRYNEWSLOG.getOperFunc(),
                        CenterSysLogUtil.DEL_INDUSTRYNEWS, INDUSTRYNEWSLOG.getCustomDesc(CenterSysLogUtil.DEL_INDUSTRYNEWS, newsIndustry.getTitle())))
                , SysLogConstant.dispatchRoutingKey(userBasicInfo.getProductNum()));
    }

    @Override
    public PageResult<IndustryInfo> pageQueryIndustryNews(IndustryParam industryParam) throws Exception {

        Criteria criteria = new Criteria();

        if (EmptyUtils.isNotEmpty(industryParam.getSearchKey())) {
            criteria.like("title", RegexUtils.replaceEspStr(industryParam.getSearchKey()));
        }

        if (industryParam.getBeginTime() != null && industryParam.getEndTime() != null) {
            if (industryParam.getBeginTime().compareTo(industryParam.getEndTime()) == NewsConstant.TIME_COMPARE_RESULT) {
                throw new ParamInvalidException("开始时间不能大于结束时间！");
            }
            criteria.gt("updateTime", industryParam.getBeginTime());
            criteria.let("updateTime", industryParam.getEndTime());
        }

        if (industryParam.getSheftStatus() == NewsConstant.ONSHEFT || industryParam.getSheftStatus() == NewsConstant.OFFSHEFT) {
            criteria.where("shelfStatus", industryParam.getSheftStatus());
        }
        //查询按照指定时间查询

        criteria.orderBy(new Sort("isTop")).orderBy(new Sort("updateTime"));

        List<NewsIndustry> newsIndustries = newsIndustryDao.queryWithCriteria(criteria);
        PageResult<IndustryInfo> result = new PageResult<>();

        List<IndustryInfo> industryInfos = new ArrayList<>();
        if (newsIndustries != null) {
            newsIndustries.forEach(news -> {
                IndustryInfo info = IndustryInfo.builder().id(news.getId()).title(news.getTitle())
                        .thumbnailId(news.getThumbnailId()).editor(news.getEditor()).sheftStatus(news.getShelfStatus()).updateTime(news.getUpdateTime()).isTop(news.getIsTop()).sheftStatus(news.getShelfStatus()).build();
                industryInfos.add(info);
            });
            result.setList(industryInfos);
            result.setTotal(newsIndustries.size());
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setIndustryNewsStatus(String industryNewsId, Integer operationType, UserBasicInfo userBasicInfo) throws Exception {
        if (EmptyUtils.isEmpty(industryNewsId)) {
            throw new ParamInvalidException("请选择你需要操作的行业资讯");
        }
        //判断哪种类型的操作
        NewsIndustry newsIndustry = newsIndustryDao.queryOne(industryNewsId);
        if (null == newsIndustry) {
            throw new ResultException("您操作的资讯信息不存在");
        }
        switch (operationType) {
            //置顶
            case NewsConstant.OPERATION_TYPR_TOP:
                // 置顶  更新资讯状态
                if (newsIndustry.getIsTop() == NewsConstant.ONTOP) {
                    throw new ParamInvalidException("您已经置顶过，无需重复置顶");
                }

                if(newsIndustry.getShelfStatus()==NewsConstant.OFFSHEFT){
                    //未上架商品不可以置顶
                    throw  new ParamInvalidException("未上架资讯不可以置顶");

                }
                //修改更新时间，修改更新人，修改状态
                newsIndustry.setIsTop(NewsConstant.TOP);
                //修改置顶时间
                newsIndustry.setTopTime(new Date());
                break;
            //上架
            case NewsConstant.OPERATION_TYPR_SHELF:
                if (newsIndustry.getShelfStatus() == NewsConstant.ONSHEFT) {
                    throw new ParamInvalidException("商品已经上架无需重复操作");
                }
                newsIndustry.setShelfStatus(NewsConstant.ONSHEFT);
                break;
            //下架
            case NewsConstant.OPERATION_TYPR_OFFSHEFT:
                if (newsIndustry.getShelfStatus() == NewsConstant.OFFSHEFT) {
                    throw new ParamInvalidException("商品已经变为下架无需重复操作");
                }
                newsIndustry.setShelfStatus(NewsConstant.OFFSHEFT);
                break;
            //取消置顶
            case NewsConstant.OPERATION_TYPE_OFFIOP:
                if (newsIndustry.getIsTop() == NewsConstant.KEEPTOP) {
                    throw new ParamInvalidException("商品已经变为不置顶");
                }
                newsIndustry.setIsTop(NewsConstant.KEEPTOP);
                break;
            default:
                throw new ParamInvalidException("操作类型有误");

        }
        newsIndustry.setUpdateTime(new Date());
        newsIndustry.setOperator(userBasicInfo.getUserName());
        newsIndustryDao.update(newsIndustry);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addProjectNews(AddProjectNews addProjectNews, UserBasicInfo userBasicInfo, Integer oper) throws Exception {
        projectParmCheck(addProjectNews);
        if (!(oper == 0 || oper == 1)) {
            throw new ParamInvalidException("操作类型有误，请选择保存或者保存并上架");
        }
        //标题验重
        List<NewsProject> title = newsProjectDao.queryWithCriteria(new Criteria().where("title", addProjectNews.getTitle()));
        if (title.size() > 0) {
            throw new AlreadyExistException("标题已经存在，请重新输入");
        }

        String id = IdGenerator.newShortId();
        NewsProject newsProject = NewsProject.builder().id(id).title(addProjectNews.getTitle()).editor(addProjectNews.getEditor()).thumbnailId(addProjectNews.getThumbnailId())
                .projectId(addProjectNews.getProjectId()).context(addProjectNews.getContext()).updateTime(new Date()).isTop(NewsConstant.NOTOP)
                .operator(userBasicInfo.getUserName()).build();

        if (oper == NewsConstant.SAVE) {
            //只添加
            newsProject.setShelfStatus(NewsConstant.OFFSHEFT);

        } else {
            //添加并且上架
            newsProject.setShelfStatus(NewsConstant.ONSHEFT);
        }
        newsProjectDao.save(newsProject);
      //文件激活
       /* if (EmptyUtils.isNotEmpty(addProjectNews.getThumbnailId())) {
            fileInfoApiService.activeFiles(Collections.singletonList(addProjectNews.getThumbnailId()));
        }*/
        sendSysLogMsgService.sendMsg(LambdaExceptionUtil.rethrowSupplier(() -> new SaveSysLogParam(userBasicInfo, PROJECTNEWSLOG.getOperFunc(),
                        CenterSysLogUtil.ADD_PROJECTNEWS, PROJECTNEWSLOG.getAddOperDesc(CenterSysLogUtil.ADD_PROJECTNEWS, addProjectNews.getTitle())))
                , SysLogConstant.dispatchRoutingKey(userBasicInfo.getProductNum()));
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editProjectNews(String projectNewsId, AddProjectNews addProjectNews, UserBasicInfo userBasicInfo) throws Exception {

        projectParmCheck(addProjectNews);
        if (EmptyUtils.isEmpty(projectNewsId)) {
            throw new ParamInvalidException("请选择你需要操作的项目资讯");
        }


        if (EmptyUtils.isEmpty(addProjectNews.getTitle())) {
            throw new ParamInvalidException("标题不能为空，请重新输入");
        }
        projectParmCheck(addProjectNews);

        NewsProject newsProject = newsProjectDao.queryOne(projectNewsId);
        if (null == newsProject) {
            throw new ResultException("您操作的工程已经被其他人删除,请重新选择");
        }

        Map<String, String> map = new HashMap<>();
        map.put(newsProject.getId(), newsProject.getTitle());
        newsProjectDao.updateWithCriteria(new Criteria().update("title", addProjectNews.getTitle()).update("editor", addProjectNews.getEditor()).update("thumbnailId", addProjectNews.getThumbnailId())
               .update("projectId", addProjectNews.getProjectId()).update("context", addProjectNews.getContext()).update("updateTime", new Date()).update("operator", userBasicInfo.getUserName()).where("id", projectNewsId));

    //缩略图处理
       // editThumbnailuuId(addProjectNews.getThumbnailId(),newsProject.getThumbnailId(),userBasicInfo);


        sendSysLogMsgService.sendMsg(LambdaExceptionUtil.rethrowSupplier(() -> new SaveSysLogParam(userBasicInfo, PROJECTNEWSLOG.getOperFunc(),
                        CenterSysLogUtil.EDIT_PROJECTNEWS, PROJECTNEWSLOG.getCustomDesc(CenterSysLogUtil.EDIT_PROJECTNEWS, map.get(projectNewsId), addProjectNews.getTitle())))
                , SysLogConstant.dispatchRoutingKey(userBasicInfo.getProductNum()));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProjectNews(String projectNewsId, UserBasicInfo userBasicInfo) throws Exception {
        if (EmptyUtils.isEmpty(projectNewsId)) {
            throw new ParamInvalidException("请选择你需要删除的项目资讯");
        }
        NewsProject newsProject = newsProjectDao.queryOne(projectNewsId);

        if (null == newsProject) {
            throw new ParamInvalidException("您删除的项目资讯已经不存在");
        }
        newsProjectDao.delete(projectNewsId);
       /*if(EmptyUtils.isNotEmpty(newsProject.getThumbnailId())){
            fileInfoApiService.markFileDeleteFlag(Collections.singletonList(newsProject.getThumbnailId()), userBasicInfo.getUserName());
        }*/

        sendSysLogMsgService.sendMsg(LambdaExceptionUtil.rethrowSupplier(() -> new SaveSysLogParam(userBasicInfo, PROJECTNEWSLOG.getOperFunc(),
                        CenterSysLogUtil.DEL_PROJECTNEWS, PROJECTNEWSLOG.getDelOperDesc(CenterSysLogUtil.DEL_PROJECTNEWS, newsProject.getTitle())))
                , SysLogConstant.dispatchRoutingKey(userBasicInfo.getProductNum()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setProjectNewsStatus(String projectNewsId, Integer operationType, UserBasicInfo userBasicInfo) throws Exception {

        if (EmptyUtils.isEmpty(projectNewsId)) {
            throw new ParamInvalidException("请选择你需要操作的项目资讯");
        }
        //判断哪种类型的操作
        NewsProject newsProject = newsProjectDao.queryOne(projectNewsId);

        switch (operationType) {
            //置顶
            case NewsConstant.OPERATION_TYPR_TOP:
                // 置顶  更新资讯状态
                if (newsProject.getIsTop() == NewsConstant.ONTOP) {
                    throw new ParamInvalidException("该项目资讯已经置顶过，无需重复置顶");
                }
                if(newsProject.getShelfStatus()==NewsConstant.OFFSHEFT){
                    //未上架资讯不可以置顶
                    throw  new ParamInvalidException("未上架资讯不可以置顶");

                }
                //修改更新时间，修改更新人，修改状态
                newsProject.setIsTop(NewsConstant.TOP);
                //修改置顶时间
                newsProject.setTopTime(new Date());
                break;
            case NewsConstant.OPERATION_TYPR_SHELF:
                //上架
                if (newsProject.getShelfStatus() == NewsConstant.ONSHEFT) {
                    throw new ParamInvalidException("商品已经上架无需重复操作");
                }
                newsProject.setShelfStatus(NewsConstant.ONSHEFT);
                break;
            //下架
            case NewsConstant.OPERATION_TYPR_OFFSHEFT:
                if (newsProject.getShelfStatus() == NewsConstant.OFFSHEFT) {
                    throw new ParamInvalidException("商品已经变为下架无需重复操作");
                }
                newsProject.setShelfStatus(NewsConstant.OFFSHEFT);
                break;
            //取消置顶
            case NewsConstant.OPERATION_TYPE_OFFIOP:
                //下架
                if (newsProject.getIsTop() == NewsConstant.KEEPTOP) {
                    throw new ParamInvalidException("商品已经变为不置顶");
                }
                newsProject.setIsTop(NewsConstant.KEEPTOP);
                break;
            default:
                throw new ParamInvalidException("操作类型有误");

        }
        newsProject.setUpdateTime(new Date());
        newsProject.setOperator(userBasicInfo.getUserName());
        newsProjectDao.update(newsProject);

    }

    @Override
    public PageResult<ProjectNewsInfo> pageQueryProjectNews(ProjectParm projectParm) throws Exception {

        Criteria criteria = new Criteria();
        criteria.select("a.id", "a.title", "a.thumbnailId", "b.projectName", "a.editor", "a.shelfStatus", "a.updateTime", "a.isTop").from(NewsProject.class).as("a").leftJoin(new Joins().with(Project.class).as("b").on("a.projectId", "b.id"));
        if (EmptyUtils.isNotEmpty(projectParm.getProjectId())) {
            criteria.where("a.projectId", projectParm.getProjectId());
        }

        if (projectParm.getSheftStatus() == NewsConstant.ONSHEFT || projectParm.getSheftStatus() == NewsConstant.OFFSHEFT) {
            criteria.where("a.shelfStatus", projectParm.getSheftStatus());
        }
        if (EmptyUtils.isNotEmpty(projectParm.getSearchKey())) {
            criteria.like("a.title", RegexUtils.replaceEspStr(projectParm.getSearchKey()));
        }

        if (projectParm.getBeginTime() != null && projectParm.getEndTime() != null) {
            if (projectParm.getBeginTime().compareTo(projectParm.getEndTime()) == NewsConstant.TIME_COMPARE_RESULT) {
                throw new ParamInvalidException("开始时间不能大于结束时间.");
            }
            criteria.gt("a.updateTime", projectParm.getBeginTime());
            criteria.let("a.updateTime", projectParm.getEndTime());
        }

        criteria.orderBy(new Sort("a.isTop")).orderBy(new Sort("a.topTime"));

        return newsProjectDao.joinQuery(ProjectNewsInfo.class, criteria).pageQuery(new Page(projectParm.getPageNum(), projectParm.getPageSize()));
    }

    private void projectParmCheck(AddProjectNews addProjectNews) throws ParamInvalidException {

        if (EmptyUtils.isEmpty(addProjectNews.getEditor())) {

            throw new ParamInvalidException("请输入编辑人");
        }
        if (EmptyUtils.isEmpty(addProjectNews.getProjectId())) {

            throw new ParamInvalidException("请选择您要关联的项目");
        }
        if (EmptyUtils.isEmpty(addProjectNews.getThumbnailId())) {
            throw new ParamInvalidException("图片不能为空");
        }
        if (EmptyUtils.isEmpty(addProjectNews.getContext())) {
            throw new ParamInvalidException("资讯信息不能为空");
        }

        if (EmptyUtils.isEmpty(addProjectNews.getTitle())) {
            throw new ParamInvalidException("标题不能为空，请重新输入");
        }


    }

    private void bannerParmCheck(AddBannerParm banner) throws Exception {

        if (EmptyUtils.isEmpty(banner.getTitle())) {
            throw new ParamInvalidException("banner标题不能为空");
        }

        if (EmptyUtils.isEmpty(banner.getThumbnailId())) {
            throw new ParamInvalidException("请选择您需要上传的图片");
        }

        if (EmptyUtils.isEmpty(banner.getContext())) {

            throw new ParamInvalidException("banner内容不能为空");
        }

        if (banner.getProjectIds().size() <= 0) {
            throw new ParamInvalidException("请至少选择一个项目");
        }

    }

    private void industryParmCheck(AddIndustryNews industryNews) throws Exception {

        if (EmptyUtils.isEmpty(industryNews.getTitle())) {
            throw new ParamInvalidException("行业资讯标题不能为空");
        }

        if (EmptyUtils.isEmpty(industryNews.getThumbnailId())) {
            throw new ParamInvalidException("请选择您需要上传的行业资讯图片");
        }

        if (EmptyUtils.isEmpty(industryNews.getContext())) {
            throw new ParamInvalidException("行业资讯内容不能为空");
        }
    }

    public void   editThumbnailuuId(String parmThumbnailId,String objThumbnailId,UserBasicInfo userBasicInfo) throws Exception {

           if(EmptyUtils.isNotEmpty(parmThumbnailId)){
            if(!parmThumbnailId.equals(objThumbnailId)){
                fileInfoApiService.markFileDeleteFlag(Collections.singletonList(objThumbnailId), userBasicInfo.getUserName());
            }
            fileInfoApiService.activeFiles(Collections.singletonList(parmThumbnailId));
          }else{
            if (EmptyUtils.isNotEmpty(parmThumbnailId)){
                fileInfoApiService.markFileDeleteFlag(Collections.singletonList(parmThumbnailId), userBasicInfo.getUserName());
            }
        }

    }





}
