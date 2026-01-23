package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.HomeService;
import com.aicommunity.vo.CarouselItemVO;
import com.aicommunity.vo.CarouselListVO;
import com.aicommunity.vo.EmpowermentListVO;
import com.aicommunity.vo.EmpowermentPostVO;
import com.aicommunity.vo.HonorInfoVO;
import com.aicommunity.vo.HonorResponseVO;
import com.aicommunity.vo.PracticesInfoVO;
import com.aicommunity.vo.LatestWinnerListVO;
import com.aicommunity.vo.LatestWinnerVO;
import com.aicommunity.vo.ToolBannerItemVO;
import com.aicommunity.vo.ToolBannerListVO;
import com.aicommunity.vo.ToolPlatformItemVO;
import com.aicommunity.vo.ToolPlatformListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页控制器
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@RestController
@RequestMapping("/api/home")
@Api(tags = "首页接口")
public class HomeController {

    @Autowired
    private HomeService homeService;

    /**
     * 获取首页轮播图
     *
     * @return 轮播图列表
     */
    @GetMapping("/carousel")
    @ApiOperation(value = "获取首页轮播图", notes = "获取首页顶部轮播图配置")
    public Result<CarouselListVO> getCarousel() {
        log.info("获取首页轮播图");
        List<CarouselItemVO> list = homeService.getCarouselList();
        CarouselListVO vo = new CarouselListVO();
        vo.setList(list);
        return Result.success(vo);
    }

    /**
     * 获取荣誉殿堂数据
     *
     * @return 荣誉殿堂信息
     */
    @GetMapping("/honor")
    @ApiOperation(value = "获取荣誉殿堂数据", notes = "获取荣誉殿堂配置，包括banner图、奖项列表、Top用户")
    public Result<HonorResponseVO> getHonor() {
        log.info("获取荣誉殿堂数据");
        HonorInfoVO honorInfo = homeService.getHonorInfo();
        HonorResponseVO vo = new HonorResponseVO();
        vo.setHonor(honorInfo);
        return Result.success(vo);
    }

    /**
     * 获取赋能交流列表
     *
     * @param limit 返回数量，默认5
     * @return 帖子列表
     */
    @GetMapping("/empowerment")
    @ApiOperation(value = "获取赋能交流列表", notes = "获取首页赋能交流板块的帖子列表")
    public Result<EmpowermentListVO> getEmpowerment(
            @ApiParam(value = "返回数量，默认5", example = "5")
            @RequestParam(required = false, defaultValue = "5") Integer limit) {
        log.info("获取赋能交流列表, limit={}", limit);
        List<EmpowermentPostVO> list = homeService.getEmpowermentList(limit);
        EmpowermentListVO vo = new EmpowermentListVO();
        vo.setList(list);
        return Result.success(vo);
    }

    /**
     * 获取AI优秀实践列表
     *
     * @param limit 每个分类返回的数量，默认5
     * @return AI优秀实践信息
     */
    @GetMapping("/practices")
    @ApiOperation(value = "获取AI优秀实践列表", notes = "获取首页AI优秀实践列表（分类展示：培训赋能、AI训战、用户交流）")
    public Result<PracticesInfoVO> getPractices(
            @ApiParam(value = "每个分类返回的数量，默认5", example = "5")
            @RequestParam(required = false, defaultValue = "5") Integer limit) {
        log.info("获取AI优秀实践列表, limit={}", limit);
        PracticesInfoVO info = homeService.getPracticesInfo(limit);
        return Result.success(info);
    }

    /**
     * 获取悬浮工具平台列表
     *
     * @return 工具列表
     */
    @GetMapping("/tool-platform")
    @ApiOperation(value = "获取悬浮工具平台列表", notes = "获取首页右侧悬浮工具平台的工具列表（与AI工具专区独立）")
    public Result<ToolPlatformListVO> getToolPlatform() {
        log.info("获取悬浮工具平台列表");
        List<ToolPlatformItemVO> list = homeService.getToolPlatformList();
        ToolPlatformListVO vo = new ToolPlatformListVO();
        vo.setList(list);
        return Result.success(vo);
    }

    /**
     * 获取工具专区轮播图
     *
     * @param toolId 指定工具ID（可选）
     * @return Banner列表
     */
    @GetMapping("/tool-banners")
    @ApiOperation(value = "获取工具专区轮播图", notes = "获取AI工具专区轮播图Banner")
    public Result<ToolBannerListVO> getToolBanners(
            @ApiParam(value = "指定工具ID，返回该工具相关的Banner", example = "1")
            @RequestParam(required = false) Long toolId) {
        log.info("获取工具专区轮播图, toolId={}", toolId);
        List<ToolBannerItemVO> list = homeService.getToolBanners(toolId);
        ToolBannerListVO vo = new ToolBannerListVO();
        vo.setList(list);
        return Result.success(vo);
    }

    /**
     * 获取最新获奖者列表（AI使用达人展示）
     *
     * @param limit 返回数量，默认9
     * @return 最新获奖者列表
     */
    @GetMapping("/latest-winners")
    @ApiOperation(value = "获取最新获奖者列表", notes = "获取最新的个人获奖者列表，用于首页AI使用达人区域展示")
    public Result<LatestWinnerListVO> getLatestWinners(
            @ApiParam(value = "返回数量，默认9", example = "9")
            @RequestParam(required = false, defaultValue = "9") Integer limit) {
        log.info("获取最新获奖者列表, limit={}", limit);
        List<LatestWinnerVO> list = homeService.getLatestWinners(limit);
        LatestWinnerListVO vo = new LatestWinnerListVO();
        vo.setList(list);
        return Result.success(vo);
    }
}
