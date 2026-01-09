package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 首页控制器
 *
 * @author AI Community Team
 */
@Api(tags = "首页相关接口")
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    /**
     * 获取首页轮播图
     *
     * @return 轮播图列表
     */
    @ApiOperation(value = "获取首页轮播图", notes = "获取首页轮播图配置")
    @GetMapping("/carousel")
    public Result<?> getCarousel() {
        return Result.success(homeService.getCarousel());
    }

    /**
     * 获取荣誉殿堂和社区头条信息
     *
     * @return 荣誉殿堂和社区头条数据
     */
    @ApiOperation(value = "获取荣誉殿堂和社区头条", notes = "一次性获取荣誉殿堂模块和社区头条的所有数据")
    @GetMapping("/honor")
    public Result<?> getHonor() {
        return Result.success(homeService.getHonor());
    }
}
