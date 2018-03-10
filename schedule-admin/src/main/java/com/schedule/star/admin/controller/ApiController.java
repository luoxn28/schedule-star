package com.schedule.star.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.schedule.star.admin.service.ApiService;
import com.schedule.star.core.bean.CallbackParam;
import com.schedule.star.core.bean.RegisterParam;
import com.schedule.star.core.bean.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 与执行器通信Controller
 *
 * @author xiangnan
 * @date 2018/3/10 10:19
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Resource
    private ApiService apiService;

    @PostMapping("/register")
    public Result register(@RequestBody RegisterParam param) {
        if (param == null) {
            return Result.FAIL;
        }

        return apiService.register(param);
    }

    @PostMapping("/keep/alive/{ip}/{port}")
    public Result keepAlive(@PathVariable String ip, @PathVariable int port) {
        if (StrUtil.isBlank(ip) || port <= 0) {
            return Result.FAIL;
        }

        return apiService.keepAlive(ip, port);
    }

    @PostMapping("/callback")
    public Result callback(@RequestBody CallbackParam callbackParam) {
        if (callbackParam == null) {
            return Result.FAIL;
        }

        return apiService.callback(callbackParam);
    }

}
