package com.aicommunity.controller;

import com.aicommunity.common.Result;
import com.aicommunity.dto.CarouselResponse;
import com.aicommunity.dto.HonorHomeResponse;
import com.aicommunity.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页控制器
 *
 * @author AI Community Team
 */
@Api(tags = "首页")
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    /**
     * 获取首页轮播图
     */
    @ApiOperation(value = "获取首页轮播图", notes = "获取首页轮播图配置")
    @GetMapping("/carousel")
    public Result<CarouselResponse> getCarousel() {
        CarouselResponse response = homeService.getCarousel();
        return Result.success(response);
    }

    /**
     * 获取荣誉殿堂和社区头条信息
     */
    @ApiOperation(value = "获取荣誉殿堂和社区头条信息", notes = "一次性获取荣誉殿堂模块和社区头条的所有数据")
    @GetMapping("/honor")
    public Result<HonorHomeResponse> getHonor() {
        HonorHomeResponse response = homeService.getHonor();
        return Result.success(response);
    }
}
